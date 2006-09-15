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

import java.util.Iterator;
import java.util.Vector;

/**
 * Provides a custom simple node JavaCC implementation.
 *
 * @author Mathieu Champlon
 */
public abstract class SimpleNode implements Node
{
    private Node parent;
    private final Vector<Node> children = new Vector<Node>();
    private final int id;
    private Parser parser;
    private Token first, last;
    private Scope scope;

    public SimpleNode( final int i )
    {
        id = i;
    }

    public SimpleNode( final Parser p, final int i )
    {
        this( i );
        parser = p;
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtOpen()
    {
        first = parser.getToken( 1 );
        scope = parser.getCurrentScope();
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtClose()
    {
        last = parser.getToken( 0 );
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
     * Retrive the node last token.
     *
     * @return a token
     */
    public final Token getLastToken()
    {
        return last;
    }

    /**
     * {@inheritDoc}
     */
    public final void jjtSetParent( final Node n )
    {
        parent = n;
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
    public final void jjtAddChild( final Node node, final int i )
    {
        children.add( node );
    }

    /**
     * {@inheritDoc}
     */
    public final Node jjtGetChild( final int i )
    {
        return children.elementAt( i );
    }

    /**
     * {@inheritDoc}
     */
    public final int jjtGetNumChildren()
    {
        return children.size();
    }

    /**
     * Visit the children of the node.
     *
     * @param visitor the visitor
     * @param data the custom data
     * @return a custom result
     */
    public final Object childrenAccept( final ParserVisitor visitor, final Object data )
    {
        final Iterator<Node> iterator = children.iterator();
        while( iterator.hasNext() )
            iterator.next().jjtAccept( visitor, data );
        return data; // FIXME it looks like return data is only set by root node ?!
    }

    /*
     * You can override these two methods in subclasses of SimpleNode to customize the way the node appears when the
     * tree is dumped. If your output uses more than one line you should override toString(String), otherwise overriding
     * toString() is probably all you need to do.
     */
    public String toString()
    {
        return ParserTreeConstants.jjtNodeName[id];
    }

    public String toString( final String prefix )
    {
        return prefix + toString();
    }

    /*
     * Override this method if you want to customize how the node dumps out its children.
     */
    public void dump( final String prefix )
    {
        System.out.println( toString( prefix ) );
        final Iterator<Node> iterator = children.iterator();
        while( iterator.hasNext() )
            ((SimpleNode)iterator.next()).dump( prefix + " " );
    }
}
