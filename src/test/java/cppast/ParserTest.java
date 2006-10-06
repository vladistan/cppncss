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

package cppast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class ParserTest extends TestCase
{
    private void parse( final String data ) throws ParseException
    {
        new Parser( new StringReader( data ) ).translation_unit();
    }

    public void testTypedefType() throws ParseException
    {
        parse( "typedef MyClass T_MyClass;" );
    }

    public void testTypedefTypeInNamespace() throws ParseException
    {
        parse( "typedef std::vector T_Vector;" );
    }

    public void testTypedefTemplateType() throws ParseException
    {
        parse( "typedef vector< MyClass > T_MyClass;" );
    }

    public void testTypedefTemplateNamespacedType() throws ParseException
    {
        parse( "typedef vector< nm::MyClass > T_MyClass;" );
    }

    public void testTypedefTemplateTypeInNamespace() throws ParseException
    {
        parse( "typedef std::vector< MyClass > T_MyClass;" );
    }

    public void testTypedefTemplateTypeOfTemplateType() throws ParseException
    {
        parse( "typedef std::vector< boost::shared_ptr< MyClass > > T_MyClass;" );
    }

    public void testTypedefTemplateTypeOfTemplateParameter() throws ParseException
    {
        parse( "typedef typename MyClass::template MySubClass< MyType >::Member T_Type;" );
    }

    public void testTemplateClassMemberTypeVariable() throws ParseException
    {
        parse( "MyClass< MyType >::Member< MyOtherType >::AnotherMember i;" );
    }

    public void testVariableType() throws ParseException
    {
        parse( "vector v;" );
    }

    public void testVariableTypeInNamespace() throws ParseException
    {
        parse( "std::vector v;" );
    }

    public void testVariableOfTemplateType() throws ParseException
    {
        parse( "auto_ptr< MyClass > pMyClass;" );
    }

    public void testVariableTemplateNamespacedType() throws ParseException
    {
        parse( "auto_ptr< nm::MyClass > pMyClass_;" );
    }

    public void testVariableTemplateTypeInNamespace() throws ParseException
    {
        parse( "std::auto_ptr< MyClass > pMyClass_;" );
    }

    public void testVariableTemplateNamespacedTypeInNamespace() throws ParseException
    {
        parse( "std::auto_ptr< nm::MyClass > pMyClass_;" );
    }

    public void testTypedefTypename() throws ParseException
    {
        parse( "typedef typename T_Elements::iterator IT_Elements;" );
    }

    public void testTypenameQualifier() throws ParseException
    {
        parse( "const typename CIT_Elements it;" );
    }

    public void testMutableQualifier() throws ParseException
    {
        parse( "mutable float f;" );
    }

    public void testTemplateFunctionDeclaration() throws ParseException
    {
        parse( "template< typename T > void MyFunction( T& t );" );
    }

    public void testTemplateFunctionDefinition() throws ParseException
    {
        parse( "template< typename T > void MyFunction( T& t ) { t.Something(); }" );
    }

    public void testTemplateMethodDeclaration() throws ParseException
    {
        parse( "class MyClass{ template< typename T > void MyMethod( T& t ); };" );
    }

    public void testUserTypeDefaultTemplateParameterClassDefinition() throws ParseException
    {
        parse( "template< typename T = DefaultType > class MyClass {};" );
    }

    public void testBaseTypeDefaultTemplateParameterClassDefinition() throws ParseException
    {
        parse( "template< typename T = int > class MyClass {};" );
    }

    public void testTemplateSpecializedMethodDeclaration() throws ParseException
    {
        parse( "class MyClass{ template<> void MyMethod< MyType >(); };" );
    }

    public void testTemplateSpecializedClassDefinition() throws ParseException
    {
        parse( "template<> class MyClass< MyType > : public MySuperClass {};" );
    }

    public void testTemplateSpecializedNamespacedClassDefinition() throws ParseException
    {
        parse( "template<> class my_namespace::MyClass< MyType > : public MySuperClass {};" );
    }

    public void testTemplateMethodDefinition() throws ParseException
    {
        parse( "class MyClass { template< typename T > void MyMethod() {} };" );
    }

    public void testTemplateSpecializedClassDeclaration() throws ParseException
    {
        parse( "template<> class MyClass< MyType > {};" );
    }

    public void testTemplateSpecializedSubClassTemplateDeclaration() throws ParseException
    {
        parse( "class MyClass{ template< typename T > class MySubClass< const T > {}; };" );
    }

    public void testTemplateSpecializedSubClassDeclaration() throws ParseException
    {
        parse( "class MyClass { template<> class MySubClass<true> {}; };" );
    }

    public void testTemplateClassInstanciation() throws ParseException
    {
        parse( "MyClass< int > instance;" );
    }

    public void testTypedTemplateClassInstanciation() throws ParseException
    {
        parse( "MyClass< MyType > instance;" );
    }

    public void testSpecializedTemplateClassInstanciation() throws ParseException
    {
        parse( "MyClass< 0 > instance;" );
    }

    public void testTemplateSpecializedClassInstanciation() throws ParseException
    {
        parse( "MyClass<> instance;" );
    }

    public void testTemplateMemberInitializer() throws ParseException
    {
        parse( "MyClass::MyClass() : my_namespace::MyType< AnotherType >( data ) {}" );
    }

    public void testTemplateTypeClassDeclaration() throws ParseException
    {
        parse( "template< int > class MyClass {};" );
    }

    public void testSpecializedTemplateTypeClassDeclaration() throws ParseException
    {
        parse( "template<> class MyClass< 0 > {};" );
    }

    public void testSpecializedTemplateMemberPointerClassDeclaration() throws ParseException
    {
        parse( "class MyClass< void (MyOtherClass::*)() > {};" );
    }

    public void testTemplateMethodOfTemplateClassSeparateDefinition() throws ParseException
    {
        parse( "template< typename T1 > class MyClass { template< typename T2 > void MyMethod(); };" + '\n'
                + "template< typename T1 > template< typename T2 > void MyClass< T1 >::MyMethod() {}" );
    }

    public void testTemplateFunctionCall() throws ParseException
    {
        parse( "void MyFunction() { process< char* >(); }" );
        parse( "void MyFunction() { process< int >( 2 ); }" );
        parse( "void MyFunction() { stream.read( function< char* >( pData ) ); }" );
        parse( "void MyFunction() { process< 1 >(); }" );
        parse( "void MyFunction() { if( i < 0 && j > 0 ); }" );
        parse( "void MyFunction() { process< (1 > 2) >(); }" );
    }

    public void testTemplateConstructorDefinition() throws ParseException
    {
        parse( "class MyClass { template< typename T > MyClass(); };" );
    }

    public void testTemplateConstructorSeparateDefinition() throws ParseException
    {
        parse( "class MyClass {}; template< typename T > MyClass< T >::MyClass() {}" );
    }

    public void testTemplateMethodCallWithTemplateParameter() throws ParseException
    {
        parse( "void MyFunction() { data.template Method< MyType >( 0 ); }" );
    }

    public void testTemplateStaticMethodCallWithTemplateParameter() throws ParseException
    {
        parse( "void MyFunction() { MyClass::template Method< MyType >( 0 ); }" );
    }

    public void testUnsupportedTemplateFunctionCallThrowsException()
    {
        try
        {
            parse( "void MyFunction() { process< 1 < 2 >(); }" );
        }
        catch( ParseException e )
        {
            return;
        }
        fail( "must throw an exception" );
    }

    public void testUnsupportedTemplateFunctionCall2ThrowsException()
    {
        try
        {
            parse( "void MyFunction() { process< 1 ? 2 : 3 >(); }" );
        }
        catch( ParseException e )
        {
            return;
        }
        fail( "must throw an exception" );
    }

    public void testBuiltInTypeCast() throws ParseException
    {
        parse( "void MyFunction() { char( 1 ); }" );
    }

    public void testUnsignedBuiltInTypeCast() throws ParseException
    {
        parse( "void MyFunction() { unsigned char( 1 ); }" );
    }

    public void testSignedBuiltInTypeCast() throws ParseException
    {
        parse( "void MyFunction() { signed char( 1 ); }" );
    }

    public void testUnsignedLongIntVariableDeclaration() throws ParseException
    {
        parse( "unsigned long int i;" );
    }

    public void testUnsignedLongIntTypeCast() throws ParseException
    {
        parse( "void MyFunction() { unsigned long int( 1 ); }" );
    }

    public void testTypeCastAssignmentExpression() throws ParseException
    {
        parse( "void MyFunction() { int( i = 0 ); }" );
    }

    public void testDestructorDefinitionWithoutClassDeclarationIsValid() throws ParseException
    {
        parse( "MyClass::~MyClass() {}" );
    }

    public void testTildeUnaryOperator() throws ParseException
    {
        parse( "int i = 0; int j = ~i;" ); // FIXME check not a destructor
    }

    public void testMethodCallWithinVariableInitialization() throws ParseException
    {
        parse( "void MyMethod() { int data( something.Method() ); }" );
    }

    public void testUsingSeveralNamespaces() throws ParseException
    {
        final Parser parser = new Parser( new StringReader( "namespace my_namespace { class MyClass{}; }" ) );
        parser.translation_unit();
        parser.ReInit( new StringReader( "using namespace my_namespace; using namespace another_namespace;"
                + "MyClass::~MyClass() {}" ) );
        parser.translation_unit();
    }

    public void testUsingNestedNamespacesFullPath() throws ParseException
    {
        final Parser parser = new Parser( new StringReader(
                "namespace my_namespace { namespace inner { class MyClass{}; } }" ) );
        parser.translation_unit();
        parser.ReInit( new StringReader( "using namespace my_namespace::inner; MyClass::~MyClass() {}" ) );
        parser.translation_unit();
    }

    public void testUsingNestedNamespacesHalfPath() throws ParseException
    {
        final Parser parser = new Parser( new StringReader(
                "namespace my_namespace { namespace inner { class MyClass{}; } }" ) );
        parser.translation_unit();
        parser.ReInit( new StringReader( "using namespace my_namespace; inner::MyClass::~MyClass() {}" ) );
        parser.translation_unit();
    }

    public void testUsingNamespaceInFunction() throws ParseException
    {
        parse( "void MyFunction() { using namespace my_namespace; }" );
    }

    public void testUsingClass() throws ParseException
    {
        parse( "using my_namespace::MyClass;" );
    }

    public void testMemberUsingClass() throws ParseException
    {
        parse( "class MyClass { using my_namespace::i; };" );
    }

    public void testFunctionUsingClass() throws ParseException
    {
        parse( "void MyFunction() { using my_namespace::i; }" );
    }

    public void testConstructorDefinitionThrowSpecification() throws ParseException
    {
        parse( "class MyClass{}; MyClass::MyClass() throw() {}" );
    }

    public void testNamespacedConstructorDefinition() throws ParseException
    {
        parse( "my_namespace::MyClass::MyClass() {}" );
    }

    public void testConversionOperator() throws ParseException
    {
        parse( "class MyClass{ operator const char* const() const {} };" );
    }

    public void testConversionOperatorOfTemplateClassSeparateDefinition() throws ParseException
    {
        parse( "template< typename T > MyClass<T>::operator bool() {}" );
    }

    public void testConstConversionOperatorSeparateDefinition() throws ParseException
    {
        parse( "MyClass::operator bool() const {}" );
    }

    public void testInlineOperatorSeparateDefinition() throws ParseException
    {
        parse( "inline MyClass::operator bool() {}" );
    }

    public void testInlinePureVirtualOperatorDeclaration() throws ParseException
    {
        parse( "class MyClass{ inline virtual operator bool() = 0; };" );
    }

    public void testFunctionReturningConstPointer() throws ParseException
    {
        parse( "const char* const MyFunction() { return 0; };" );
    }

    public void testMemberPointerWithAnonymousParameter() throws ParseException
    {
        parse( "typedef void (T::*M)( P& );" );
    }

    public void testMemberPointerWithParameterByCopy() throws ParseException
    {
        parse( "typedef void (T::*M)( P p );" );
    }

    public void testInnerTemplateClass() throws ParseException
    {
        parse( "class MyClass { template< typename T > class MyInnerClass {}; };" );
    }

    public void testSingleLineCommentAtEndOfFile() throws ParseException
    {
        parse( "// single line comment at end of file" );
    }

    public void testPreProcessorDirective() throws ParseException
    {
        parse( "#endif" + '\n' + ";" );
    }

    public void testPreProcessorDirectiveAtEndOfFile() throws ParseException
    {
        parse( "#endif" );
    }

    public void testPreProcessorOnSeveralLinesAtEndOfFile() throws ParseException
    {
        parse( "#define DEF anything \\" + '\n' + "should be escaped" );
    }

    public void testCommentAtEndOfFile() throws ParseException
    {
        parse( "/* should be escaped */" );
    }

    public void testEnumWithoutCommaAfterLastValue() throws ParseException
    {
        parse( "enum MyEnum { my_value };" );
    }

    public void testEnumWithTwoValues() throws ParseException
    {
        parse( "enum MyEnum { my_value, my_other_value };" );
    }

    public void testEnumWithCommaAfterLastValue() throws ParseException
    {
        parse( "enum MyEnum { my_value, };" );
    }

    public void testEnumWithTwoValuesWithCommaAfterLastValue() throws ParseException
    {
        parse( "enum MyEnum { my_value, my_other_value, };" );
    }

    public void testEmptyEnum() throws ParseException
    {
        parse( "enum MyEnum {};" );
    }

    public void testEmptyAnonymousEnum() throws ParseException
    {
        parse( "enum {};" );
    }

    public void testVariableParenthesisInitialization() throws ParseException
    {
        parse( "int var( 3 );" );
    }

    public void testNamespaceAliasingDeclaration() throws ParseException
    {
        parse( "namespace bfs = boost::filesystem;" );
    }

    public void testBitFieldDeclaration() throws ParseException
    {
        parse( "class MyClass { int my_value : 1; };" );
    }

    public void testStaticConstMemberDataInPlaceInitialization() throws ParseException
    {
        parse( "class MyClass { static const int data = 0x80; };" );
    }

    public void testExplicitConstructorDeclaration() throws ParseException
    {
        parse( "class MyClass { explicit MyClass(); };" );
    }

    public void testExplicitConstructorDefinition() throws ParseException
    {
        parse( "class MyClass { explicit MyClass() : data_( 0 ) {}; };" );
    }

    public void testPureVirtualDestructorDeclaration() throws ParseException
    {
        parse( "class MyClass { virtual ~MyClass() = 0; };" );
    }

    public void testMemberDataList() throws ParseException
    {
        parse( "class MyClass{ float v1, v2; };" );
    }

    public void testReturnParenthezizedExpression() throws ParseException
    {
        parse( "int MyFunction() { return (stat); }" );
    }

    public void testReturnParenthezizedComplexExpression() throws ParseException
    {
        parse( "int MyFunction() { return (int)(2 * (stat)); }" );
    }

    public void testSizeOfBaseType() throws ParseException
    {
        parse( "void MyFunction() { sizeof( char ); }" );
    }

    public void testSizeOfUserType() throws ParseException
    {
        parse( "void MyFunction() { sizeof( MyType ); }" );
    }

    public void testSizeOfUserTypeReference() throws ParseException
    {
        parse( "void MyFunction() { sizeof( MyType& ); }" );
    }

    public void testSizeOfAnExpression() throws ParseException
    {
        parse( "void MyFunction() { sizeof( pValue->m ); }" );
    }

    public void testSizeOfFunctionCall() throws ParseException
    {
        parse( "void MyFunction() { sizeof( data.Method( 0 ) ); }" );
    }

    public void testSizeOfAssignment() throws ParseException
    {
        parse( "void MyFunction() { sizeof( i = 0 ); }" );
    }

    public void testSizeOfCommaExpression() throws ParseException
    {
        parse( "void MyFunction() { sizeof( 0, 1 ); }" );
    }

    public void testSizeOfWithoutParenthesis() throws ParseException
    {
        parse( "void MyFunction() { sizeof i; }" );
        parse( "void MyFunction() { sizeof 9; }" );
        parse( "void MyFunction() { sizeof -3; }" );
    }

    public void testGlobalScopeOverrideFunctionCall() throws ParseException
    {
        parse( "void MyFunction() { ::Call(); }" );
    }

    public void testStringJuxtaposition() throws ParseException // FIXME check naming
    {
        parse( "void MyFunction() { Call( \"abc\" \"def\" ); }" );
    }

    public void testInitializedArrayLastElementFollowedByComma() throws ParseException
    {
        parse( "void MyFunction() { static int data[] = { 0, 1, }; }" );
    }

    public void testIfStatementWithAssignment() throws ParseException
    {
        parse( "void MyFunction() { if( int i = 0 ); }" );
    }

    public void testConditionalExpression() throws ParseException
    {
        parse( "void MyFunction() { i ? x = 0 : x = 1; }" );
    }

    public void testDestructorWithExceptionSpecificationDeclaration() throws ParseException
    {
        parse( "class MyClass { ~MyClass() throw(); };" );
    }

    public void testDestructorWithExplicitVoidParameterDeclaration() throws ParseException
    {
        parse( "class MyClass { ~MyClass( void ); };" );
    }

    public void testMethodWithExceptionSpecificationDeclaration() throws ParseException
    {
        parse( "class MyClass { void MyMethod() throw(); };" );
    }

    public void testTypeidOfUserTypeReference() throws ParseException
    {
        parse( "void MyFunction() { typeid( MyType& ); }" );
    }

    public void testTypeidOfExpression() throws ParseException
    {
        parse( "void MyFunction() { typeid( data.Method( 0 ) ); }" );
    }

    public void testTypeidOfCommaExpression() throws ParseException
    {
        parse( "void MyFunction() { typeid( i, j ); }" );
    }

    public void testConditionWithFunctionCall() throws ParseException
    {
        parse( "void MyFunction() { if( data.Method( 0 ) ); }" );
    }

    public void testTypeidOfAssignmentExpression() throws ParseException
    {
        parse( "void MyFunction() { typeid( i = 0 ); }" );
    }

    public void testTypeidOfThis() throws ParseException
    {
        parse( "void MyFunction() { typeid( this ); }" );
    }

    public void testTypeidOfContentOfThis() throws ParseException
    {
        parse( "void MyFunction() { typeid( *this ); }" );
    }

    public void testCanRetrieveNameOfTypeidResult() throws ParseException
    {
        parse( "void MyFunction() { typeid( MyClass ).name(); }" );
    }

    public void testMutableMember() throws ParseException
    {
        parse( "class MyClass { mutable int i; };" );
    }

    public void testVolatileMember() throws ParseException
    {
        parse( "class MyClass { volatile int i; };" );
    }

    public void testVolatileVariable() throws ParseException
    {
        parse( "volatile int i;" );
    }

    public void testConstUnsignedIntVariable() throws ParseException
    {
        parse( "const unsigned int i;" );
    }

    public void testVolatileConstUnsignedIntVariable() throws ParseException
    {
        parse( "volatile const unsigned int i;" );
    }

    public void testFriendClassDeclaration() throws ParseException
    {
        parse( "friend MyClass;" );
    }

    public void testFriendMemberClassDeclaration() throws ParseException
    {
        parse( "class MyClass { friend AnotherClass; };" );
    }

    public void testVolatileMethodDeclaration() throws ParseException
    {
        parse( "class MyClass { void MyMethod() volatile; };" );
    }

    public void testVolatileMethodDefinition() throws ParseException
    {
        parse( "class MyClass { void MyMethod() volatile {} };" );
    }

    public void testVolatilePointerTypeConversionOperatorDeclaration() throws ParseException
    {
        parse( "class MyClass { operator AnotherClass* volatile(); };" );
    }

    public void testVolatileConversionOperatorDeclaration() throws ParseException
    {
        parse( "class MyClass { operator int() volatile; };" );
    }

    public void testInfiniteForLoop() throws ParseException
    {
        parse( "void MyFunction() { for(;;); }" );
    }

    public void testAnonymousEnumVariableDeclaration() throws ParseException
    {
        parse( "enum {} MyEnum;" );
    }

    public void testAnonymousClassVariableDeclaration() throws ParseException
    {
        parse( "class {} MyClass;" );
    }

    public void testAnonymousClassClassMember() throws ParseException
    {
        parse( "class MyClass{ class {}; };" );
    }

    public void testForWithFunctionCallsWithSeveralParametersAsInitialization() throws ParseException
    {
        parse( "void MyFunction() { for( call( i, j );; ); }" );
    }

    public void testTemplateFriendClassMemberDeclaration() throws ParseException
    {
        parse( "class MyClass{ template< typename T > friend class AnotherClass; };" );
    }

    public void testTemplateClassMemberDeclaration() throws ParseException
    {
        parse( "class MyClass{ template< typename T > friend class AnotherClass; };" );
    }

    public void testClassForwardDeclarationMember() throws ParseException
    {
        parse( "class MyClass{ class AnotherClass; };" );
    }

    public void testFriendClassForwardDeclarationMember() throws ParseException
    {
        parse( "class MyClass{ friend class AnotherClass; };" );
    }

    public void testTemplateMemberVariableOfTemplateType() throws ParseException
    {
        parse( "template< typename T > class MyClass{ typename MyType< T > t; };" );
    }

    public void testClassForwardDeclaration() throws ParseException
    {
        parse( "class MyClass;" );
    }

    public void testPureVirtualDestructorWithImplementation() throws ParseException
    {
        parse( "class MyClass{ virtual ~MyClass() = 0 {} };" );
    }

    public void testPureVirtualMethodWithImplementation() throws ParseException
    {
        parse( "class MyClass{ virtual void MyMethod() = 0 {} };" );
    }

    public void testPlacementNew() throws ParseException
    {
        parse( "void MyFunction() { new (where) MyClass(); }" );
    }

    public void testNewWithoutParameters() throws ParseException
    {
        parse( "void MyFunction() { new MyClass; }" );
    }

    public void testNewType() throws ParseException
    {
        parse( "void MyFunction() { new MyClass(); }" );
    }

    public void testNewTypeWithParameters() throws ParseException
    {
        parse( "void MyFunction() { new MyClass( 9, data.Method( i, j - 1 ) ); }" );
    }

    public void testAsmExpression() throws ParseException
    {
        parse( "void MyFunction() { asm( \"nop\" ); }" );
    }

    public void testThrowWithoutAnException() throws ParseException
    {
        parse( "void MyFunction() { throw; }" );
    }

    public void testThrowAnException() throws ParseException
    {
        parse( "void MyFunction() { throw e; }" );
    }

    public void testEmptyStatement() throws ParseException
    {
        parse( "void MyFunction() { ; }" );
    }

    public void testTemplateForwardDeclarationDoesNotRegisterTemplateTypeAsType() throws ParseException
    {
        parse( "template< typename T > Type1; template< typename T > Type2;" );
    }

    public void testOneVariableArgumentFunctionDeclaration() throws ParseException
    {
        parse( "void MyFunction( ... );" );
    }

    public void testVariableArgumentFunctionDeclaration() throws ParseException
    {
        parse( "void MyFunction( int x, ...);" );
    }

    public void testOneVariableArgumentFunctionDefinition() throws ParseException
    {
        parse( "void MyFunction( ... ) {}" );
    }

    public void testVariableArgumentFunctionDefinition() throws ParseException
    {
        parse( "void MyFunction( int x, ... ) {}" );
    }

    public void testIntegerParameterInTemplateDeclaration() throws ParseException
    {
        parse( "template< typename T, int n > void MyFunction();" );
    }

    public void testVoidDefaultTemplateParameterDeclaration() throws ParseException
    {
        parse( "template< typename T = void > void MyFunction();" );
    }

    public void testVoidPointerDefaultTemplateParameterDeclaration() throws ParseException
    {
        parse( "template< typename T = void* > void MyFunction();" );
    }

    public void testHexadecimalEscapeSequenceInString() throws ParseException
    {
        parse( "char* str = \"\\x02\\x03\";" );
    }

    public void testBackslashAtEndOfLineIsIgnored() throws ParseException
    {
        parse( "int i\\\n; int j\\\r;" );
    }

    public void testBackslashAtEndOfLineInStringIsIgnored() throws ParseException
    {
        parse( "char* str = \"abc\\\ndef\"; char* str2 = \"abc\\\rdef\"; char* str3 = \"abc\\\r\ndef\";" );
    }

    public void testCp1252NotAsciiCharacterInCommentIsValid() throws ParseException
    {
        parse( "// ’ € Œ" );
    }

    public void testCp1252NotAsciiCharacterInMultiCommentIsValid() throws ParseException
    {
        parse( "/* ’ € Œ */" );
    }

    public void testExternFunctionDefinitionIsValid() throws ParseException
    {
        parse( "extern \"C\" void MyFunction() {}" );
    }

    public void testTMP() throws IOException, ParseException
    {
        final Parser parser = new Parser( new StringReader( "" ) );
        load( parser, "TMP.h" );
        load( parser, "TMP.cpp" );
    }

    private void load( final Parser parser, final String name ) throws IOException, ParseException
    {
        final URL resource = ParserTest.class.getClassLoader().getResource( name );
        if( resource == null )
            throw new IOException( "resource not found : " + name );
        parser.ReInit( new BufferedReader( new FileReader( resource.getFile() ) ) );
        parser.translation_unit();
    }
}
