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

import cppast.AstCaseStatement;
import cppast.AstClassDefinition;
import cppast.AstConditionalExpression;
import cppast.AstFunctionBody;
import cppast.AstCatchBlock;
import cppast.AstIfStatement;
import cppast.AstIterationStatement;
import cppast.AstLogicalAndExpression;
import cppast.AstLogicalOrExpression;

/**
 * Implements a CCN counter.
 *
 * @author Mathieu Champlon
 */
public final class CcnCounter extends AbstractCounter
{
    /**
     * Create a CCN counter.
     *
     * @param observer a counter observer
     */
    public CcnCounter( final CounterObserver observer )
    {
        super( "CCN", observer );
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
    public Object visit( final AstIfStatement node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstIterationStatement node, final Object data )
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
    public Object visit( final AstCatchBlock node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLogicalAndExpression node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstLogicalOrExpression node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstConditionalExpression node, final Object data )
    {
        increment();
        return node.accept( this, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstClassDefinition node, final Object data )
    {
        return data;
    }
}
