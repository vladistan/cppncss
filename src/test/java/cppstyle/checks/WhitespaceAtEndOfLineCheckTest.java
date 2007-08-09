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

import java.io.StringReader;
import cppast.ParseException;
import cppast.Parser;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class WhitespaceAtEndOfLineCheckTest extends EasyMockTestCase
{
    private static final String SEPARATOR = System.getProperty( "line.separator" );
    /**
     * Tested object.
     */
    private WhitespaceAtEndOfLineCheck check;
    /**
     * Mock objects.
     */
    private CheckListener listener;

    protected void setUp() throws Exception
    {
        listener = createMock( CheckListener.class );
        check = new WhitespaceAtEndOfLineCheck( listener );
    }

    private void check( final String data ) throws ParseException
    {
        replay();
        check.visit( new Parser( new StringReader( data ) ).translation_unit(), null );
    }

    public void testNoWhitespaceIsValid() throws ParseException
    {
        check( ";" );
    }

    public void testWhitespaceAtEndOfLineGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "; " + SEPARATOR );
    }

    public void testWhitespaceAtEndOfFileGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of file" );
        check( "; " );
    }

    public void testWhitespaceAtEndOfLineInsideCStyleCommentGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "/* my " + SEPARATOR + "comment */" );
    }

    public void testWhitespaceAtEndOfLineInsideCppStyleCommentGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "// my " + SEPARATOR + "// comment" );
    }

    public void testWhitespaceAtEndOfLineAfterCStyleCommentGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "/* my comment */ " + SEPARATOR + ";" );
    }

    public void testWhitespaceAtEndOfLineAfterCppStyleCommentGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "// my " + SEPARATOR + ";" );
    }

    public void testWhitespaceAtEndOfLineInsidePreprocessorDirectiveGeneratesFailure() throws ParseException
    {
        listener.fail( "whitespace at end of line 1" );
        check( "#define symbol something " + SEPARATOR + ";" );
    }
}
