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

package checks;

import java.io.StringReader;
import java.util.Properties;
import cppast.ParseException;
import cppast.Parser;
import cppstyle.checks.CheckListener;
import cppstyle.checks.NewlineAtEndOfFileCheck;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class NewlineAtEndOfFileCheckTest extends EasyMockTestCase
{
    /**
     * Tested object.
     */
    private NewlineAtEndOfFileCheck check;
    /**
     * Mock objects.
     */
    private CheckListener listener;
    private Properties properties;
    private static final String SEPARATOR = System.getProperty( "line.separator" );

    protected void setUp() throws Exception
    {
        listener = createMock( CheckListener.class );
        properties = createMock( Properties.class );
        check = new NewlineAtEndOfFileCheck( listener, properties );
    }

    public void testNewlineAtEndOfFileDoesNotGenerateFailure() throws ParseException
    {
        replay();
        check.visit( new Parser( new StringReader( ";" + SEPARATOR ) ).translation_unit(), null );
    }

    public void testMissingNewlineAtEndOfFileGeneratesFailure() throws ParseException
    {
        listener.fail( "missing new line at end of file" );
        replay();
        check.visit( new Parser( new StringReader( ";" ) ).translation_unit(), null );
    }

    public void testCppStyleCommentAtEndOfFileDoesNotHideNewLine() throws ParseException
    {
        check.visit( new Parser( new StringReader( "// the comment" + SEPARATOR ) ).translation_unit(), null );
    }

    public void testPreprocessorDirectiveAtEndOfFileDoesNotHideNewLine() throws ParseException
    {
        check.visit( new Parser( new StringReader( "# the directive" + SEPARATOR ) ).translation_unit(), null );
    }

    public void testCStyleCommentAtEndOfFileDoesNotHideNewLine() throws ParseException
    {
        check.visit( new Parser( new StringReader( "/* the comment */" + SEPARATOR ) ).translation_unit(), null );
    }
}
