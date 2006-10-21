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

package cppncss.counter;

import cppast.AstBreakStatement;
import cppast.AstCaseStatement;
import cppast.AstConstructorDeclaration;
import cppast.AstConstructorInitializer;
import cppast.AstContinueStatement;
import cppast.AstDeclaration;
import cppast.AstDeclarationStatement;
import cppast.AstDefaultStatement;
import cppast.AstDestructorDeclaration;
import cppast.AstElseStatement;
import cppast.AstExpressionStatement;
import cppast.AstFunctionBody;
import cppast.AstFunctionDeclaration;
import cppast.AstGotoStatement;
import cppast.AstCatchBlock;
import cppast.AstIfStatement;
import cppast.AstIterationStatement;
import cppast.AstLabelStatement;
import cppast.AstMemberDeclaration;
import cppast.AstNamespaceDefinition;
import cppast.AstReturnStatement;
import cppast.AstSwitchStatement;

/**
 * Implements a NCSS counter.
 *
 * @author Mathieu Champlon
 */
public final class NcssCounter extends AbstractCounter
{
    /**
     * Create a NCSS counter.
     *
     * @param observer a counter observer
     */
    public NcssCounter( final CounterObserver observer )
    {
        super( "NCSS", observer );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionBody node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstructorInitializer node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDeclarationStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstExpressionStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIfStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstElseStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIterationStatement node, final Object data ) // FIXME homogeneity
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstSwitchStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstGotoStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstBreakStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstContinueStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstReturnStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLabelStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstCaseStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDefaultStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstCatchBlock node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstNamespaceDefinition node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDeclaration node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstMemberDeclaration node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionDeclaration node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstructorDeclaration node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDestructorDeclaration node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }
}
