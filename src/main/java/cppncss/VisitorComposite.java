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

import java.util.Iterator;
import java.util.Vector;
import cppast.AstAssignmentExpression;
import cppast.AstCaseStatement;
import cppast.AstConstructorDefinition;
import cppast.AstDestructorDefinition;
import cppast.AstFunctionDefinition;
import cppast.AstFunctionName;
import cppast.AstHandler;
import cppast.AstIfStatement;
import cppast.AstIterationStatement;
import cppast.AstTranslationUnit;
import cppast.ParserVisitor;
import cppast.SimpleNode;

/**
 * Provides a composite of visitors.
 *
 * @author Mathieu Champlon
 */
public final class VisitorComposite implements ParserVisitor
{
    private final Vector<ParserVisitor> visitors = new Vector<ParserVisitor>();

    /**
     * Add a visitor.
     *
     * @param visitor the visitor
     */
    public void register( final ParserVisitor visitor )
    {
        visitors.add( visitor );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final SimpleNode node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstTranslationUnit node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionDefinition node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionName node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDestructorDefinition node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstructorDefinition node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstCaseStatement node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIfStatement node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIterationStatement node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstHandler node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstAssignmentExpression node, final Object data )
    {
        final Iterator<ParserVisitor> iterator = visitors.iterator();
        while( iterator.hasNext() )
            iterator.next().visit( node, data );
        return data;
    }
}
