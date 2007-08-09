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

import cppast.AbstractVisitor;
import cppast.AstTranslationUnit;
import cppast.ParserConstants;
import cppast.Token;

/**
 * Checks whether files contain whitespace characters at end of line or not.
 *
 * @author Mathieu Champlon
 */
public final class WhitespaceAtEndOfLineCheck extends AbstractVisitor
{
    private final CheckListener listener;

    /**
     * Create a tab character check.
     *
     * @param listener
     */
    public WhitespaceAtEndOfLineCheck( final CheckListener listener )
    {
        if( listener == null )
            throw new IllegalArgumentException( "argument 'listener' is null" );
        this.listener = listener;
    }

    public Object visit( final AstTranslationUnit node, final Object data )
    {
        for( Token token = node.getFirstToken(); token != null; token = token.next )
        {
            Token specialToken = token.specialToken;
            while( specialToken != null )
            {
                check( specialToken );
                specialToken = specialToken.specialToken;
            }
        }
        if( whitespace( node.getLastToken().specialToken ) )
            listener.fail( "whitespace at end of file" );
        return data;
    }

    private void check( final Token token )
    {
        if( token.kind == ParserConstants.NEW_LINE && whitespace( token.specialToken ) )
            listener.fail( "whitespace at end of line " + token.specialToken.endLine );
        if( token.kind == ParserConstants.C_STYLE_COMMENT || token.kind == ParserConstants.CPP_STYLE_COMMENT )
        {
            final String[] lines = token.image.split( "\\r\\n|\\n|\\r" );
            for( int line = 0; line < lines.length - 1; ++line )
                if( whitespace( lines[line] ) )
                    listener.fail( "whitespace at end of line " + (token.beginLine + line) );
        }
    }

    private boolean whitespace( final Token token )
    {
        return token != null && whitespace( token.image );
    }

    private boolean whitespace( final String string )
    {
        return string.endsWith( " " ) || string.endsWith( "\t" );
    }
}
