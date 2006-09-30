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

import java.util.Collection;
import java.util.Vector;

/**
 * Provides a composite for parser visitors.
 *
 * @author Mathieu Champlon
 */
public final class VisitorComposite implements ParserVisitor
{
    private final Vector<ParserVisitor> visitors = new Vector<ParserVisitor>();

    /**
     * Create a visitor composite.
     *
     * @param visitors visitors to initialy register
     */
    public VisitorComposite( final Collection< ? extends ParserVisitor> visitors )
    {
        this.visitors.addAll( visitors );
    }

    /**
     * Create a visitor composite.
     */
    public VisitorComposite()
    {
    }

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
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstTranslationUnit node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionDefinition node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionName node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDestructorDefinition node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstructorDefinition node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstCaseStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIfStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIterationStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstHandler node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionParameters node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstParameter node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstParameterType node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionParameterTypeQualifier node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionBody node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstructorInitializer node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstExpressionStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstElseStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDeclarationStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstSwitchStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstJumpStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLabelStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDefaultStatement node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConditionalExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLogicalOrExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLogicalAndExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstPrimaryExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstAssignmentExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstInclusiveOrExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstExclusiveOrExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstAndExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstEqualityExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstRelationalExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstShiftExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstAdditiveExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstMultiplicativeExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstPointerToMemberExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstCastExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstUnaryExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstPostfixExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIdExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstNewExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstDeleteExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionCallExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConstantExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstTypeIdExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstThrowExpression node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstClassDefinition node, final Object data )
    {
        for( ParserVisitor visitor : visitors )
            visitor.visit( node, data );
        return data;
    }
}
