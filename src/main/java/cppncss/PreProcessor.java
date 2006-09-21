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

import java.io.Reader;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import cppast.ParserTokenManager;
import cppast.SimpleCharStream;
import cppast.Token;

/**
 * Adapts the two token management systems.
 *
 * @author Mathieu Champlon
 */
public final class PreProcessor extends ParserTokenManager implements TokenProvider
{
    private final Vector<TokenFilter> filters = new Vector<TokenFilter>();
    private final Stack<Token> buffer = new Stack<Token>();

    /**
     * Create an adapter.
     */
    public PreProcessor()
    {
        super( null );
    }

    /**
     * Set a new input.
     *
     * @param reader the input reader
     */
    public void reset( final Reader reader )
    {
        buffer.clear();
        ReInit( new SimpleCharStream( reader ) );
    }

    /**
     * {@inheritDoc}
     */
    public Token next()
    {
        return super.getNextToken();
    }

    /**
     * {@inheritDoc}
     */
    public Token getNextToken()
    {
        if( !buffer.empty() )
            return buffer.pop();
        final Token token = next();
        if( filter( token ) )
            return getNextToken();
        return token;
    }

    private boolean filter( final Token token )
    {
        final Iterator<TokenFilter> iterator = filters.iterator();
        while( iterator.hasNext() )
            if( iterator.next().process( token ) )
                return true;
        return false;
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
        register( name, new Macro( this, buffer, name, value ) );
    }

    private void register( final String name, final TokenFilter macro )
    {
        final Iterator<TokenFilter> iterator = filters.iterator();
        while( iterator.hasNext() )
        {
            if( iterator.next().matches( name ) )
                throw new RuntimeException( "macro redefinition '" + name + "'" );
        }
        filters.add( macro );
    }
}
