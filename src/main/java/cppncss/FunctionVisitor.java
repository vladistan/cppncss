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

import cppast.AbstractVisitor;
import cppast.AstConstructorDefinition;
import cppast.AstDestructorDefinition;
import cppast.AstFunctionDefinition;
import cppast.SimpleNode;
import cppast.Visitor;
import cppast.VisitorAdapter;

/**
 * Implements a visitor for functions.
 *
 * @author Mathieu Champlon
 */
public class FunctionVisitor extends AbstractVisitor
{
    private final Counter counter;

    /**
     * Create a function visitor.
     *
     * @param counter the counter to apply
     */
    public FunctionVisitor( final Counter counter )
    {
        this.counter = counter;
    }

    /**
     * {@inheritDoc}
     */
    public final void visit( final AstFunctionDefinition node )
    {
        process( node );
    }

    /**
     * {@inheritDoc}
     */
    public final void visit( final AstConstructorDefinition node )
    {
        process( node );
    }

    /**
     * {@inheritDoc}
     */
    public final void visit( final AstDestructorDefinition node )
    {
        process( node );
    }

    private Object process( final SimpleNode node )
    {
        final Object result = node.childrenAccept( new VisitorAdapter( counter ), null );
        counter.flush( getFunctionName( node ) );
        return result;
    }

    private String getFunctionName( final SimpleNode node )
    {
        final Visitor visitor = new FunctionNameExtractor();
        node.childrenAccept( new VisitorAdapter( visitor ), null );
        return visitor.toString();
    }
}
