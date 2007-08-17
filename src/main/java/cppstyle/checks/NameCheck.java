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
import cppast.Token;

/**
 * Provides a means to check the name of a given token against a regular expression.
 *
 * @author Mathieu Champlon
 */
class NameCheck
{
    private final CheckListener listener;
    private final String format;
    private final String type;

    /**
     * Create a name check.
     *
     * @param listener the check listener
     * @param properties the properties
     * @param type the name of the check
     */
    public NameCheck( final CheckListener listener, final Properties properties, final String type )
    {
        if( listener == null )
            throw new IllegalArgumentException( "argument 'listener' is null" );
        if( type == null )
            throw new IllegalArgumentException( "argument 'type' is null" );
        this.listener = listener;
        this.format = properties.getProperty( "format" );
        if( format == null )
            throw new IllegalArgumentException( "missing property 'format'" );
        this.type = type;
    }

    /**
     * Verify a given token.
     *
     * @param token the token to verify
     */
    public void verify( final Token token )
    {
        if( !token.image.matches( format ) )
            listener.fail( "invalid " + type + " name", token.beginLine );
    }
}
