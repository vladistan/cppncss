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
import cppast.AstMemberDeclaration;
import cppast.AstParameterName;
import cppast.ParserConstants;
import cppast.SimpleNode;
import cppast.Token;

/**
 * Checks for the validity of member (e.g. field) names.
 *
 * @author Mathieu Champlon
 */
public final class MemberNameCheck extends AbstractVisitor
{
    private final CheckListener listener;
    private final String format;

    /**
     * Create a member name check.
     *
     * @param listener the check listener
     * @param properties the available properties
     */
    public MemberNameCheck( final CheckListener listener, final Properties properties )
    {
        if( listener == null )
            throw new IllegalArgumentException( "argument 'listener' is null" );
        this.listener = listener;
        this.format = properties.getProperty( "format" );
        if( format == null )
            throw new IllegalArgumentException( "missing property 'format'" );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstMemberDeclaration node, final Object data )
    {
        if( !node.contains( ParserConstants.TYPEDEF ) )
            check( node );
        return super.visit( node, data );
    }

    private void check( final SimpleNode node )
    {
        node.accept( new AbstractVisitor()
        {
            public Object visit( final AstParameterName subnode, final Object data )
            {
                if( subnode.jjtGetParent() == node )
                {
                    final Token token = subnode.getFirstToken();
                    if( !token.image.matches( format ) )
                        listener.fail( "invalid member name", token.beginLine );
                }
                return data;
            }
        }, null );
    }
}
