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

package cppncss.counter;

import java.io.StringReader;
import tools.EasyMockTestCase;
import cppast.ParseException;
import cppast.Parser;

/**
 * @author Mathieu Champlon
 */
public class CcnTest extends EasyMockTestCase
{
    /**
     * Tested object.
     */
    private CcnCounter counter;
    /**
     * Mock objects.
     */
    private CounterObserver observer;

    /**
     * {@inheritDoc}
     */
    protected void setUp()
    {
        observer = createMock( CounterObserver.class );
        counter = new CcnCounter( observer );
    }

    private void assertCcn( final int expected, final String content ) throws ParseException
    {
        observer.notify( "CCN", "my item", 42, expected );
        replay();
        new Parser( new StringReader( content ) ).translation_unit().jjtAccept( counter, null );
        counter.flush( "my item", 42 );
    }

    public void testFunctionWithEmptyBodyHasCcnValueOfOne() throws ParseException
    {
        assertCcn( 1, "void MyFunction() {}" );
    }

    public void testTwoFunctionsWithEmptyBodyHaveCcnValueOfTwo() throws ParseException
    {
        assertCcn( 2, "void MyFunction() {} void MyOtherFunction() {}" );
    }

    public void testIfStatementIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { if( true ); }" );
    }

    public void testForStatementIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { for( ;; ); }" );
    }

    public void testWhileStatementIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { while( true ); }" );
    }

    public void testSwitchStatementIncrementsCcnByOnePerCase() throws ParseException
    {
        assertCcn( 4, "void MyFunction() { switch( i ) { case 0: break; case 1: case 2: break; default: break; } }" );
    }

    public void testSwitchStatementDoesNotIncrementCcnForDefault() throws ParseException
    {
        assertCcn( 1, "void MyFunction() { switch( i ) { default: break; } }" );
    }

    public void testCatchStatementIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 3, "void MyFunction() { try {} catch( std::exception& ) {} catch(...) {} }" );
    }

    public void testDoStatementIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { do {} while( true ); }" );
    }

    public void testAndExpressionIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { i && j; }" );
    }

    public void testDoubleAndExpressionIncrementsCcnByTwo() throws ParseException
    {
        assertCcn( 3, "void MyFunction() { i && j && k; }" );
    }

    public void testOrExpressionIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { i || j; }" );
    }

    public void testDoubleOrExpressionIncrementsCcnByTwo() throws ParseException
    {
        assertCcn( 3, "void MyFunction() { i || j || k; }" );
    }

    public void testConditionalExpressionIncrementsCcnByOne() throws ParseException
    {
        assertCcn( 2, "void MyFunction() { i ? j : 0; }" );
    }

    public void testDoubleConditionalExpressionIncrementsCcnByTwo() throws ParseException
    {
        assertCcn( 3, "void MyFunction() { i ? j : k ? 1 : 0; }" );
    }

    public void testExpressionWithAndOrAndConditionalIncrementsCcnByThree() throws ParseException
    {
        assertCcn( 4, "void MyFunction() { i ? (j && k) : (l || m); }" );
    }

    public void testCcnFromMethodOfClassDefinedOnStackAddsUp() throws ParseException
    {
        assertCcn( 3, "void MyFunction() { class MyClass{ void MyMethod() { if(i); } }; }" );
    }
}
