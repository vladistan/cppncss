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

import static org.easymock.EasyMock.expect;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import cppast.ParseException;
import cppast.Parser;
import cppstyle.checks.CheckListener;
import cppstyle.checks.FileHeaderCheck;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class FileHeaderCheckTest extends EasyMockTestCase
{
    /**
     * Mock objects.
     */
    private CheckListener listener;
    private Properties properties;
    private File folder;

    protected void setUp() throws Exception
    {
        listener = createMock( CheckListener.class );
        properties = createMock( Properties.class );
        folder = createMock( File.class );
    }

    private FileHeaderCheck create( final String header ) throws IOException
    {
        expect( properties.getProperty( "header" ) ).andReturn( header );
        replay();
        return new FileHeaderCheck( listener, properties, folder );
    }

    private void check( final String actual, final String expected ) throws IOException, ParseException
    {
        create( expected ).visit( new Parser( new StringReader( actual ) ).translation_unit(), null );
    }

    public void testCreationWithoutAnyOfThePropertyDefiningTheExpecteHeaderContentThrows()
    {
        expect( properties.getProperty( "headerFile" ) ).andReturn( null );
        try
        {
            create( null );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testCheckingHeaderAgainstItselfDoesNotReportAnyFailure() throws ParseException, IOException
    {
        final String header = "/* this is the header\n we want to check for */";
        check( header, header );
    }

    public void testExpectedHeaderIsStrippedFromLeadingCrLf() throws ParseException, IOException
    {
        final String actual = "/* this is the header\n we want to check for */";
        final String expected = actual + '\r' + '\n' + '\r';
        check( actual, expected );
    }

    public void testComparisonFailureOnFirstLineIsLoggedToListener() throws ParseException, IOException
    {
        final String actual = "/* this is the wrong header\n we want to check for */";
        final String expected = "/* this is the header\n we want to check for */";
        listener.fail( "file header mismatch line 1" );
        check( actual, expected );
    }

    public void testComparisonFailureOnSecondLineIsLoggedToListener() throws ParseException, IOException
    {
        final String actual = "/* this is the header\n we want to test */";
        final String expected = "/* this is the header\n we want to check for */";
        listener.fail( "file header mismatch line 2" );
        check( actual, expected );
    }

    public void testComparisonFailuresOnBothLinesIsLoggedToListener() throws ParseException, IOException
    {
        final String actual = "/* this is the wrong header\n we want to test */";
        final String expected = "/* this is the header\n we want to check for */";
        listener.fail( "file header mismatch line 1-2" );
        check( actual, expected );
    }

    public void testMissingSecondLineInExpectationIsLoggedAsFailureToListener() throws ParseException, IOException
    {
        final String actual = "// this is the header\r\n// we want to test";
        final String expected = "// this is the header";
        listener.fail( "file header mismatch line 2" );
        check( actual, expected );
    }

    public void testMissingSecondLineInActualIsLoggedAsFailureToListener() throws ParseException, IOException
    {
        final String actual = "// this is the header";
        final String expected = "// this is the header\r\n// we want to test";
        listener.fail( "file header mismatch line 2" );
        check( actual, expected );
    }
}
