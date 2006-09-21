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

import java.io.StringReader;
import junit.framework.TestCase;
import cppast.Token;

/**
 * @author Mathieu Champlon
 */
public class PreProcessor2Test extends TestCase
{
    private PreProcessor2 processor;

    protected void setUp()
    {
        processor = new PreProcessor2();
    }

    private void parse( final String data )
    {
        processor.reset( new StringReader( data ) );
    }

    private void assertToken( int kind, String image )
    {
        final Token token = processor.getNextToken();
        assertEquals( kind, token.kind );
        assertEquals( image, token.image );
        assertNull( token.next );
    }

    public void testNoMacroNoDefineDoesNotModifyTokens()
    {
        parse( "here is my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "my" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testDefineReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "your" );
        parse( "here is my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithoutContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my() text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( anything ! ) text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithExpressionContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( ((anything ! (anything !)) anything !) ) text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroReplacesSeveralOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( something ) my( something else ) text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "your" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroNameWithoutParenthesisIsNoOp()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my text()" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "my" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.LPARENTHESIS, "(" );
        assertToken( PreProcessor2.RPARENTHESIS, ")" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testRegisterSameMacroNameTwiceThrowsAnException()
    {
        processor.addMacro( "name", "value" );
        try
        {
            processor.addMacro( "name", "value" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testMacroWithNonIdTokenReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", ";" );
        parse( "here is my() text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.SEMICOLON, ";" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testDefineWithNonIdTokenReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", ";" );
        parse( "here is my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.SEMICOLON, ";" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "this()" );
        parse( "here is my() text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.THIS, "this" );
        assertToken( PreProcessor2.LPARENTHESIS, "(" );
        assertToken( PreProcessor2.RPARENTHESIS, ")" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testDefineWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.THIS, "this" );
        assertToken( PreProcessor2.LPARENTHESIS, "(" );
        assertToken( PreProcessor2.RPARENTHESIS, ")" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() my() text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my my text" );
        assertToken( PreProcessor2.ID, "here" );
        assertToken( PreProcessor2.ID, "is" );
        assertToken( PreProcessor2.ID, "text" );
        assertToken( PreProcessor2.EOF, "" );
    }
}
