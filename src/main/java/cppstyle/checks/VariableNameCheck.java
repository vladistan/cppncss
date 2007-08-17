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

package cppstyle.checks;

import java.util.Properties;
import cppast.AbstractVisitor;
import cppast.AstDeclaration;
import cppast.AstFunctionBody;
import cppast.AstFunctionDeclaration;
import cppast.AstFunctionDefinition;
import cppast.AstParameterName;
import cppast.ParserConstants;
import cppast.SimpleNode;

/**
 * Checks for the validity of variable names.
 *
 * @author Mathieu Champlon
 */
public final class VariableNameCheck extends AbstractVisitor
{
    private final NameCheck check;

    /**
     * Create a variable name check.
     *
     * @param listener the check listener
     * @param properties the available properties
     */
    public VariableNameCheck( final CheckListener listener, final Properties properties )
    {
        check = new NameCheck( listener, properties, "variable" );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionBody node, final Object data )
    {
        check( node );
        return super.visit( node, data );
    }

    private void check( final SimpleNode node )
    {
        node.accept( new AbstractVisitor()
        {
            public Object visit( final AstFunctionDeclaration subnode, final Object data )
            {
                return data;
            }

            public Object visit( final AstFunctionDefinition subnode, final Object data )
            {
                return data;
            }

            public Object visit( final AstDeclaration subnode, final Object data )
            {
                if( !filter( subnode ) )
                    return data;
                return super.visit( subnode, data );
            }

            private boolean filter( final SimpleNode node )
            {
                return !node.contains( ParserConstants.TYPEDEF )
                        && (!node.contains( ParserConstants.CONST ) || !node.contains( ParserConstants.STATIC ));
            }

            public Object visit( final AstParameterName subnode, final Object data )
            {
                check.verify( subnode.getFirstToken() );
                return data;
            }
        }, null );
    }
}
