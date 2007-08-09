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

package cpptools.preprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import cppast.JavaCharStream;
import cppast.Token;

/**
 * Adapts the two token management systems.
 *
 * @author Mathieu Champlon
 */
public final class PreProcessor implements TokenProvider
{
    private final List<TokenFilter> filters = new ArrayList<TokenFilter>();
    private final Stack<Token> buffer = new Stack<Token>();
    private final TokenProvider provider;

    /**
     * Create an adapter.
     *
     * @param provider a token provider
     */
    public PreProcessor( final TokenProvider provider )
    {
        if( provider == null )
            throw new IllegalArgumentException( "parameter 'provider' is null" );
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    public void reset( final JavaCharStream stream )
    {
        buffer.clear();
        provider.reset( stream );
    }

    /**
     * {@inheritDoc}
     */
    public Token next()
    {
        if( !buffer.empty() )
            return buffer.pop();
        final Token token = provider.next();
        if( filter( token ) )
            return attach( next(), token.specialToken );
        return token;
    }

    private boolean filter( final Token token )
    {
        for( TokenFilter filter : filters )
            if( filter.process( token ) )
                return true;
        return false;
    }

    private Token attach( final Token token, final Token specialToken )
    {
        findEnd( token ).specialToken = specialToken;
        return token;
    }

    private Token findEnd( final Token token )
    {
        Token end = token;
        while( end.specialToken != null )
            end = end.specialToken;
        return end;
    }

    /**
     * Add a define.
     *
     * @param name the name of the symbol
     * @param value the value of the symbol
     */
    public void addDefine( final String name, final String value )
    {
        register( name, new Define( buffer, name, value ) );
    }

    /**
     * Add a macro.
     *
     * @param name the name of the symbol
     * @param value the value of the symbol
     */
    public void addMacro( final String name, final String value )
    {
        register( name, new Macro( provider, buffer, name, value ) );
    }

    private void register( final String name, final TokenFilter macro )
    {
        for( TokenFilter filter : filters )
            if( filter.matches( name ) )
                throw new RuntimeException( "macro redefinition '" + name + "'" );
        filters.add( macro );
    }
}
