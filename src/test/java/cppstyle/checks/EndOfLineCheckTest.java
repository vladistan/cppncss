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
import java.util.Properties;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class EndOfLineCheckTest extends EasyMockTestCase
{
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

    private void check( final String data, final String type )
    {
        expect( properties.getProperty( "type" ) ).andReturn( type );
        replay();
        new EndOfLineCheck( listener, properties ).notify( data );
    }

    public void testInvalidEndOfLineTypeThrows()
    {
        try
        {
            check( ";", "INVALID" );
        }
        catch( final Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testNoEndOfLineIsValid()
    {
        check( ";", "crlf" );
    }

    public void testMatchingEndOfLineIsValid()
    {
        check( ";\n", "lf" );
    }

    public void testCrInvalidLfEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 1 );
        check( ";\r", "lf" );
    }

    public void testCrInvalidCrLfEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 1 );
        check( ";\r", "crlf" );
    }

    public void testLfInvalidCrEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 1 );
        check( ";\n", "cr" );
    }

    public void testLfInvalidCrLfEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 1 );
        check( ";\n", "crlf" );
    }

    public void testCrLfInvalidCrEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 2 );
        check( ";\r\n", "cr" );
    }

    public void testCrLfInvalidLfEndOfLineGeneratesFailure()
    {
        listener.fail( "invalid end of line", 1 );
        check( ";\r\n", "lf" );
    }

    public void testDefaultEndOfLineTypeIsAutomaticallyDetected()
    {
        listener.fail( "invalid end of line", 2 );
        check( ";\r\n\r", null );
    }
}
