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

package cppast;

/**
 * Provides a custom base node implementation.
 *
 * @author Mathieu Champlon
 */
public abstract class SimpleNode implements Node
{
    private Node parent;
    private Node[] children = new Node[0];
    private final String name;
    private Token first, last;
    private Scope scope;

    /**
     * Create a node.
     *
     * @param id the node identifier
     */
    public SimpleNode( final int id )
    {
        this.name = ParserTreeConstants.jjtNodeName[id];
    }

    /**
     * Create a node.
     *
     * @param parser the parser
     * @param id the node identifier
     */
    public SimpleNode( final Parser parser, final int id )
    {
        this( id );
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtOpen()
    {
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtClose()
    {
    }

    /**
     * Open the node scope.
     *
     * @param token the first token of the node
     * @param scope the scope of the node
     */
    public final void openScope( final Token token, final Scope scope )
    {
        this.first = token;
        this.scope = scope;
    }

    /**
     * Close the node scope.
     *
     * @param token the last token of the node
     */
    public final void closeScope( final Token token )
    {
        last = token;
    }

    /**
     * Resolve a symbol to a fully scoped name.
     *
     * @param name the symbol
     * @return a fully scoped symbol name
     */
    public final String resolve( final String name )
    {
        return scope.resolve( name );
    }

    /**
     * Retrieve the node first token.
     *
     * @return a token
     */
    public final Token getFirstToken()
    {
        return first;
    }

    /**
     * Retrieve the node last token.
     *
     * @return a token
     */
    public final Token getLastToken()
    {
        return last;
    }

    /**
     * Retrieve the comment attached to the node.
     *
     * @return the comment or null if none
     */
    public final String getComment()
    {
        Token token = first.specialToken;
        if( token == null )
            return null;
        while( token.specialToken != null )
            token = token.specialToken;
        while( token != null )
        {
            if( token.kind == Parser.C_STYLE_COMMENT || token.kind == Parser.CPP_STYLE_COMMENT )
                return token.image;
            token = token.next;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtSetParent( final Node node )
    {
        parent = node;
    }

    /**
     * {@inheritDoc}
     */
    public final Node jjtGetParent()
    {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtAddChild( final Node node, final int index )
    {
        if( index >= children.length )
        {
            final Node[] c = new Node[index + 1];
            System.arraycopy( children, 0, c, 0, children.length );
            children = c;
        }
        children[index] = node;
    }

    /**
     * {@inheritDoc}
     */
    public final Node jjtGetChild( final int index )
    {
        return children[index];
    }

    /**
     * {@inheritDoc}
     */
    public final int jjtGetNumChildren()
    {
        return children.length;
    }

    /**
     * Visit the children of the node.
     *
     * @param visitor the visitor
     * @param data the custom data
     * @return a custom result
     */
    public final Object accept( final ParserVisitor visitor, final Object data )
    {
        Object result = data;
        for( int index = 0; index < children.length; ++index )
            result = children[index].jjtAccept( visitor, result );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public final String toString()
    {
        return name;
    }

    /**
     * Check whether a token is part of the node or not.
     *
     * @param kind the token kind
     * @return true if a token of the given kind is found in the node, false otherwise
     */
    public final boolean contains( final int kind )
    {
        for( Token token = first; token != last.next; token = token.next )
            if( token.kind == kind )
                return true;
        return false;
    }

    /**
     * Retrieve the parent.
     *
     * @return the node parent
     */
    public final SimpleNode getParent()
    {
        return (SimpleNode)parent;
    }
}
