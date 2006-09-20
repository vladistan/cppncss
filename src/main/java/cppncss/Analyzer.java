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

package cppncss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import cppast.ParseException;
import cppast.Parser;
import cppast.ParserTokenManager;
import cppast.ParserVisitor;
import cppast.Token;

/**
 * @author Mathieu Champlon
 */
public final class Analyzer
{
    private static final String[] DECLARATIONS =
    {
            ".h", ".hpp"
    };
    private static final String[] DEFINITIONS =
    {
            ".cpp", ".cxx", ".inl"
    };
    private static final String[] SKIPPED =
    {
            ".svn", "CVS"
    };
    private final boolean recursive;
    private final boolean force;
    private final List<String> files;
    private final PreProcessor processor;
    private final FileObserver observer;
    private final EventHandler handler;
    private Parser parser;

    /**
     * Create an analyzer.
     *
     * @param options the options
     * @param observer a file observer
     * @param handler an event handler
     */
    public Analyzer( final Options options, final FileObserver observer, final EventHandler handler )
    {
        if( options == null )
            throw new IllegalArgumentException( "argument 'options' is null" );
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        if( handler == null )
            throw new IllegalArgumentException( "argument 'handler' is null" );
        recursive = options.hasOption( "r" );
        force = options.hasOption( "f" );
        processor = new PreProcessor();
        final List<String> names = options.getOptionProperties( "D" );
        final List<String> values = options.getOptionPropertyValues( "D" );
        for( int i = 0; i < names.size(); ++i )
        {
            processor.addMacro( names.get( i ), values.get( i ) );
            processor.addDefine( names.get( i ), values.get( i ) );
        }
        this.observer = observer;
        this.handler = handler;
        files = sort( resolve( options.getArgList() ) );
    }

    private List<String> resolve( final List<String> inputs )
    {
        final Vector<String> result = new Vector<String>();
        final Iterator<String> iterator = inputs.iterator();
        while( iterator.hasNext() )
            resolve( result, iterator.next(), true );
        return result;
    }

    private void resolve( final Vector<String> result, final String string, final boolean processDirectory )
    {
        final File file = new File( string );
        if( !file.isDirectory() )
        {
            if( isFrom( string, DECLARATIONS ) || isFrom( string, DEFINITIONS ) )
                result.add( string );
        }
        else if( processDirectory )
        {
            final String[] content = file.list( new FilenameFilter()
            {
                public boolean accept( final File dir, final String name )
                {
                    return !isFrom( name, SKIPPED );
                }
            } );
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
            if( string.endsWith( strings[i] ) )
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
     * Parse the files and visit the abstract syntax trees.
     * <p>
     * Because of memory consumption the trees cannot be cached therefore this method must probably be called only once.
     *
     * @param visitor the visitor
     * @throws IOException an exception occurs reading a file
     */
    public void accept( final ParserVisitor visitor ) throws IOException
    {
        handler.started();
        final int parsed = process( visitor );
        handler.finished( parsed, files.size() );
    }

    private int process( final ParserVisitor visitor ) throws IOException
    {
        final Iterator<String> iterator = files.iterator();
        int parsed = 0;
        while( iterator.hasNext() )
        {
            final String filename = iterator.next();
            observer.changed( filename );
            handler.changed( filename );
            if( process( visitor, filename ) )
                ++parsed;
            else if( !force )
                return parsed;
        }
        return parsed;
    }

    private boolean process( final ParserVisitor visitor, final String filename ) throws IOException
    {
        try
        {
            parse( visitor, filename );
            return true;
        }
        catch( FileNotFoundException exception )
        {
            handler.error( filename, exception, "File not found" );
        }
        catch( ParseException exception )
        {
            final Token token = getToken( exception );
            final String message = "Parse error (line " + token.endLine + ", column " + token.endColumn + ")";
            handler.error( filename, exception, message );
            handler.display( new BufferedReader( open( filename ) ), token.beginLine, token.beginColumn );
        }
        catch( Throwable throwable )
        {
            handler.error( filename, throwable, throwable.getMessage() );
        }
        return false;
    }

    private Token getToken( final ParseException exception )
    {
        Token token = exception.currentToken.next;
        while( token.next != null )
            token = token.next;
        return token;
    }

    private void parse( final ParserVisitor visitor, final String filename ) throws ParseException, IOException
    {
        final ParserTokenManager manager = new TokenManagerAdapter( open( filename ) );
        if( parser == null )
            parser = new Parser( manager );
        else
            parser.ReInit( manager );
        parser.translation_unit().jjtAccept( visitor, null );
    }

    private Reader open( final String filename ) throws IOException
    {
        return new StringReader( processor.filter( read( filename ) ) );
    }

    private String read( final String filename ) throws IOException
    {
        final FileInputStream stream = new FileInputStream( filename );
        final byte[] content = new byte[stream.available()];
        final int read = stream.read( content );
        if( read != content.length )
            throw new IOException( "error reading content of file '" + filename + "' : could only read " + read
                    + " bytes out of " + content.length + " available" );
        return new String( content );
    }
}
