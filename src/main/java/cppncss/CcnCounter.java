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
 *
 * $Id: $
 */

package cppncss;

import cppast.ASTassignment_expression;
import cppast.ASTcompound_statement;
import cppast.ASThandler;
import cppast.ASTiteration_statement;
import cppast.ASTlabeled_statement;
import cppast.ASTselection_statement;
import cppast.Parser;
import cppast.SimpleNode;
import cppast.Token;

/**
 * @author Mathieu Champlon
 */
public class CcnCounter extends Visitor
{
    private int count_ = 0;

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTcompound_statement node, final Object data )
    {
        if( count_ == 0 )
            ++count_;
        return super.visit( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTselection_statement node, final Object data )
    {
        if( node.getFirstToken().image == "if" )
            ++count_;
        return super.visit( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTiteration_statement node, final Object data )
    {
        ++count_;
        return super.visit( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTlabeled_statement node, final Object data )
    {
        if( node.getFirstToken().image == "case" )
            ++count_;
        return super.visit( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASThandler node, final Object data )
    {
        ++count_;
        return super.visit( node, data );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final ASTassignment_expression node, final Object data )
    {
        count_ += count( node, Parser.AND );
        count_ += count( node, Parser.OR );
        count_ += count( node, Parser.QUESTIONMARK );
        return data;
    }

    private int count( final SimpleNode node, final int kind )
    {
        int result = 0;
        for( Token token = node.getFirstToken(); token != node.getLastToken().next; token = token.next )
            if( token.kind == kind )
                ++result;
        return result;
    }

    public final int getCount()
    {
        return count_;
    }
}
