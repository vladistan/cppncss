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

import static org.easymock.EasyMock.expect;
import java.io.IOException;
import java.util.Properties;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class HeaderCheckTest extends EasyMockTestCase
{
    private static final String SEPARATOR = System.getProperty( "line.separator" );
    /**
     * Mock objects.
     */
    private CheckListener listener;
    private Properties properties;

    protected void setUp() throws Exception
    {
        listener = createMock( CheckListener.class );
        properties = createMock( Properties.class );
    }

    private HeaderCheck create( final String header, final String ignore ) throws IOException
    {
        expect( properties.getProperty( "ignoreLines" ) ).andReturn( ignore );
        expect( properties.getProperty( "header" ) ).andReturn( header );
        replay();
        return new HeaderCheck( listener, properties );
    }

    private void check( final String actual, final String expected, final String ignore ) throws IOException
    {
        create( expected, ignore ).notify( actual );
    }

    private void check( final String actual, final String expected ) throws IOException
    {
        check( actual, expected, null );
    }

    public void testCreationWithoutAnyOfThePropertyDefiningTheExpecteHeaderContentThrows()
    {
        expect( properties.getProperty( "header" ) ).andReturn( null );
        expect( properties.getProperty( "headerFile" ) ).andReturn( null );
        replay();
        try
        {
            new HeaderCheck( listener, properties );
        }
        catch( final Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testExactHeaderMatchIsAllowed() throws IOException
    {
        final String header = "/* this is the header" + SEPARATOR + " we want to check for */";
        check( header, header );
    }

    public void testEmptyActualHeaderIsFailure() throws IOException
    {
        final String actual = "";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        listener.fail( "file header mismatch", 1, 2 );
        check( actual, expected );
    }

    public void testComparisonFailureOnFirstLineIsFailure() throws IOException
    {
        final String actual = "/* this is the wrong header" + SEPARATOR + " we want to check for */";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        listener.fail( "file header mismatch", 1, 1 );
        check( actual, expected );
    }

    public void testComparisonFailureOnSecondLineIsFailure() throws IOException
    {
        final String actual = "/* this is the header" + SEPARATOR + " we want to test */";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        listener.fail( "file header mismatch", 2, 2 );
        check( actual, expected );
    }

    public void testComparisonFailuresOnBothLinesIsFailure() throws IOException
    {
        final String actual = "/* this is the wrong header" + SEPARATOR + " we want to test */";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        listener.fail( "file header mismatch", 1, 2 );
        check( actual, expected );
    }

    public void testTooManyLinesInExpectationIsAllowed() throws IOException
    {
        final String actual = "// this is the header" + SEPARATOR + "// this is more";
        final String expected = "// this is the header";
        check( actual, expected );
    }

    public void testMissingSecondLineInActualIsFailure() throws IOException
    {
        final String actual = "// this is the header";
        final String expected = "// this is the header" + SEPARATOR + "// we want to test";
        listener.fail( "file header mismatch", 2, 2 );
        check( actual, expected );
    }

    public void testComparisonFailureOnSecondIgnoredLineIsAllowed() throws IOException
    {
        final String actual = "/* this is the header" + SEPARATOR + " we want to test */";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        check( actual, expected, "2" );
    }

    public void testComparisonFailureOnBothIgnoredLinesIsAllowed() throws IOException
    {
        final String actual = "/* this is the wrong header" + SEPARATOR + " we want to test */";
        final String expected = "/* this is the header" + SEPARATOR + " we want to check for */";
        check( actual, expected, "1,2" );
    }
}
