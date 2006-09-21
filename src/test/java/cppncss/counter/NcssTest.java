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
import cppncss.counter.FunctionObserver;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;

/**
 * @author Mathieu Champlon
 */
public class NcssTest extends EasyMockTestCase
{
    private FunctionObserver observer;

    /**
     * {@inheritDoc}
     */
    protected void setUp()
    {
        observer = createMock( FunctionObserver.class );
    }

    private void parse( final String data ) throws ParseException
    {
        new Parser( new StringReader( data ) ).translation_unit().jjtAccept(
                new FunctionVisitor( new NcssCounter( observer ) ), null );
    }

    public void testFunctionWithEmptyBodyHasNcssValueOfOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 1 );
        replay();
        parse( "void MyFunction() {}" );
    }

    public void testConstructorInitializerIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyClass::MyClass()", 1, 2 );
        replay();
        parse( "MyClass::MyClass() : i() {}" );
    }

    public void testSemiColumnDoesNotIncrementNcss() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 1 );
        replay();
        parse( "void MyFunction() { ; }" );
    }

    public void testDeclarationStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { int i = 0; }" );
    }

    public void testExpressionStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { i + 1; }" );
    }

    public void testIfStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { if( true ); }" );
    }

    public void testElseStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 3 );
        replay();
        parse( "void MyFunction() { if( true ); else; }" );
    }

    public void testWhileStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { while( true ); }" );
    }

    public void testDoStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { do {} while( true ); }" );
    }

    public void testForStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { for( ;; ); }" );
    }

    public void testSwitchStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { switch( i ); }" );
    }

    public void testBreakStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 3 );
        replay();
        parse( "void MyFunction() { for( ;; ) { break; } }" );
    }

    public void testContinueStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 3 );
        replay();
        parse( "void MyFunction() { for( ;; ) { continue; } }" );
    }

    public void testReturnStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { return; }" );
    }

    public void testGotoStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { goto label; }" );
    }

    public void testCatchStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 3 );
        replay();
        parse( "void MyFunction() { try {} catch( std::exception& ) {} catch(...) {} }" );
    }

    public void testLabelIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 2 );
        replay();
        parse( "void MyFunction() { label: ; }" );
    }

    public void testDefaultStatementIncrementsNcssByOne() throws ParseException
    {
        observer.notify( "NCSS", "MyFunction()", 1, 3 );
        replay();
        parse( "void MyFunction() { switch( i ) { default : ; } }" );
    }
}
