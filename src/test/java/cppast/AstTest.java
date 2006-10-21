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
    private Expression expression;

    private final class Tree
    {
        private final StringWriter buffer = new StringWriter();
        private final PrintWriter writer = new PrintWriter( buffer );

        public void add( final String node )
        {
            writer.println( node );
        }

        public void parse( final String content ) throws ParseException
        {
            final StringWriter writer = new StringWriter();
            final Node root = new Parser( new StringReader( content ) ).translation_unit();
            dump( root, new PrintWriter( writer ), 0 );
            assertEquals( buffer.toString(), writer.toString() );
        }

        private void dump( final Node node, final PrintWriter writer, final int level )
        {
            for( int i = 0; i < level; ++i )
                writer.append( ' ' );
            writer.println( node.toString() );
            for( int i = 0; i < node.jjtGetNumChildren(); ++i )
                dump( node.jjtGetChild( i ), writer, level + 1 );
        }
    }

    private final class Expression
    {
        private final Tree tree = new Tree();

        public Expression()
        {
            tree.add( "TranslationUnit" );
            tree.add( " FunctionDefinition" );
            tree.add( "  FunctionName" );
            tree.add( "  FunctionParameters" );
            tree.add( "  FunctionBody" );
        }

        public void add( final String node )
        {
            tree.add( "   " + node );
        }

        public void parse( final String content ) throws ParseException
        {
            tree.parse( "void f() { " + content + "; }" );
        }
    }

    protected void setUp()
    {
        tree = new Tree();
        expression = new Expression();
    }

    public void testFunctionDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.parse( "void MyFunction() {}" );
    }

    public void testFunctionWithOneParameterDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "   Parameter" );
        tree.add( "    ParameterType" );
        tree.add( "    FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  FunctionBody" );
        tree.parse( "void MyFunction( int i ) {}" );
    }

    public void testFunctionWithTwoParametersDefinition() throws ParseException
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
        tree.parse( "void MyFunction( int i, const float& p ) {}" );
    }

    public void testFunctionWithInnerClassDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.add( "   DeclarationStatement" );
        tree.add( "    ClassDefinition" );
        tree.parse( "void MyFunction() { class MyClass {}; }" );
    }

    public void testFunctionDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" );
        tree.add( "  FunctionParameters" );
        tree.parse( "void MyFunction();" );
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
        tree.parse( "class MyClass { void MyMethod() {} };" );
    }

    public void testMethodDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.parse( "class MyClass { void MyMethod(); };" );
    }

    public void testMethodSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.parse( "void MyClass::MyMethod() {}" );
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
        tree.parse( "class MyClass { MyClass() {} };" );
    }

    public void testConstructorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   ConstructorDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.parse( "class MyClass { MyClass(); };" );
    }

    public void testConstructorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " ConstructorDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.parse( "MyClass::MyClass() {}" );
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
        tree.parse( "class MyClass { ~MyClass() {} };" );
    }

    public void testDestructorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   DestructorDeclaration" );
        tree.add( "    FunctionName" );
        tree.parse( "class MyClass { ~MyClass(); };" );
    }

    public void testDestructorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " DestructorDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.parse( "MyClass::~MyClass() {}" );
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
        tree.parse( "class MyClass { bool operator==( const MyClass& ) {} };" );
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
        tree.parse( "class MyClass { bool operator==( const MyClass& ); };" );
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
        tree.parse( "bool MyClass::operator==( const MyClass& ) {}" );
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
        tree.parse( "class MyClass { operator const char*() const {} };" );
    }

    public void testConversionOperatorDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionDeclaration" );
        tree.add( "    FunctionName" );
        tree.add( "    FunctionParameters" );
        tree.parse( "class MyClass { operator const char*() const; };" );
    }

    public void testConversionOperatorSeparateDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " FunctionDefinition" );
        tree.add( "  FunctionName" );
        tree.add( "  FunctionParameters" );
        tree.add( "  FunctionBody" );
        tree.parse( "MyClass::operator const char*() const {}" );
    }

    public void testIdExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " IdExpression" );
        expression.parse( "i" );
    }

    public void testScopedIdExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " IdExpression" );
        expression.parse( "MyClass::i" );
    }

    public void testConstantExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ConstantExpression" );
        expression.parse( "42" );
        expression.parse( "\"abc\"" );
        expression.parse( "\"abc\" \"def\"" );
    }

    public void testLogicalAndExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " LogicalAndExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j && k" );
    }

    public void testLogicalOrExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " LogicalOrExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j || k" );
    }

    public void testConditionalExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ConditionalExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j ? k : l;" );
    }

    public void testAssignmentExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " AssignmentExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j = k" );
    }

    public void testThrowExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ThrowExpression" );
        expression.parse( "throw" );
    }

    public void testThrowExpressionWithException() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ThrowExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "throw my_exception()" );
    }

    public void testInclusiveOrExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " InclusiveOrExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j | k" );
    }

    public void testExclusiveOrExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ExclusiveOrExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j ^ k" );
    }

    public void testAndExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " AndExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "j & k" );
    }

    public void testEqualityExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " EqualityExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i == j" );
        expression.parse( "i != j" );
    }

    public void testRelationalExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " RelationalExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i < j" );
        expression.parse( "i > j" );
        expression.parse( "i <= j" );
        expression.parse( "i >= j" );
    }

    public void testShiftExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ShiftExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i << j" );
        expression.parse( "i >> j" );
    }

    public void testAdditiveExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " AdditiveExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i + j" );
        expression.parse( "i - j" );
    }

    public void testMultiplicativeExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " MultiplicativeExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i * j" );
        expression.parse( "i / j" );
        expression.parse( "i % j" );
    }

    public void testPointerToMemberExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " PointerToMemberExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "i .* j" );
        expression.parse( "i ->* j" );
    }

    public void testCastExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " CastExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "(int) i" );
        expression.parse( "(MyType) i" );
    }

    public void testUnaryExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " UnaryExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "++ i" );
        expression.parse( "-- i" );
        expression.parse( "& i" );
        expression.parse( "* i" );
        expression.parse( "+ i" );
        expression.parse( "- i" );
        expression.parse( "+ i" );
        expression.parse( "~ i" );
        expression.parse( "! i" );
        expression.parse( "sizeof i" );
    }

    public void testUnarySizeofExpression() throws ParseException // FIXME should be regular unary expression
    {
        expression.add( "ExpressionStatement" );
        expression.add( " UnaryExpression" );
        expression.parse( "sizeof( i )" ); // FIXME i considered as type_id()
    }

    public void testFunctionCallExpression() throws ParseException // TODO
    {
        expression.add( "ExpressionStatement" );
        expression.add( " IdExpression" ); // FIXME FunctionCallExpression ?
        expression.parse( "i()" );
    }

    public void testPostfixExpression() throws ParseException // TODO
    {
        expression.add( "ExpressionStatement" );
        expression.add( " PostfixExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  PostfixExpression" ); // FIXME ?!
        expression.parse( "i ++" );
        expression.parse( "i --" );
    }

    public void testThisIsPrimaryExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " PrimaryExpression" );
        expression.parse( "this" );
    }

    public void testParenthizedExpressionIsPrimaryExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " PrimaryExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "( i )" );
    }

    public void testNewExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " NewExpression" );
        expression.parse( "::new MyType" );
        expression.parse( "new MyType" );
        expression.parse( "new MyType()" );
    }

    public void testComplexNewExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " NewExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "new MyType( i )" );
        expression.parse( "new (i) MyType" );
        expression.parse( "new MyType[i]" );
    }

    public void testDeleteExpression() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " DeleteExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "::delete i" );
        expression.parse( "delete i" );
        expression.parse( "delete[] i" );
    }

    public void testTypeIdExpression() throws ParseException // FIXME consider as function call ?
    {
        expression.add( "ExpressionStatement" );
        expression.add( " TypeIdExpression" );
        expression.parse( "typeid( int )" );
        expression.parse( "typeid( i )" );
        expression.parse( "typeid( MyType& )" );
    }

    public void testClassDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.parse( "class {};" );
        tree.parse( "class MyClass {};" );
        tree.parse( "struct {};" );
        tree.parse( "struct MyStruct {};" );
        tree.parse( "union {};" );
        tree.parse( "union MyUnion {};" );
    }

    public void testClassDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDeclaration" );
        tree.parse( "class MyClass;" );
        tree.parse( "struct MyStruct;" );
        tree.parse( "union MyUnion;" );
    }

    public void testEnumDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        tree.parse( "enum {};" );
        tree.parse( "enum MyEnum {};" );
    }

    public void testClassVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.parse( "class MyClass {} c;" );
    }

    public void testAnonymousClassVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.parse( "class {} c;" );
    }

    public void testEnumVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.parse( "enum MyEnum {} e;" );
    }

    public void testAnonymousEnumVariableDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  EnumSpecifier" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.parse( "enum {} e;" );
    }

    public void testVariableExternalDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.parse( "int i;" );
    }

    public void testVariableExternalDeclarationAndInitialization() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "  ConstantExpression" );
        tree.parse( "int i = 0;" );
    }

    public void testClassMemberVariableDeclaration() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.add( "  ClassDefinition" );
        tree.add( "   FunctionParameterTypeQualifier" ); // FIXME FunctionParameterTypeQualifier
        tree.add( "   MemberDeclaration" );
        tree.parse( "class MyClass { int i; };" );
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
        tree.parse( "void MyFunction() { class MyClass { void MyMethod(); }; }" );
    }

    public void testNamespaceDefinition() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " NamespaceDefinition" );
        tree.parse( "namespace std {}" );
        tree.parse( "namespace {}" );
    }

    public void testUsingNamespace() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.parse( "using namespace std;" );
    }

    public void testUsingNamespaceType() throws ParseException
    {
        tree.add( "TranslationUnit" );
        tree.add( " Declaration" );
        tree.parse( "using namespace std::vector;" );
    }

    public void testLabelStatement() throws ParseException
    {
        expression.add( "LabelStatement" );
        expression.parse( "label:" );
    }

    public void testIfStatement() throws ParseException
    {
        expression.add( "IfStatement" );
        expression.add( " ConstantExpression" );
        expression.parse( "if( true )" );
    }

    public void testIfElseStatement() throws ParseException
    {
        expression.add( "IfStatement" );
        expression.add( " ConstantExpression" );
        expression.add( " ElseStatement" );
        expression.parse( "if( true ) ; else" );
    }

    public void testWhileStatement() throws ParseException
    {
        expression.add( "IterationStatement" );
        expression.add( " ConstantExpression" );
        expression.parse( "while( true )" );
    }

    public void testDoWhileStatement() throws ParseException
    {
        expression.add( "IterationStatement" );
        expression.add( " ConstantExpression" );
        expression.parse( "do ; while( true )" );
    }

    public void testForStatement() throws ParseException
    {
        expression.add( "IterationStatement" );
        expression.add( " FunctionParameterTypeQualifier" ); // FIXME ?!
        // expression.add( " InitializationStatement" ); // FIXME ?!
        expression.add( " ConstantExpression" );
        expression.add( " RelationalExpression" );
        expression.add( "  IdExpression" );
        expression.add( "  ConstantExpression" );
        expression.add( " UnaryExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "for( int i = 0; i < 2 ; ++i )" );
    }

    public void testSwitchStatement() throws ParseException
    {
        expression.add( "SwitchStatement" );
        expression.add( " IdExpression" );
        expression.add( " CaseStatement" );
        expression.add( "  ConstantExpression" );
        expression.add( "  BreakStatement" );
        expression.add( " CaseStatement" );
        expression.add( "  ConstantExpression" );
        expression.add( "  DefaultStatement" ); // FIXME default child of previous case when no break ?
        expression.add( "   BreakStatement" );
        expression.parse( "switch( i ) { case 0: break; case 1: default: break; }" );
    }

    public void testGotoStatement() throws ParseException
    {
        expression.add( "GotoStatement" );
        expression.parse( "goto label" );
    }

    public void testBreakStatement() throws ParseException
    {
        expression.add( "BreakStatement" );
        expression.parse( "break" );
    }

    public void testContinueStatement() throws ParseException
    {
        expression.add( "ContinueStatement" );
        expression.parse( "continue" );
    }

    public void testReturnStatement() throws ParseException
    {
        expression.add( "ReturnStatement" );
        expression.parse( "return" );
    }

    public void testReturnStatementWithExpression() throws ParseException
    {
        expression.add( "ReturnStatement" );
        expression.add( " ConstantExpression" );
        expression.parse( "return 12" );
    }

    public void testThrowStatement() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ThrowExpression" );
        expression.parse( "throw" );
    }

    public void testThrowStatementWithException() throws ParseException
    {
        expression.add( "ExpressionStatement" );
        expression.add( " ThrowExpression" );
        expression.add( "  IdExpression" );
        expression.parse( "throw exception()" );
    }

    public void testTryCatchBlocks() throws ParseException
    {
        expression.add( "TryBlock" );
        expression.add( "CatchBlock" ); // FIXME should CatchBlock be child of TryBlock ?
        expression.add( " Parameter" );
        expression.add( "  ParameterType" );
        expression.add( "  FunctionParameterTypeQualifier" );
        expression.add( "CatchBlock" );
        expression.parse( "try {} catch( exception& ) {} catch(...) {}" );
    }
}
