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
public final class MemberNameCheckTest extends EasyMockTestCase
{
    private static final String FORMAT = "^[a-z][a-zA-Z]+_$";
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
        final MemberNameCheck check = new MemberNameCheck( listener, properties );
        check.visit( new Parser( new StringReader( data ) ).translation_unit(), null );
    }

    public void testMissingFormatPropertyThrows()
    {
        expect( properties.getProperty( "format" ) ).andReturn( null );
        replay();
        try
        {
            new MemberNameCheck( listener, properties );
        }
        catch( final Exception e )
        {
            return;
        }
        fail( "should have thrown" );
    }

    public void testMemberNameMatchingFormatIsValid() throws ParseException
    {
        check( "class C { int valid_; };", FORMAT );
    }

    public void testMemberNameNonMachingFormatIsFailure() throws ParseException
    {
        listener.fail( "invalid member name", 1 );
        check( "class C { int invalid; };", FORMAT );
    }

    public void testArrayMemberNameMatchingFormatIsValid() throws ParseException
    {
        check( "class C { int valid_[3]; };", FORMAT );
    }

    public void testArrayMemberNameNonMachingFormatIsFailure() throws ParseException
    {
        listener.fail( "invalid member name", 1 );
        check( "class C { int invalid[3]; };", FORMAT );
    }

    public void testClassMemberNameMatchingFormatIsValid() throws ParseException
    {
        check( "class C { class Inner{} valid_; };", FORMAT );
    }

    public void testClassMemberNameNonMachingFormatIsFailure() throws ParseException
    {
        listener.fail( "invalid member name", 1 );
        check( "class C { class Inner{} invalid; };", FORMAT );
    }

    public void testTypedefIsSkipped() throws ParseException
    {
        check( "class C { typedef int T_INT; };", "^UNUSED$" );
    }

    public void testInnerClassDeclarationIsSkipped() throws ParseException
    {
        check( "class C { class Inner{}; };", "^UNUSED$" );
    }

    public void testStaticMemberIsSkipped() throws ParseException
    {
        check( "class C { static int valid_; };", "^UNUSED$" );
    }
}
