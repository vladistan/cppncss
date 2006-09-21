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

package cppncss.preprocessor;

import java.io.StringReader;
import junit.framework.TestCase;
import cppast.Token;
import cppncss.preprocessor.PreProcessor;

/**
 * @author Mathieu Champlon
 */
public class PreProcessorTest extends TestCase
{
    private PreProcessor processor;

    protected void setUp()
    {
        processor = new PreProcessor();
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

    private void assertLocation( final Token token, final int beginLine, final int beginColumn, final int endLine,
            final int endColumn )
    {
        assertEquals( beginLine, token.beginLine );
        assertEquals( beginColumn, token.beginColumn );
        assertEquals( endLine, token.endLine );
        assertEquals( endColumn, token.endColumn );
    }

    public void testNoMacroNoDefineDoesNotModifyTokens()
    {
        parse( "here is my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "my" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testEmptyNameIsInvalid()
    {
        try
        {
            processor.addDefine( "  ", "" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testDefineNameMustBeMadeOfTokenOfTypeId()
    {
        try
        {
            processor.addDefine( "this", "" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testDefineNameMustBeMadeOfOneToken()
    {
        try
        {
            processor.addDefine( "one two", "" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testDefineReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "your" );
        parse( "here is my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithoutContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my() text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( anything ! ) text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithExpressionContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( ((anything ! (anything !)) anything !) ) text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroReplacesSeveralOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( something ) my( something else ) text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "your" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroNameWithoutParenthesisIsNoOp()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my text()" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "my" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.LPARENTHESIS, "(" );
        assertToken( PreProcessor.RPARENTHESIS, ")" );
        assertToken( PreProcessor.EOF, "" );
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
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.SEMICOLON, ";" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testDefineWithNonIdTokenReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", ";" );
        parse( "here is my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.SEMICOLON, ";" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "this()" );
        parse( "here is my() text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.THIS, "this" );
        assertToken( PreProcessor.LPARENTHESIS, "(" );
        assertToken( PreProcessor.RPARENTHESIS, ")" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testDefineWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.THIS, "this" );
        assertToken( PreProcessor.LPARENTHESIS, "(" );
        assertToken( PreProcessor.RPARENTHESIS, ")" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() my() text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my my text" );
        assertToken( PreProcessor.ID, "here" );
        assertToken( PreProcessor.ID, "is" );
        assertToken( PreProcessor.ID, "text" );
        assertToken( PreProcessor.EOF, "" );
    }

    public void testInsertedTokensLinesAndColumnsAreThoseOfTheDefineNameToken()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is my text" );
        processor.getNextToken();
        processor.getNextToken();
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
    }

    public void testInsertedTokensLinesAndColumnsAreThoseOfTheMacroNameToken()
    {
        processor.addMacro( "my", "this()" );
        parse( "here is my() text" );
        processor.getNextToken();
        processor.getNextToken();
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
        assertLocation( processor.getNextToken(), 1, 9, 1, 10 );
    }
}
