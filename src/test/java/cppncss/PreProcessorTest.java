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

import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class PreProcessorTest extends TestCase
{
    private PreProcessor processor;

    protected void setUp() throws Exception
    {
        processor = new PreProcessor();
    }

    public void testNoMacroNoDefineDoesNotModifyText()
    {
        final String text = "this is my text";
        assertEquals( text, processor.filter( text ) );
    }

    public void testDefineReplacesOccurenceWithinText()
    {
        processor.addDefine( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my text" ) );
    }

    public void testMacroWithoutContentReplacesOccurenceWithinText()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my() text" ) );
    }

    public void testMacroSeparatedFromBracetsReplacesOccurenceWithinText()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my  () text" ) );
    }

    public void testMacroWithContentReplacesOccurenceWithinText()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my( anything ! ) text" ) );
    }

    public void testMacroWithExpressionContentReplacesOccurenceWithinText()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is your text", processor
                .filter( "this is my( ((anything ! (anything !)) anything !) ) text" ) );
    }

    public void testMacroNameWithoutParenthesisIsNoOp()
    {
        processor.addMacro( "my", "your" );
        final String text = "this is my text()";
        assertEquals( text, processor.filter( text ) );
    }

    public void testMacroAndDefineWithSameNameReplacesOccurenceWithinText()
    {
        processor.addMacro( "my", "your" );
        processor.addDefine( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my text" ) );
        assertEquals( "this is your text", processor.filter( "this is my( anything ! ) text" ) );
    }

    public void testMacroReplacesSeveralOccurencesWithinText()
    {
        processor.addMacro( "my", "" );
        processor.addDefine( "my", "" );
        assertEquals( "", processor.filter( "my( something )my( something else )" ) );
    }

    public void testMacroReplacementWithParenthesisInString()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is your text", processor.filter( "this is my( \"any string )\") text" ) );
    }

    public void testRegisterMacroTwiceThrowsAnException()
    {
        processor.addMacro( "name", "value" );
        try
        {
            processor.addMacro( "name", "another value" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "must throw an exception" );
    }

    public void testRegisterDefineTwiceThrowsAnException()
    {
        processor.addDefine( "name", "value" );
        try
        {
            processor.addDefine( "name", "another value" );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "must throw an exception" );
    }

    public void testDefineOnlyReplacesToken()
    {
        processor.addDefine( "my", "your" );
        assertEquals( "this is my_text", processor.filter( "this is my_text" ) );
        assertEquals( "this is_my text", processor.filter( "this is_my text" ) );
    }

    public void testMacroOnlyReplacesToken()
    {
        processor.addMacro( "my", "your" );
        assertEquals( "this is_my( text )", processor.filter( "this is_my( text )" ) );
    }

    public void testMacroWithParameterReplacesText()
    {
        fail( "todo !" );
    }
}
