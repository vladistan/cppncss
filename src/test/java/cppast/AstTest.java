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

package cppast;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class AstTest extends TestCase
{
    private Tree tree;

    private final class Tree
    {
        private final StringWriter buffer = new StringWriter();
        private final PrintWriter writer = new PrintWriter( buffer );

        public void add( final String node )
        {
            writer.println( node );
        }

        public String toString()
        {
            return buffer.toString();
        }
    }

    private String parse( final String content ) throws ParseException
    {
        final StringWriter w = new StringWriter();
        PrintWriter writer = new PrintWriter( w );
        final Node root = new Parser( new StringReader( content ) ).translation_unit();
        dump( root, writer, 0 );
        return w.toString();
    }

    private String parseExpression( final String expression ) throws ParseException
    {
        return parse( "void f() { " + expression + "; }" );
    }

    private void dump( final Node node, final PrintWriter writer, final int level )
    {
        for( int i = 0; i < level; ++i )
            writer.append( ' ' );
        writer.println( node.toString() );
        for( int i = 0; i < node.jjtGetNumChildren(); ++i )
            dump( node.jjtGetChild( i ), writer, level + 1 );
    }

    private void addExpressionPrefix()
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
    }

    protected void setUp()
    {
        tree = new Tree();
    }

    public void testFunctionDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "void MyFunction() {}" ) );
    }

    public void testFunctionWithOneParameter() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "   Parameter" );
        tree.add( "    ParameterType" );
        tree.add( "    FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "void MyFunction( int i ) {}" ) );
    }

    public void testFunctionWithTwoParameters() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "   Parameter" );
        tree.add( "    ParameterType" );
        tree.add( "    FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "   Parameter" );
        tree.add( "    ParameterType" );
        tree.add( "    FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "void MyFunction( int i, const float& p ) {}" ) );
    }

    public void testFunctionWithClassDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.add( "   DeclarationStatement" );
        tree.add( "    ClassDefinition" );
        assertEquals( tree.toString(), parse( "void MyFunction() { class MyClass {}; }" ) );
    }

    public void testFunctionDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" );
        tree.add( "  FunctionParameters" );
        assertEquals( tree.toString(), parse( "void MyFunction();" ) );
    }

    public void testMethodDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDefinition" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "    FunctionBody" );
        assertEquals( tree.toString(), parse( "class MyClass { void MyMethod() {} };" ) );
    }

    public void testMethodDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        assertEquals( tree.toString(), parse( "class MyClass { void MyMethod(); };" ) );
    }

    public void testMethodSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "void MyClass::MyMethod() {}" ) );
    }

    public void testConstructorDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   ConstructorDefinition" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "    FunctionBody" );
        assertEquals( tree.toString(), parse( "class MyClass { MyClass() {} };" ) );
    }

    public void testConstructorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   ConstructorDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        assertEquals( tree.toString(), parse( "class MyClass { MyClass(); };" ) );
    }

    public void testConstructorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " ConstructorDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "MyClass::MyClass() {}" ) );
    }

    public void testDestructorDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   DestructorDefinition" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "    FunctionBody" );
        assertEquals( tree.toString(), parse( "class MyClass { ~MyClass() {} };" ) );
    }

    public void testDestructorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   DestructorDeclaration" );
        tree.add( "    FunctionName" );
        assertEquals( tree.toString(), parse( "class MyClass { ~MyClass(); };" ) );
    }

    public void testDestructorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " DestructorDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "MyClass::~MyClass() {}" ) );
    }

    public void testEqualityOperatorDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDefinition" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "     Parameter" );
        tree.add( "      ParameterType" );
        tree.add( "      FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "    FunctionBody" );
        assertEquals( tree.toString(), parse( "class MyClass { bool operator==( const MyClass& ) {} };" ) );
    }

    public void testEqualityOperatorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "     Parameter" );
        tree.add( "      ParameterType" );
        tree.add( "      FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "class MyClass { bool operator==( const MyClass& ); };" ) );
    }

    public void testEqualityOperatorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "   Parameter" );
        tree.add( "    ParameterType" );
        tree.add( "    FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "bool MyClass::operator==( const MyClass& ) {}" ) );
    }

    public void testConversionOperatorDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDefinition" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.add( "    FunctionBody" );
        assertEquals( tree.toString(), parse( "class MyClass { operator const char*() const {} };" ) );
    }

    public void testConversionOperatorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        assertEquals( tree.toString(), parse( "class MyClass { operator const char*() const; };" ) );
    }

    public void testConversionOperatorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        assertEquals( tree.toString(), parse( "MyClass::operator const char*() const {}" ) );
    }

    public void testIdExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i" ) );
    }

    public void testScopedIdExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    IdExpression" );
        assertEquals( tree.toString(), parseExpression( "MyClass::i" ) );
    }

    public void testConstantExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ConstantExpression" );
        assertEquals( tree.toString(), parseExpression( "42" ) );
        assertEquals( tree.toString(), parseExpression( "\"abc\"" ) );
        assertEquals( tree.toString(), parseExpression( "\"abc\" \"def\"" ) );
    }

    public void testLogicalAndExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    LogicalAndExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j && k" ) );
    }

    public void testLogicalOrExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    LogicalOrExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j || k" ) );
    }

    public void testConditionalExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ConditionalExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j ? k : l;" ) );
    }

    public void testAssignmentExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    AssignmentExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j = k" ) );
    }

    public void testThrowExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ThrowExpression" );
        assertEquals( tree.toString(), parseExpression( "throw" ) );
    }

    public void testThrowExpressionWithException() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ThrowExpression" );
        tree.add( "     IdExpression" ); // FIXME IdExpression ?
        assertEquals( tree.toString(), parseExpression( "throw my_exception()" ) );
    }

    public void testInclusiveOrExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    InclusiveOrExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j | k" ) );
    }

    public void testExclusiveOrExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ExclusiveOrExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j ^ k" ) );
    }

    public void testAndExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    AndExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "j & k" ) );
    }

    public void testEqualityExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    EqualityExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i == j" ) );
        assertEquals( tree.toString(), parseExpression( "i != j" ) );
    }

    public void testRelationalExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    RelationalExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i < j" ) );
        assertEquals( tree.toString(), parseExpression( "i > j" ) );
        assertEquals( tree.toString(), parseExpression( "i <= j" ) );
        assertEquals( tree.toString(), parseExpression( "i >= j" ) );
    }

    public void testShiftExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    ShiftExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i << j" ) );
        assertEquals( tree.toString(), parseExpression( "i >> j" ) );
    }

    public void testAdditiveExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    AdditiveExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i + j" ) );
        assertEquals( tree.toString(), parseExpression( "i - j" ) );
    }

    public void testMultiplicativeExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    MultiplicativeExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i * j" ) );
        assertEquals( tree.toString(), parseExpression( "i / j" ) );
        assertEquals( tree.toString(), parseExpression( "i % j" ) );
    }

    public void testPointerToMemberExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    PointerToMemberExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "i .* j" ) );
        assertEquals( tree.toString(), parseExpression( "i ->* j" ) );
    }

    public void testCastExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    CastExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "(int) i" ) );
        assertEquals( tree.toString(), parseExpression( "(MyType) i" ) );
    }

    public void testUnaryExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    UnaryExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "++ i" ) );
        assertEquals( tree.toString(), parseExpression( "-- i" ) );
        assertEquals( tree.toString(), parseExpression( "& i" ) );
        assertEquals( tree.toString(), parseExpression( "* i" ) );
        assertEquals( tree.toString(), parseExpression( "+ i" ) );
        assertEquals( tree.toString(), parseExpression( "- i" ) );
        assertEquals( tree.toString(), parseExpression( "+ i" ) );
        assertEquals( tree.toString(), parseExpression( "~ i" ) );
        assertEquals( tree.toString(), parseExpression( "! i" ) );
        assertEquals( tree.toString(), parseExpression( "sizeof i" ) );
    }

    public void testUnarySizeofExpression() throws ParseException // FIXME should be regular unary expression
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    UnaryExpression" );
        assertEquals( tree.toString(), parseExpression( "sizeof( i )" ) ); // FIXME i considered as type_id()
    }

    public void testFunctionCallExpression() throws ParseException // TODO
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    IdExpression" ); // FIXME FunctionCallExpression ?
        assertEquals( tree.toString(), parseExpression( "i()" ) );
    }

    public void testPostfixExpression() throws ParseException // TODO
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    PostfixExpression" );
        tree.add( "     IdExpression" );
        tree.add( "     PostfixExpression" ); // FIXME ?!
        assertEquals( tree.toString(), parseExpression( "i ++" ) );
        assertEquals( tree.toString(), parseExpression( "i --" ) );
    }

    public void testThisIsPrimaryExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    PrimaryExpression" );
        assertEquals( tree.toString(), parseExpression( "this" ) );
    }

    public void testParenthizedExpressionIsPrimaryExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    PrimaryExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "( i )" ) );
    }

    public void testNewExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    NewExpression" );
        assertEquals( tree.toString(), parseExpression( "::new MyType" ) );
        assertEquals( tree.toString(), parseExpression( "new MyType" ) );
        assertEquals( tree.toString(), parseExpression( "new MyType()" ) );
    }

    public void testComplexNewExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    NewExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "new MyType( i )" ) );
        assertEquals( tree.toString(), parseExpression( "new (i) MyType" ) );
        assertEquals( tree.toString(), parseExpression( "new MyType[i]" ) );
    }

    public void testDeleteExpression() throws ParseException
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    DeleteExpression" );
        tree.add( "     IdExpression" );
        assertEquals( tree.toString(), parseExpression( "::delete i" ) );
        assertEquals( tree.toString(), parseExpression( "delete i" ) );
        assertEquals( tree.toString(), parseExpression( "delete[] i" ) );
    }

    public void testTypeIdExpression() throws ParseException // FIXME consider as function call ?
    {
        addExpressionPrefix();
        tree.add( "   ExpressionStatement" );
        tree.add( "    TypeIdExpression" );
        assertEquals( tree.toString(), parseExpression( "typeid( int )" ) );
        assertEquals( tree.toString(), parseExpression( "typeid( i )" ) );
        tree.add( "     FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parseExpression( "typeid( MyType& )" ) );
    }

    public void testClassDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        assertEquals( tree.toString(), parse( "class {};" ) );
        assertEquals( tree.toString(), parse( "class MyClass {};" ) );
        assertEquals( tree.toString(), parse( "struct {};" ) );
        assertEquals( tree.toString(), parse( "struct MyStruct {};" ) );
        assertEquals( tree.toString(), parse( "union {};" ) );
        assertEquals( tree.toString(), parse( "union MyUnion {};" ) );
    }

    public void testClassDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDeclaration" );
        assertEquals( tree.toString(), parse( "class MyClass;" ) );
        assertEquals( tree.toString(), parse( "struct MyStruct;" ) );
        assertEquals( tree.toString(), parse( "union MyUnion;" ) );
    }

    public void testEnumDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        assertEquals( tree.toString(), parse( "enum {};" ) );
        assertEquals( tree.toString(), parse( "enum MyEnum {};" ) );
    }

    public void testClassVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "class MyClass {} c;" ) );
    }

    public void testAnonymousClassVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "class {} c;" ) );
    }

    public void testEnumVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "enum MyEnum {} e;" ) );
    }

    public void testAnonymousEnumVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "enum {} e;" ) );
    }

    public void testVariableExternalDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        assertEquals( tree.toString(), parse( "int i;" ) );
    }

    public void testVariableExternalDeclarationAndInitialization() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  ConstantExpression" );
        assertEquals( tree.toString(), parse( "int i = 0;" ) );
    }

    public void testClassMemberVariableDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "   MemberDeclaration" );
        assertEquals( tree.toString(), parse( "class MyClass { int i; };" ) );
    }

    public void testClassDefinitionWithOneMethodDeclarationWithinFunction() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.add( "   DeclarationStatement" );
        tree.add( "    ClassDefinition" );
        tree.add( "     FunctionDeclaration" );
        tree.add( "      FunctionName" );
        tree.add( "      FunctionParameters" );
        assertEquals( tree.toString(), parse( "void MyFunction() { class MyClass { void MyMethod(); }; }" ) );
    }

    public void testUsingNamespace() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        assertEquals( tree.toString(), parse( "using namespace std;" ) );
    }

    public void testUsingNamespaceType() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        assertEquals( tree.toString(), parse( "using namespace std::vector;" ) );
    }
}
