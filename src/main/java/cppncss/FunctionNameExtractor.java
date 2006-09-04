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

import cppast.ASTctor_declarator;
import cppast.ASTdtor_declarator;
import cppast.ASTfunction_direct_declarator;
import cppast.ASToperator_id;
import cppast.SimpleNode;
import cppast.Token;

/**
 * @author Mathieu Champlon
 */
public class FunctionNameExtractor extends Visitor
{
    private String name_;

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTfunction_direct_declarator node, final Object data )
    {
        return process( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTctor_declarator node, final Object data )
    {
        return process( node, data );
    }

    private Object process( final SimpleNode node, final Object data )
    {
        final Visitor visitor = new NameExtractor();
        node.childrenAccept( visitor, data );
        name_ = visitor.toString();
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASToperator_id node, final Object data )
    {
        final Visitor visitor = new NameExtractor();
        node.jjtAccept( visitor, data );
        name_ = visitor.toString();
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTdtor_declarator node, final Object data )
    {
        final StringBuffer buffer = new StringBuffer();
        for( Token token = node.getFirstToken(); token.next != node.getLastToken(); token = token.next )
            buffer.append( token.image );
        name_ = node.resolve( buffer.toString() );
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return name_;
    }
}
