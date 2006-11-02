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
    /**
     * Tested object.
     */
    private NcssCounter counter;
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
        counter = new NcssCounter( observer );
    }

    private void assertNcss( final int expected, final String content ) throws ParseException
    {
        observer.notify( "NCSS", "my item", 42, expected );
        replay();
        new Parser( new StringReader( content ) ).translation_unit().jjtAccept( counter, null );
        counter.flush( "my item", 42 );
        verify();
        reset();
    }

    public void testEmptyContentHasNcssOfZero() throws ParseException
    {
        assertNcss( 0, "" );
    }

    public void testSingleSemiColonHasNcssOfZero() throws ParseException
    {
        assertNcss( 0, ";" );
    }

    public void testDeclarationStatementHasNcssValueOfOne() throws ParseException
    {
        assertNcss( 1, "int i = 0;" );
    }

    public void testFunctionDeclarationHasNcssValueOfOne() throws ParseException
    {
        assertNcss( 1, "void MyFunction();" );
    }

    public void testFunctionDefinitionWithEmptyBodyHasNcssValueOfOne() throws ParseException
    {
        assertNcss( 1, "void MyFunction() {}" );
    }

    public void testConstructorInitializerIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "MyClass::MyClass() : i() {}" );
    }

    public void testSemiColonDoesNotIncrementNcss() throws ParseException
    {
        assertNcss( 1, "void MyFunction() { ; }" );
    }

    public void testDeclarationStatementWithinFunctionIncrementsNcssByOne() throws ParseException
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
        assertNcss( 2, "void MyFunction() { switch( i ) {} }" );
    }

    public void testCaseStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { switch( i ) { case 0 : ; } }" );
    }

    public void testDefaultStatementIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { switch( i ) { default : ; } }" );
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

    public void testNamespaceDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "namespace my_namespace {}" );
        assertNcss( 1, "namespace {}" );
        assertNcss( 1, "namespace nm = my_namespace::sub_namespace;" );
    }

    public void testUsingNamespaceIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "using namespace std;" );
        assertNcss( 1, "using namespace std::vector;" );
    }

    public void testClassDeclarationIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "class MyClass;" );
    }

    public void testClassDeclarationWithinFunctionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { class MyClass; }" );
    }

    public void testClassDefinitionWithinFunctionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { class MyClass {}; }" );
    }

    public void testClassDefinitionWithOneMethodDeclarationWithinFunctionIncrementsNcssByTwo() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { class MyClass { void MyMethod(); }; }" );
    }

    public void testClassDefinitionWithOneVariableDeclarationWithinFunctionIncrementsNcssByTwo() throws ParseException
    {
        assertNcss( 3, "void MyFunction() { class MyClass { int i; }; }" );
    }

    public void testClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "class MyClass {};" );
    }

    public void testMemberVariableWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { int i; };" );
    }

    public void testMethodDeclarationWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { void MyMethod(); };" );
    }

    public void testConstructorDeclarationWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { MyClass(); };" );
    }

    public void testDestructorDeclarationWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { ~MyClass(); };" );
    }

    public void testMethodDefinitionWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { void MyMethod() {} };" );
    }

    public void testClassDefinitionWithinClassDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "class MyClass { class MyInnerClass {}; };" );
    }

    public void testClassVariableDefinitionIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "class MyClass {} c;" );
        assertNcss( 1, "class {} c;" );
    }

    public void testStructDeclarationIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "struct MyStruct;" );
        assertNcss( 1, "struct MyStruct {};" );
        assertNcss( 1, "struct MyStruct {} s;" );
        assertNcss( 1, "struct {} s;" );
    }

    public void testUnionDeclarationIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "union MyUnion;" );
        assertNcss( 1, "union MyUnion {};" );
        assertNcss( 1, "union MyUnion {} u;" );
        assertNcss( 1, "union {} u;" );
    }

    public void testEnumIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "enum MyEnum;" );
        assertNcss( 1, "enum MyEnum {};" );
        assertNcss( 1, "enum MyEnum {} e;" );
        assertNcss( 1, "enum {} e;" );
    }

    public void testTypedefIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 1, "typedef int id;" );
        assertNcss( 1, "typedef class MyClass {} c;" );
        assertNcss( 2, "void MyFunction() { typedef int id; }" );
    }

    public void testInitializerIncrementsNcssByOne() throws ParseException
    {
        assertNcss( 2, "MyClass::MyClass() : Base_ABC() {}" );
        assertNcss( 2, "MyClass::MyClass() : Base_ABC( this ) {}" );
        assertNcss( 2, "MyClass::MyClass() : data_( 0 ) {}" );
        assertNcss( 3, "MyClass::MyClass() : data_( 0 ), counter_( 0 ) {}" );
    }

    public void testLabelIncrementNcssByOne() throws ParseException
    {
        assertNcss( 2, "void MyFunction() { label: ; }" );
    }
}
