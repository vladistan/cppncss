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

package cpptools;

import cppast.JavaCharStream;
import cppast.ParserTokenManager;
import cppast.Token;
import cpptools.preprocessor.TokenProvider;

/**
 * Adapts a token provider to a token manager.
 *
 * @author Mathieu Champlon
 */
public final class TokenManagerAdapter extends ParserTokenManager
{
    private final TokenProvider provider;

    /**
     * Create a token manager adapter.
     *
     * @param provider the token provider to adapt
     */
    public TokenManagerAdapter( final TokenProvider provider )
    {
        super( null );
        if( provider == null )
            throw new IllegalArgumentException( "parameter 'provider' is null" );
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    public void ReInit( final JavaCharStream stream )
    {
        provider.reset( stream );
    }

    /**
     * {@inheritDoc}
     */
    public Token getNextToken()
    {
        return provider.next();
    }
}
