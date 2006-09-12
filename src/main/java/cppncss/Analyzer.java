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
 *
 * $Id: $
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
import cppast.Token;

/**
 * @author Mathieu Champlon
 */
public class Analyzer
{
    private final boolean debug;
    private final boolean verbose;
    private final boolean recursive;
    private final boolean force;
    private final List<String> files;
    private final PreProcessor processor;
    private Parser parser;
    private static final String[] declarations =
    {
            ".h", ".hpp"
    };
    private static final String[] definitions =
    {
            ".cpp", ".cxx", ".inl"
    };
    private static final String[] skip =
    {
            ".svn", "CVS"
    };

    public Analyzer( final String[] args )
    {
        final Options options = new Options( args );
        debug = options.hasOption( "d" );
        verbose = debug || options.hasOption( "v" );
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
            if( isFrom( string, declarations ) || isFrom( string, definitions ) )
                result.add( string );
        }
        else if( processDirectory )
        {
            final String[] content = file.list( new FilenameFilter()
            {
                public boolean accept( final File dir, final String name )
                {
                    return !isFrom( name, skip );
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
                if( isFrom( lhs, declarations ) && isFrom( rhs, definitions ) )
                    return -1;
                if( isFrom( lhs, definitions ) && isFrom( rhs, declarations ) )
                    return 1;
                return 0;
            }
        } );
        return files;
    }

    public void parse( final Visitor visitor ) throws IOException
    {
        final long start = System.currentTimeMillis();
        final int parsed = process( visitor );
        final long end = System.currentTimeMillis();
        final double time = (end - start) / 1000.0;
        System.out.println( "Successfully parsed " + parsed + " / " + files.size() + " files in " + time + " s" );
    }

    private int process( final Visitor visitor ) throws IOException
    {
        final Iterator<String> iterator = files.iterator();
        int parsed = 0;
        while( iterator.hasNext() )
        {
            final String filename = iterator.next();
            if( debug )
                System.out.println( "Parsing " + filename );
            if( process( visitor, filename ) )
                ++parsed;
            else if( !force )
                return parsed;
        }
        return parsed;
    }

    private boolean process( final Visitor visitor, final String filename ) throws IOException
    {
        try
        {
            parse( visitor, filename );
            return true;
        }
        catch( FileNotFoundException exception )
        {
            error( filename, exception, "File not found" );
        }
        catch( ParseException exception )
        {
            final String message = "Parse error (line " + getToken( exception ).endLine + ", column "
                    + getToken( exception ).endColumn + ")";
            error( filename, exception, message );
            if( verbose )
                display( exception, filename );
        }
        catch( Throwable throwable )
        {
            error( filename, throwable, throwable.getMessage() );
        }
        return false;
    }

    private void parse( final Visitor visitor, final String filename ) throws ParseException, IOException
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
        final byte content[] = new byte[stream.available()];
        stream.read( content );
        return new String( content );
    }

    private void error( final String filename, final Throwable throwable, final String reason )
    {
        if( debug )
            throwable.printStackTrace();
        if( verbose )
            System.out.println( "Skipping " + filename + " : " + reason );
    }

    private void display( final ParseException exception, final String filename ) throws IOException
    {
        final int start = getToken( exception ).beginLine;
        displayLocation( start, 3, filename );
        displayCursor( getToken( exception ).beginColumn );
    }

    private Token getToken( final ParseException exception )
    {
        Token token = exception.currentToken.next;
        while( token.next != null )
            token = token.next;
        return token;
    }

    private void displayCursor( final int column )
    {
        for( int i = 0; i < column - 1; ++i )
            System.out.print( ' ' );
        System.out.println( '^' );
    }

    private void displayLocation( final int start, final int lines, final String filename ) throws IOException
    {
        final BufferedReader reader = new BufferedReader( open( filename ) );
        for( int i = 0; i < start - lines; i++ )
            reader.readLine();
        for( int i = 0; i < lines; ++i )
            System.out.println( reader.readLine() );
    }
}
