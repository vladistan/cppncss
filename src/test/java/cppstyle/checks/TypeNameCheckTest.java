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
import java.io.StringReader;
import java.util.Properties;
import cppast.ParseException;
import cppast.Parser;
import cpptools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public final class TypeNameCheckTest extends EasyMockTestCase
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

    private void check( final String data, final String regex ) throws ParseException
    {
        expect( properties.getProperty( "format" ) ).andReturn( regex );
        replay();
        final TypeNameCheck check = new TypeNameCheck( listener, properties );
        check.visit( new Parser( new StringReader( data ) ).translation_unit(), null );
    }

    public void testMissingFormatPropertyThrows()
    {
        expect( properties.getProperty( "format" ) ).andReturn( null );
        replay();
        try
        {
            new TypeNameCheck( listener, properties );
        }
        catch( final Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testMatchingFormatIsValid() throws ParseException
    {
        check( "class ValidClassName {};", "^[A-Z][a-zA-Z]*$" );
    }

    public void testNonMachingFormatIsFailure() throws ParseException
    {
        listener.fail( "invalid type name line 1" );
        check( "class invalidClassName {};", "^[A-Z][a-zA-Z]*$" );
    }

    public void testAnonymousClassIsSkipped() throws ParseException
    {
        check( "class {} myClass;", "^[A-Z][a-zA-Z]*$" );
    }
}
