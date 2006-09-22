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

import cppast.AbstractVisitor;
import cppast.AstFunctionBody;
import cppast.AstFunctionName;
import cppast.AstFunctionParameterTypeQualifier;
import cppast.AstFunctionParameters;
import cppast.AstParameterType;
import cppast.Parser;
import cppast.SimpleNode;
import cppast.Token;

/**
 * Provides a visitor implementation to extract a function name.
 *
 * @author Mathieu Champlon
 */
public final class FunctionNameExtractor extends AbstractVisitor
{
    private boolean isFirstParameter;

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionName node, final Object data )
    {
        return node.resolve( build( node, new Filter()
        {
            public String decorate( final Token token )
            {
                final String value = token.image;
                switch( token.kind )
                {
                    case Parser.SCOPE :
                    case Parser.TILDE :
                        return value;
                }
                switch( token.next.kind )
                {
                    case Parser.SCOPE :
                    case Parser.LPARENTHESIS :
                    case Parser.RPARENTHESIS :
                    case Parser.AMPERSAND :
                    case Parser.STAR :
                    case Parser.LESSTHAN :
                    case Parser.COMMA :
                        return value;
                }
                return value + " ";
            }
        } ) );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionParameters node, final Object data )
    {
        return data + "(" + process( node ) + ")";
    }

    private String process( final SimpleNode node )
    {
        isFirstParameter = true;
        final String result = (String)node.accept( this, "" );
        if( !isFirstParameter )
            return result + " ";
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstParameterType node, final Object data )
    {
        final String result = " " + build( node, new Filter()
        {
            public String decorate( final Token token )
            {
                final String value = token.image;
                switch( token.kind )
                {
                    case Parser.CONST :
                    case Parser.SIGNED :
                    case Parser.UNSIGNED :
                    case Parser.LESSTHAN :
                    case Parser.COMMA :
                        return value + " ";
                    case Parser.GREATERTHAN :
                        return " " + value;
                }
                return value;
            }
        } );
        if( !isFirstParameter )
            return data + "," + result;
        isFirstParameter = false;
        return data + result;
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionParameterTypeQualifier node, final Object data )
    {
        return data + build( node, new Filter()
        {
            public String decorate( final Token token )
            {
                final String value = token.image;
                switch( token.kind )
                {
                    case Parser.CONST :
                    case Parser.LPARENTHESIS :
                        return " " + value;
                }
                return value;
            }
        } );
    }

    /**
     * {@inheritDoc}
     */
    public Object visit( final AstFunctionBody node, final Object data )
    {
        return data;
    }

    private interface Filter
    {
        String decorate( final Token token );
    }

    private String build( final SimpleNode node, final Filter filter )
    {
        final StringBuffer buffer = new StringBuffer();
        for( Token token = node.getFirstToken(); token != node.getLastToken().next; token = token.next )
            buffer.append( filter.decorate( token ) );
        return buffer.toString();
    }
}
