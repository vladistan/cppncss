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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import cppast.JavaCharStream;
import cppast.ParserTokenManager;
import cppast.Token;

/**
 * Captures filter common behaviours.
 *
 * @author Mathieu Champlon
 */
public abstract class AbstractTokenFilter implements TokenFilter
{
    private final Stack<Token> buffer;
    private final List<Token> tokens;
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
        this.name = check( name );
        this.tokens = parse( value );
    }

    private String check( final String name )
    {
        if( name.length() == 0 )
            throw new IllegalArgumentException( "Empty macro/define name specified" );
        final ParserTokenManager manager = new ParserTokenManager( new JavaCharStream( new StringReader( name ) ) );
        final Token token = manager.getNextToken();
        if( token.kind != ParserTokenManager.ID )
            throw new IllegalArgumentException( "Illegal macro/define name : " + "'" + name + "'" );
        final Token next = manager.getNextToken();
        if( next.kind != ParserTokenManager.EOF )
            throw new IllegalArgumentException( "Illegal macro/define name : " + "'" + name + "'" );
        return name;
    }

    private List<Token> parse( final String value )
    {
        final List<Token> result = new ArrayList<Token>();
        final ParserTokenManager manager = new ParserTokenManager( new JavaCharStream( new StringReader( value ) ) );
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
     *
     * @param location the location of the insertion
     */
    protected final void insert( final Token location )
    {
        for( Token token : tokens )
            buffer.push( copy( token, location ) );
    }

    private Token copy( final Token token, final Token location )
    {
        final Token result = Token.newToken( token.kind );
        result.kind = token.kind;
        result.beginColumn = location.beginColumn;
        result.beginLine = location.beginLine;
        result.endColumn = location.endColumn;
        result.endLine = location.endLine;
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
