/**
 * Redistribution  and use  in source  and binary  forms, with  or without
 * modification, are permitted provided  that the following conditions are
 * met :
 *
 * . Redistributions  of  source  code  must  retain  the  above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * . Redistributions in  binary form  must reproduce  the above  copyright
 *   notice, this list of conditions  and the following disclaimer in  the
 *   documentation and/or other materials provided with the distribution.
 *
 * . The name of the author may not be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE  AUTHOR ``AS IS''  AND ANY EXPRESS  OR
 * IMPLIED  WARRANTIES,  INCLUDING,  BUT   NOT  LIMITED  TO,  THE   IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND  FITNESS FOR A PARTICULAR  PURPOSE ARE
 * DISCLAIMED.  IN NO  EVENT SHALL  THE AUTHOR  BE LIABLE  FOR ANY  DIRECT,
 * INDIRECT,  INCIDENTAL,  SPECIAL,  EXEMPLARY,  OR  CONSEQUENTIAL  DAMAGES
 * (INCLUDING,  BUT  NOT LIMITED  TO,  PROCUREMENT OF  SUBSTITUTE  GOODS OR
 * SERVICES;  LOSS  OF USE,  DATA,  OR PROFITS;  OR  BUSINESS INTERRUPTION)
 * HOWEVER CAUSED  AND ON  ANY THEORY  OF LIABILITY,  WHETHER IN  CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY  WAY  OUT OF  THE  USE OF  THIS  SOFTWARE, EVEN  IF  ADVISED OF  THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package cppncss.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import tools.Options;
import cppast.ParseException;
import cppast.Parser;
import cppast.ParserVisitor;
import cppast.Token;
import cppncss.analyzer.preprocessor.PreProcessor;

/**
 * Builds and walks a forrest of abstract syntax trees from a set of given files.
 *
 * @author Mathieu Champlon
 */
/**
 * @author Mathieu Champlon
 */
public final class Analyzer
{
    private static final String[] DECLARATIONS =
    {
            ".h", ".hpp", ".hxx", ".h++", ".inl"
    };
    private static final String[] DEFINITIONS =
    {
            ".cpp", ".cxx", ".c++", ".c", ".cc"
    };
    private static final String[] SKIPPED =
    {
            ".svn", "CVS", "RCS", "SCCS"
    };
    private static final FilenameFilter FILES_FILTER = new FilenameFilter()
    {
        public boolean accept( final File directory, final String name )
        {
            for( String value : SKIPPED )
                if( value.equals( name ) )
                    return false;
            return true;
        }
    };
    private final boolean recursive;
    private final boolean force;
    private final String prefix;
    private final List<String> files;
    private final PreProcessor processor;
    private final ParserVisitor visitor;
    private final FileObserver observer;
    private final EventHandler handler;
    private final Parser parser;

    /**
     * Create an analyzer.
     *
     * @param options the options
     * @param visitor the abstract syntax tree visitor
     * @param observer a file observer
     * @param handler an event handler
     */
    public Analyzer( final Options options, final ParserVisitor visitor, final FileObserver observer,
            final EventHandler handler )
    {
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        if( handler == null )
            throw new IllegalArgumentException( "argument 'handler' is null" );
        if( visitor == null )
            throw new IllegalArgumentException( "argument 'visitor' is null" );
        this.recursive = options.hasOption( "r" );
        this.force = options.hasOption( "k" );
        this.prefix = getPrefix( options );
        this.processor = createProcessor( options );
        this.parser = new Parser( processor );
        this.observer = observer;
        this.handler = handler;
        this.visitor = visitor;
        this.files = sort( resolve( options.getArgList() ) );
    }

    private String getPrefix( final Options options )
    {
        final List<String> prefixes = options.getOptionPropertyValues( "p" );
        if( prefixes.size() > 0 )
            return prefixes.get( 0 );
        return "";
    }

    private PreProcessor createProcessor( final Options options )
    {
        final PreProcessor result = new PreProcessor();
        final List<String> defineNames = options.getOptionProperties( "D" );
        final List<String> defineValues = options.getOptionPropertyValues( "D" );
        for( int i = 0; i < defineNames.size(); ++i )
            result.addDefine( defineNames.get( i ), defineValues.get( i ) );
        final List<String> macroNames = options.getOptionProperties( "M" );
        final List<String> macroValues = options.getOptionPropertyValues( "M" );
        for( int i = 0; i < macroNames.size(); ++i )
            result.addMacro( macroNames.get( i ), macroValues.get( i ) );
        return result;
    }

    private List<String> resolve( final List<String> inputs )
    {
        final List<String> result = new ArrayList<String>();
        for( String input : inputs )
            resolve( result, input, true );
        return result;
    }

    private void resolve( final List<String> result, final String string, final boolean processDirectory )
    {
        final File file = new File( string );
        if( !file.isDirectory() )
        {
            if( isFrom( string, DECLARATIONS ) || isFrom( string, DEFINITIONS ) )
                result.add( string );
        }
        else if( processDirectory )
        {
            final String[] content = file.list( FILES_FILTER );
            for( int i = 0; i < content.length; ++i )
            {
                final String filename = string + File.separatorChar + content[i];
                resolve( result, filename, recursive );
            }
        }
    }

    private boolean isFrom( final String string, final String[] strings )
    {
        for( int i = 0; i < strings.length; ++i )
            if( string.toLowerCase( Locale.getDefault() ).endsWith( strings[i] ) )
                return true;
        return false;
    }

    private List<String> sort( final List<String> files )
    {
        Collections.sort( files, new Comparator<String>()
        {
            public int compare( final String lhs, final String rhs )
            {
                if( isFrom( lhs, DECLARATIONS ) && isFrom( rhs, DEFINITIONS ) )
                    return -1;
                if( isFrom( lhs, DEFINITIONS ) && isFrom( rhs, DECLARATIONS ) )
                    return 1;
                return 0;
            }
        } );
        return files;
    }

    /**
     * Run the analyzis.
     */
    public void run()
    {
        handler.started();
        final int parsed = process( visitor );
        handler.finished( parsed, files.size() );
    }

    private int process( final ParserVisitor visitor )
    {
        int parsed = 0;
        for( String filename : files )
        {
            observer.changed( filter( filename ) );
            handler.changed( filter( filename ) );
            if( process( visitor, filename ) )
                ++parsed;
            else if( !force )
                return parsed;
        }
        return parsed;
    }

    private String filter( final String filename )
    {
        if( filename.startsWith( prefix ) )
            return filename.substring( prefix.length() );
        return filename;
    }

    private boolean process( final ParserVisitor visitor, final String filename )
    {
        try
        {
            parse( visitor, filename );
            return true;
        }
        catch( ParseException exception )
        {
            final Token token = getToken( exception );
            final String message = "Parse error (line " + token.endLine + ", column " + token.endColumn + ")";
            handler.error( filename, exception, message );
            handler.display( filename, token.beginLine, token.beginColumn );
        }
        catch( Throwable throwable )
        {
            handler.error( filename, throwable, throwable.getMessage() );
        }
        return false;
    }

    private void parse( final ParserVisitor visitor, final String filename ) throws ParseException, IOException
    {
        final BufferedReader reader = new BufferedReader( new FileReader( filename ) );
        processor.reset( reader );
        parser.ReInit( processor );
        parser.translation_unit().jjtAccept( visitor, null );
        reader.close();
    }

    private Token getToken( final ParseException exception )
    {
        Token token = exception.currentToken.next;
        while( token.next != null )
            token = token.next;
        return token;
    }
}
