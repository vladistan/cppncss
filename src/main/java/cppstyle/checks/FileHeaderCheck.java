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

package cppstyle.checks;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import cppast.AbstractVisitor;
import cppast.AstTranslationUnit;

/**
 * Checks for the validity of file headers.
 *
 * @author Mathieu Champlon
 */
public final class FileHeaderCheck extends AbstractVisitor
{
    private final CheckListener listener;
    private final String[] expected;

    /**
     * Create a file header check.
     *
     * @param listener
     * @param properties
     * @param folder
     * @throws IOException
     */
    public FileHeaderCheck( final CheckListener listener, final Properties properties, final File folder ) throws IOException
    {
        if( listener == null)
            throw new IllegalArgumentException( "argument 'listener' is null" );
        this.listener = listener;
        this.expected = split( trim( getExpected( properties, folder ) ) );
    }

    private String[] split( final String string )
    {
        return string.split( "[\n\r]" );
    }

    private String trim( final String string )
    {
        if( string.length() == 0 )
            return string;
        final char last = string.charAt( string.length() - 1 );
        if( last == '\n' || last == '\r' )
            return trim( string.substring( 0, string.length() - 1 ) );
        return string;
    }

    private String getExpected( final Properties properties, final File folder ) throws IOException
    {
        final String content = properties.getProperty( "header" );
        if( content != null )
            return content;
        final String filename = properties.getProperty( "headerFile" );
        if( filename != null )
            return readFile( filename, folder );
        throw new IllegalArgumentException( "missing property 'headerFile' or 'header'" );
    }

    public String readFile( final String filename, final File folder ) throws IOException
    {
        final FileInputStream stream = new FileInputStream( folder + File.separator + filename );
        try
        {
            final byte buffer[] = new byte[stream.available()];
            stream.read( buffer );
            return new String( buffer );
        }
        finally
        {
            stream.close();
        }
    }

    public Object visit( final AstTranslationUnit node, final Object data )
    {
        final String[] actual = split( node.getComment() );
        if( expected.length != actual.length )
            listener.fail( "file header line count invalid" );
        for( int line = 0; line < actual.length; ++line )
            if( !actual[line].equals( expected[line] ) )
                listener.fail( "file header mismatch line " + (line + 1) );
        return data;
    }
}
