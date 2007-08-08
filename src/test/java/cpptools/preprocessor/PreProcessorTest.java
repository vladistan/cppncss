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

package cpptools.preprocessor;

import java.io.StringReader;

import junit.framework.TestCase;
import cppast.JavaCharStream;
import cppast.ParserConstants;
import cppast.ParserTokenManager;
import cppast.Token;
import cpptools.TokenProviderAdapter;

/**
 * @author Mathieu Champlon
 */
public class PreProcessorTest extends TestCase
{
    private PreProcessor processor;

    protected void setUp()
    {
        processor = new PreProcessor( new TokenProviderAdapter( new ParserTokenManager( null ) ) );
    }

    private void parse( final String data )
    {
        processor.reset( new JavaCharStream( new StringReader( data ) ) );
    }

    private void assertToken( int kind, String image )
    {
        final Token token = processor.next();
        assertEquals( kind, token.kind );
        assertEquals( image, token.image );
        assertNull( token.next );
    }

    private void assertLocation( final Token token, final int beginLine, final int beginColumn, final int endLine, final int endColumn )
    {
        assertEquals( beginLine, token.beginLine );
        assertEquals( beginColumn, token.beginColumn );
        assertEquals( endLine, token.endLine );
        assertEquals( endColumn, token.endColumn );
    }

    public void testNoMacroNoDefineDoesNotModifyTokens()
    {
        parse( "here is my text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "my" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
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
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithoutContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my() text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( anything ! ) text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithExpressionContentReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( ((anything ! (anything !)) anything !) ) text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroReplacesSeveralOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my( something ) my( something else ) text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "your" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroNameWithoutParenthesisIsNoOp()
    {
        processor.addMacro( "my", "your" );
        parse( "here is my text()" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "my" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.LPARENTHESIS, "(" );
        assertToken( ParserConstants.RPARENTHESIS, ")" );
        assertToken( ParserConstants.EOF, "" );
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
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.SEMICOLON, ";" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testDefineWithNonIdTokenReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", ";" );
        parse( "here is my text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.SEMICOLON, ";" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "this()" );
        parse( "here is my() text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.THIS, "this" );
        assertToken( ParserConstants.LPARENTHESIS, "(" );
        assertToken( ParserConstants.RPARENTHESIS, ")" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testDefineWithSeveralNonIdTokensReplacesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is my text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.THIS, "this" );
        assertToken( ParserConstants.LPARENTHESIS, "(" );
        assertToken( ParserConstants.RPARENTHESIS, ")" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesOccurenceWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testMacroWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addMacro( "my", "" );
        parse( "here is my() my() text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testDefineWithEmptyValueRemovesTwoConsecutiveOccurencesWithinInputTokens()
    {
        processor.addDefine( "my", "" );
        parse( "here is my my text" );
        assertToken( ParserConstants.ID, "here" );
        assertToken( ParserConstants.ID, "is" );
        assertToken( ParserConstants.ID, "text" );
        assertToken( ParserConstants.EOF, "" );
    }

    public void testInsertedTokensLinesAndColumnsAreThoseOfTheDefineNameToken()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is my text" );
        processor.next();
        processor.next();
        assertLocation( processor.next(), 1, 9, 1, 10 );
        assertLocation( processor.next(), 1, 9, 1, 10 );
        assertLocation( processor.next(), 1, 9, 1, 10 );
    }

    public void testInsertedTokensLinesAndColumnsAreThoseOfTheMacroNameToken()
    {
        processor.addMacro( "my", "this()" );
        parse( "here is my() text" );
        processor.next();
        processor.next();
        assertLocation( processor.next(), 1, 9, 1, 10 );
        assertLocation( processor.next(), 1, 9, 1, 10 );
        assertLocation( processor.next(), 1, 9, 1, 10 );
    }

    public void testSpecialTokensAreKeptWhenReplacingToken()
    {
        processor.addDefine( "my", "this()" );
        parse( "here is /*surely*/my text" );
        processor.next();
        processor.next();
        final Token token = processor.next();
        assertNotNull( token.specialToken );
        assertEquals( "this", token.image );
        assertEquals( "/*surely*/", token.specialToken.image );
    }

    public void testSpecialTokensAreKeptWhenRemovingToken()
    {
        processor.addDefine( "my", "" );
        parse( "here is /*surely*/my/*own*/text" );
        processor.next();
        processor.next();
        final Token token = processor.next();
        assertNotNull( token.specialToken );
        assertEquals( "text", token.image );
        assertEquals( "/*own*/", token.specialToken.image );
        assertNotNull( token.specialToken.specialToken );
        assertEquals( "/*surely*/", token.specialToken.specialToken.image );
    }
}
