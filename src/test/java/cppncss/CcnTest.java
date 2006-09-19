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

package cppncss;

import java.io.StringReader;
import tools.EasyMockTestCase;
import cppast.ParseException;
import cppast.Parser;

/**
 * @author Mathieu Champlon
 */
public class CcnTest extends EasyMockTestCase
{
    private FunctionObserver observer;

    /**
     * {@inheritDoc}
     */
    protected void setUp()
    {
        observer = createMock( FunctionObserver.class );
    }

    private void parseString( final String data ) throws ParseException
    {
        new Parser( new StringReader( data ) ).translation_unit().jjtAccept(
                new FunctionVisitor( new CcnCounter( observer ) ), null );
    }

    public void testFunctionWithEmptyBodyHasCcnValueOfOne() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 1 );
        replay();
        parseString( "void MyFunction() {}" );
    }

    public void testFunctionWithIfHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { if( true ); }" );
    }

    public void testFunctionWithForHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { for( ;; ); }" );
    }

    public void testFunctionWithWhileHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { while( true ); }" );
    }

    public void testFunctionWithSwitchIncrementsOneCcnPerCase() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 4 );
        replay();
        parseString( "void MyFunction() { switch( i ) { case 0: break; case 1: case 2: break; default: break; } }" );
    }

    public void testFunctionWithSwitchDoesNotIncrementCcnForDefault() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 1 );
        replay();
        parseString( "void MyFunction() { switch( i ) { default: break; } }" );
    }

    public void testFunctionWithTryCatchIncrementsOneCcnPerCatch() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 3 );
        replay();
        parseString( "void MyFunction() { try {} catch( std::exception& ) {} catch(...) {} }" );
    }

    public void testFunctionWithDoWhileHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { do {} while( true ); }" );
    }

    public void testFunctionWithAndExpressionHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { i && j; }" );
    }

    public void testFunctionWithOrExpressionHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { i || j; }" );
    }

    public void testFunctionWithConditionalExpressionHasCcnValueOfTwo() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parseString( "void MyFunction() { i ? j : 0; }" );
    }

    public void testFunctionWithExpressionHasCcnValueOfFour() throws ParseException
    {
        observer.notify( "MyFunction()", 1, 4 );
        replay();
        parseString( "void MyFunction() { i ? (j && k) : (l || m); }" );
    }
}
