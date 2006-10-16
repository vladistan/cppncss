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
public class NcssTest extends EasyMockTestCase
{
    private CounterObserver observer;

    /**
     * {@inheritDoc}
     */
    protected void setUp()
    {
        observer = createMock( CounterObserver.class );
    }

    private void assertNcss( final int expected, final String content ) throws ParseException
    {
        observer.notify( "NCSS", null, 1, expected );
        replay();
        new Parser( new StringReader( content ) ).translation_unit().jjtAccept(
                new FileVisitor( new NcssCounter( observer ) ), null );
    }

    public void testFunctionWithEmptyBodyHasNcssValueOfOne() throws ParseException
    {
        assertNcss( 1, "void MyFunction() {}" );
    }

    public void testConstructorInitializerIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "MyClass::MyClass() : i() {}" );
    }

    public void testSemiColumnDoesNotIncrementNcss() throws ParseException
    {
        assertNcss( 1, "void MyFunction() { ; }" );
    }

    public void testDeclarationStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { int i = 0; }" );
    }

    public void testExpressionStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { i + 1; }" );
    }

    public void testIfStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { if( true ); }" );
    }

    public void testElseStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { if( true ); else; }" );
    }

    public void testWhileStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { while( true ); }" );
    }

    public void testDoStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { do {} while( true ); }" );
    }

    public void testForStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { for( ;; ); }" );
    }

    public void testSwitchStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { switch( i ); }" );
    }

    public void testBreakStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { for( ;; ) { break; } }" );
    }

    public void testContinueStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { for( ;; ) { continue; } }" );
    }

    public void testReturnStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { return; }" );
    }

    public void testGotoStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { goto label; }" );
    }

    public void testCatchStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { try {} catch( std::exception& ) {} catch(...) {} }" );
    }

    public void testLabelIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { label: ; }" );
    }

    public void testDefaultStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { switch( i ) { default : ; } }" );
    }
}
