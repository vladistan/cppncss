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

import java.io.StringReader;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import cppast.ParserTokenManager;
import cppast.SimpleCharStream;
import cppast.Token;

/**
 * Captures filter common behaviours.
 *
 * @author Mathieu Champlon
 */
public abstract class AbstractTokenFilter implements TokenFilter
{
    private final Stack<Token> buffer;
    private final Vector<Token> tokens;
    private final String name;

    /**
     * Create an abstract token filter.
     *
     * @param buffer the token stack where to output filtered tokens
     * @param name the name of the filter
     * @param value the raw value of the filtering result
     */
    public AbstractTokenFilter( final Stack<Token> buffer, final String name, final String value )
    {
        if( buffer == null )
            throw new IllegalArgumentException( "parameter 'buffer' is null" );
        if( name == null )
            throw new IllegalArgumentException( "parameter 'name' is null" );
        this.buffer = buffer;
        this.name = name;
        this.tokens = parse( value );
    }

    private Vector<Token> parse( final String value )
    {
        final Vector<Token> result = new Vector<Token>();
        final ParserTokenManager manager = new ParserTokenManager( new SimpleCharStream( new StringReader( value ) ) );
        Token token = manager.getNextToken();
        while( token.kind != ParserTokenManager.EOF )
        {
            result.add( 0, token );
            token = manager.getNextToken();
        }
        return result;
    }

    /**
     * Put both tokens back into the flow.
     *
     * @param token the head of the flow
     * @param next the token following the head
     */
    protected final void undo( final Token token, final Token next )
    {
        buffer.push( next );
        buffer.push( token );
    }

    /**
     * Insert filtered tokens into the token flow.
     */
    protected final void insert()
    {
        final Iterator<Token> iterator = tokens.iterator();
        while( iterator.hasNext() )
            buffer.push( copy( iterator.next() ) );
    }

    private Token copy( final Token token )
    {
        final Token result = Token.newToken( token.kind );
        result.kind = token.kind;
        // FIXME lines and columns should point to real input
        // result.beginColumn = token.beginColumn;
        // result.beginLine = token.beginLine;
        // result.endColumn = token.endColumn;
        // result.endLine = token.endLine;
        result.image = token.image;
        result.next = token.next;
        result.specialToken = token.specialToken;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean matches( final String name )
    {
        return this.name.equals( name );
    }
}
