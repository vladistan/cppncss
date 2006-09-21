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

import java.util.Stack;
import cppast.ParserTokenManager;
import cppast.Token;

/**
 * Manages macro pre-processing.
 *
 * @author Mathieu Champlon
 */
public final class Macro extends AbstractTokenFilter
{
    private final TokenProvider provider;

    /**
     * Create a macro.
     *
     * @param provider the token provider to retrieve subsequent tokens
     * @param buffer the token stack where to output filtered tokens
     * @param name the define symbol
     * @param value the define value
     */
    public Macro( final TokenProvider provider, final Stack<Token> buffer, final String name, final String value )
    {
        super( buffer, name, value );
        if( provider == null )
            throw new IllegalArgumentException( "parameter 'provider' is null" );
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    public boolean process( final Token token )
    {
        if( matches( token.image ) )
        {
            final Token next = provider.next();
            if( next.kind == ParserTokenManager.LPARENTHESIS )
                replace( token );
            else
                undo( token, next );
            return true;
        }
        return false;
    }

    private void replace( final Token token )
    {
        erase();
        insert( token );
    }

    private void erase()
    {
        Token token = null;
        int level = 1;
        do
        {
            token = provider.next();
            if( token.kind == ParserTokenManager.LPARENTHESIS )
                ++level;
            if( token.kind == ParserTokenManager.RPARENTHESIS )
                --level;
        }
        while( level != 0 || token.kind != ParserTokenManager.RPARENTHESIS );
    }
}
