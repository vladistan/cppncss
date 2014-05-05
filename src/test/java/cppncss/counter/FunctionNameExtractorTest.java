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

package cppncss.counter;

import java.io.StringReader;
import junit.framework.TestCase;
import cppast.AstTranslationUnit;
import cppast.Node;
import cppast.ParseException;
import cppast.Parser;

/**
 * @author Mathieu Champlon
 */
public class FunctionNameExtractorTest extends TestCase
{
    private String extract( final String data ) throws ParseException
    {
        final Node node = new Parser( new StringReader( data ) ).translation_unit();
        return (String)node.jjtAccept( new FunctionNameExtractor(), null );
    }

    public void testNotFunctionReturnsNull() throws ParseException
    {
        assertNull( extract( "" ) );
    }

    public void testFunctionDefinitionWithoutParameters() throws ParseException
    {
        assertEquals( "MyFunction()", extract( "void MyFunction() {}" ) );
    }

    public void testFunctionDefinitionWithIntegerParameter() throws ParseException
    {
        assertEquals( "MyFunction( int )", extract( "void MyFunction( int p ) {}" ) );
    }

    public void testFunctionDefinitionWithIntegerParameterWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( int )", extract( "void MyFunction( int ) {}" ) );
    }

    public void testFunctionDefinitionWithIntegerPointerParameter() throws ParseException
    {
        assertEquals( "MyFunction( int* )", extract( "void MyFunction( int* p ) {}" ) );
    }

    public void testFunctionDefinitionWithIntegerPointerParameterWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( int* )", extract( "void MyFunction( int* ) {}" ) );
    }

    public void testFunctionDefinitionWithIntegerReferencePointerConstParameter() throws ParseException
    {
        assertEquals( "MyFunction( int*& )", extract( "void MyFunction( int*& p ) {}" ) );
    }

    public void testFunctionDefinitionWithIntegerReferencePointerConstParameterWithoutParameterName()
            throws ParseException
    {
        assertEquals( "MyFunction( int*& )", extract( "void MyFunction( int*& ) {}" ) );
    }

    public void testFunctionDefinitionWithConstPointerConstParameter() throws ParseException
    {
        assertEquals( "MyFunction( const int* const )", extract( "void MyFunction( const int* const p ) {}" ) );
    }

    public void testFunctionDefinitionWithConstPointerConstParameterWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( const int* const )", extract( "void MyFunction( const int* const ) {}" ) );
    }

    public void testFunctionDefinitionWithUnsignedIntegerParameter() throws ParseException
    {
        assertEquals( "MyFunction( unsigned int )", extract( "void MyFunction( unsigned int p ) {}" ) );
    }

    public void testFunctionDefinitionWithUnsignedIntegerParameterWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( unsigned int )", extract( "void MyFunction( unsigned int ) {}" ) );
    }

    public void testFunctionDefinitionWithUnsignedIntegerPointerParameter() throws ParseException
    {
        assertEquals( "MyFunction( unsigned int* )", extract( "void MyFunction( unsigned int* p ) {}" ) );
    }

    public void testFunctionDefinitionWithUnsignedIntegerPointerParameterWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( unsigned int* )", extract( "void MyFunction( unsigned int* ) {}" ) );
    }

    public void testFunctionDefinitionWithSeveralParameters() throws ParseException
    {
        assertEquals( "MyFunction( int, float&, const char* )",
                extract( "void MyFunction( int p1, float& p2, const char* p3 ) {}" ) );
    }

    public void testFunctionDefinitionWithSeveralParametersWithoutParameterNames() throws ParseException
    {
        assertEquals( "MyFunction( int, float&, const char* )",
                extract( "void MyFunction( int, float&, const char* ) {}" ) );
    }

    public void testConstMethodDefinition() throws ParseException
    {
        assertEquals( "MyClass::MyMethod()", extract( "void MyClass::MyMethod() const {}" ) );
    }

    public void testMethodDefinitionWithConstReferenceReturnType() throws ParseException
    {
        assertEquals( "MyClass::MyMethod()", extract( "const MyType& MyClass::MyMethod() {}" ) );
    }

    public void testEqualityOperatorDefinition() throws ParseException
    {
        assertEquals( "MyClass::operator ==( const MyClass& )",
                extract( "bool MyClass::operator==( const MyClass& rhs ) const {}" ) );
    }

    public void testConversionOperatorDefinition() throws ParseException
    {
        assertEquals( "MyClass::operator const unsigned char*()",
                extract( "MyClass::operator const unsigned char*() const {}" ) );
    }

    public void testConstructorDefinition() throws ParseException
    {
        assertEquals( "MyClass::MyClass()", extract( "MyClass::MyClass() {}" ) );
    }

    public void testConstructorDefinitionWithParameter() throws ParseException
    {
        assertEquals( "MyClass::MyClass( int )", extract( "MyClass::MyClass( int p ) {}" ) );
    }

    public void testDestructorDefinition() throws ParseException
    {
        assertEquals( "MyClass::~MyClass()", extract( "MyClass::~MyClass() {}" ) );
    }

    public void testFunctionBodyDoesNotAlterFunctionSignature() throws ParseException
    {
        assertEquals( "MyFunction()", extract( "void MyFunction() { char *p; }" ) );
    }

    public void testArrayArgument() throws ParseException
    {
        assertEquals( "MyFunction( MyType[3] )", extract( "void MyFunction( MyType p[3] ) {}" ) );
    }

    public void testArrayArgumentWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( MyType[3] )", extract( "void MyFunction( MyType[3] ) {}" ) );
    }

    public void testTemplateClassMethod() throws ParseException
    {
        assertEquals( "MyClass< T, F >::MyMethod()", extract( "void MyClass< T, F >::MyMethod() {}" ) );
    }

    public void testTemplateParameterFunction() throws ParseException
    {
        assertEquals( "MyFunction( MyClass< T, F >& )", extract( "void MyFunction( MyClass< T, F >& p ) {}" ) );
    }

    public void testTemplateParameterFunctionWithoutParameterName() throws ParseException
    {
        assertEquals( "MyFunction( MyClass< T, F >& )", extract( "void MyFunction( MyClass< T, F >& ) {}" ) );
    }

    public void testPointerOnMemberParameterFunction() throws ParseException
    {
        assertEquals( "MyFunction( void (C::*)( char, float ) )",
                extract( "void MyFunction( void (C::*M)( char, float ) ) {}" ) );
    }

    public void testPointerOnFunctionParameterFunction() throws ParseException
    {
        assertEquals( "MyFunction( void (*)( char, float ) )",
                extract( "void MyFunction( void (*F)( char, float ) ) {}" ) );
    }

    public void testFunctionReturningPointerOnFunction() throws ParseException
    {
        assertEquals( "MyFunction( int )", extract( "void (*MyFunction( int ))( char, float ) {}" ) );
    }

    public void testParenthesisOperatorDefinition() throws ParseException
    {
        assertEquals( "MyClass::operator()( int )", extract( "void MyClass::operator()( int i ) {}" ) );
    }

    public void testFunctionInAnonymousNamespace() throws ParseException
    {
        assertEquals( "`anonymous-namespace'::MyFunction()", extract( "namespace { void MyFunction(); }" ) );
    }

    public void testMethodOfClassDefinedInFunction() throws ParseException
    {
        final String content = "void MyFunction() { class MyClass{ void MyMethod(); }; }";
        final AstTranslationUnit root = new Parser( new StringReader( content ) ).translation_unit();
        final Node node = root.jjtGetChild( 0 ).jjtGetChild( 2 ).jjtGetChild( 0 );
        final String actual = (String)node.jjtAccept( new FunctionNameExtractor(), null );
        // final String actual = (String)new FunctionNameExtractor().visit( node, null );
        assertEquals( "MyClass::MyMethod()", actual );
    }

    public void testMethodOfClassDefinedLocally() throws ParseException
    {
        final String content = "void MyFunction() { { class MyClass{ void MyMethod(); }; } }";
        final AstTranslationUnit root = new Parser( new StringReader( content ) ).translation_unit();
        final Node node = root.jjtGetChild( 0 ).jjtGetChild( 2 ).jjtGetChild( 0 );
        final String actual = (String)node.jjtAccept( new FunctionNameExtractor(), null );
        // final String actual = (String)new FunctionNameExtractor().visit( node, null );
        assertEquals( "MyClass::MyMethod()", actual );
    }
}
