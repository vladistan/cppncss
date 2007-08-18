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

import java.util.Properties;

/**
 * Checks that files end with a new line.
 *
 * @author Mathieu Champlon
 */
public final class EndOfLineCheck implements FileContentObserver
{
    private final CheckListener listener;
    private final String type;

    /**
     * Create a new line at end of file check.
     *
     * @param listener the check listener
     * @param properties the available properties
     */
    public EndOfLineCheck( final CheckListener listener, final Properties properties )
    {
        if( listener == null )
            throw new IllegalArgumentException( "argument 'listener' is null" );
        this.listener = listener;
        this.type = getType( properties );
    }

    private String getType( final Properties properties )
    {
        final String type = properties.getProperty( "type" );
        if( type == null )
            return null;
        if( type.equals( "cr" ) )
            return "\r";
        if( type.equals( "lf" ) )
            return "\n";
        if( type.equals( "crlf" ) )
            return "\r\n";
        throw new IllegalArgumentException( "invalid 'type' property, must be one of 'cr', 'lf' or 'crlf'" );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String content )
    {
        int number = 0;
        for( final String line : split( content ) )
        {
            for( int index = next( line ); index != -1; index = next( line, index + 1 ) )
                listener.fail( "invalid end of line", ++number );
            ++number;
        }
    }

    private String[] split( final String content )
    {
        if( type != null )
            return content.split( type );
        return content.split( detect( content ) );
    }

    private String detect( final String content )
    {
        if( content.indexOf( "\r\n" ) != -1 )
            return "\r\n";
        if( content.indexOf( "\r" ) != -1 )
            return "\r";
        return "\n";
    }

    private int next( final String line )
    {
        return next( line, 0 );
    }

    private int next( final String line, final int start )
    {
        final int index = line.indexOf( '\r', start );
        if( index != -1 )
            return index;
        return line.indexOf( '\n', start );
    }
}
