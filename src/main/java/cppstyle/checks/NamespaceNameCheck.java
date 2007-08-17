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
import cppast.AstNamespaceDefinition;
import cppast.ParserConstants;
import cppast.Token;

/**
 * Checks for the validity of type (e.g. class) names.
 *
 * @author Mathieu Champlon
 */
public final class NamespaceNameCheck extends AbstractVisitor
{
    private final NameCheck check;

    /**
     * Create a type name check.
     *
     * @param listener the check listener
     * @param properties the available properties
     */
    public NamespaceNameCheck( final CheckListener listener, final Properties properties )
    {
        check = new NameCheck( listener, properties, "namespace" );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstNamespaceDefinition node, final Object data )
    {
        final Token identifier = node.getFirstToken().next;
        if( identifier.kind != ParserConstants.LCURLYBRACE )
            check.verify( identifier );
        return super.visit( node, data );
    }
}
