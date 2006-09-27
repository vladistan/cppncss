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

import java.io.StringReader;
import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class AstTest extends TestCase
{
    private Node parse( final String data ) throws ParseException
    {
        return new Parser( new StringReader( data ) ).translation_unit();
    }

    private Node parseExpression( final String data ) throws ParseException
    {
        final Node root = parse( "void MyFunction() { " + data + "; }" );
        final Node child = assertIsBranch( root, AstFunctionDefinition.class );
        assertEquals( 3, child.jjtGetNumChildren() );
        assertIsLeaf( child.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsLeaf( child.jjtGetChild( 1 ), AstFunctionParameters.class );
        final Node body = child.jjtGetChild( 2 );
        assertEquals( AstFunctionBody.class, body.getClass() );
        assertEquals( 1, body.jjtGetNumChildren() );
        final Node expression = body.jjtGetChild( 0 );
        assertEquals( AstExpressionStatement.class, expression.getClass() );
        assertEquals( 1, expression.jjtGetNumChildren() );
        return expression.jjtGetChild( 0 );
    }

    private void assertIsLeaf( final Node node, final Class c )
    {
        assertEquals( c, node.getClass() );
        assertEquals( 0, node.jjtGetNumChildren() );
    }

    private Node assertIsBranch( final Node node, final Class to )
    {
        assertEquals( 1, node.jjtGetNumChildren() );
        final Node child = node.jjtGetChild( 0 );
        assertEquals( to, child.getClass() );
        return child;
    }

    private void assertIsFunctionDefinition( final Node node, final Class c )
    {
        final Node child = assertIsBranch( node, c );
        assertEquals( 3, child.jjtGetNumChildren() );
        assertIsLeaf( child.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsLeaf( child.jjtGetChild( 1 ), AstFunctionParameters.class );
        assertIsLeaf( child.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    private void assertIsParameter( final Node node )
    {
        assertEquals( AstParameter.class, node.getClass() );
        assertEquals( 2, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstParameterType.class );
        assertIsLeaf( node.jjtGetChild( 1 ), AstFunctionParameterTypeQualifier.class );
    }

    private void assertIsParameters( final Node node, final int number )
    {
        assertEquals( AstFunctionParameters.class, node.getClass() );
        assertEquals( number, node.jjtGetNumChildren() );
        for( int i = 0; i < number; ++i )
            assertIsParameter( node.jjtGetChild( i ) );
    }

    private void assertNoChildOf( final Node node, final Class c )
    {
        for( int i = 0; i < node.jjtGetNumChildren(); ++i )
            assertNotSame( c, node.jjtGetChild( i ) );
    }

    private void assertIsExpression( final Node node, final Class type, int childs )
    {
        assertEquals( type, node.getClass() );
        assertEquals( childs, node.jjtGetNumChildren() );
        for( int i = 0; i < node.jjtGetNumChildren(); ++i )
            assertIsLeaf( node.jjtGetChild( i ), AstIdExpression.class );
    }

    public void testFunctionDefinition() throws ParseException
    {
        final Node root = parse( "void MyFunction() {}" );
        assertIsFunctionDefinition( root, AstFunctionDefinition.class );
    }

    public void testFunctionWithOneParameter() throws ParseException
    {
        final Node root = parse( "void MyFunction( int i ) {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsParameters( node.jjtGetChild( 1 ), 1 );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    public void testFunctionWithTwoParameters() throws ParseException
    {
        final Node root = parse( "void MyFunction( int i, const float& p ) {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsParameters( node.jjtGetChild( 1 ), 2 );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    public void testFunctionDeclaration() throws ParseException
    {
        final Node root = parse( "void MyFunction();" );
        assertNoChildOf( root, AstFunctionDefinition.class );
    }

    public void testMethodDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { void MyMethod() {} };" );
        assertIsFunctionDefinition( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
    }

    public void testMethodDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { void MyMethod(); };" );
        assertNoChildOf( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
    }

    public void testMethodSeparateDefinition() throws ParseException
    {
        final Node root = parse( "void MyClass::MyMethod() {}" );
        assertIsFunctionDefinition( root, AstFunctionDefinition.class );
    }

    public void testConstructorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { MyClass() {} };" );
        assertIsFunctionDefinition( assertIsBranch( root, AstClassDefinition.class ), AstConstructorDefinition.class );
    }

    public void testConstructorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { MyClass(); };" );
        assertNoChildOf( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
    }

    public void testConstructorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::MyClass() {}" );
        assertIsFunctionDefinition( root, AstConstructorDefinition.class );
    }

    public void testDestructorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { ~MyClass() {} };" );
        assertIsFunctionDefinition( assertIsBranch( root, AstClassDefinition.class ), AstDestructorDefinition.class );
    }

    public void testDestructorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { ~MyClass(); };" );
        assertNoChildOf( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
    }

    public void testDestructorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::~MyClass() {}" );
        assertIsFunctionDefinition( root, AstDestructorDefinition.class );
    }

    public void testEqualityOperatorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { bool operator==( const MyClass& ) {} };" );
        final Node node = assertIsBranch( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsParameters( node.jjtGetChild( 1 ), 1 );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    public void testEqualityOperatorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { bool operator==( const MyClass& ); };" );
        final Node node = assertIsBranch( root, AstClassDefinition.class );
        assertNoChildOf( node, AstFunctionDefinition.class ); // FIXME check FunctionDeclaration
    }

    public void testEqualityOperatorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "bool MyClass::operator==( const MyClass& ) {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsParameters( node.jjtGetChild( 1 ), 1 );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    public void testConversionOperatorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { operator const char*() const {} };" );
        assertIsFunctionDefinition( assertIsBranch( root, AstClassDefinition.class ), AstFunctionDefinition.class );
    }

    public void testConversionOperatorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { operator const char*() const; };" );
        final Node node = assertIsBranch( root, AstClassDefinition.class );
        assertNoChildOf( node, AstFunctionDefinition.class );
    }

    public void testConversionOperatorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::operator const char*() const {}" );
        assertIsFunctionDefinition( root, AstFunctionDefinition.class );
    }

    public void testIdExpression() throws ParseException
    {
        assertIsLeaf( parseExpression( "i" ), AstIdExpression.class );
        assertIsLeaf( parseExpression( "MyClass::i" ), AstIdExpression.class );
    }

    public void testConstantExpression() throws ParseException
    {
        assertIsLeaf( parseExpression( "42" ), AstConstantExpression.class );
        assertIsLeaf( parseExpression( "\"abc\"" ), AstConstantExpression.class );
        assertIsLeaf( parseExpression( "\"abc\" \"def\"" ), AstConstantExpression.class );
    }

    public void testLogicalAndExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "j && k" ), AstLogicalAndExpression.class, 2 );
    }

    public void testLogicalOrExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "j || k" ), AstLogicalOrExpression.class, 2 );
    }

    public void testConditionalExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "j ? k : l;" ), AstConditionalExpression.class, 3 );
    }

    public void testAssignmentExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "j = k" ), AstAssignmentExpression.class, 2 );
    }

    public void testThrowExpression() throws ParseException
    {
        assertIsLeaf( parseExpression( "throw" ), AstThrowExpression.class );
    }

    public void testThrowExpressionWithException() throws ParseException
    {
        final Node root = parseExpression( "throw my_exception()" );
        assertEquals( AstThrowExpression.class, root.getClass() );
        assertIsBranch( root, AstIdExpression.class ); // FIXME IdExpression ?
    }

    public void testInclusiveOrExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i | j" ), AstInclusiveOrExpression.class, 2 );
    }

    public void testExclusiveOrExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i ^ j" ), AstExclusiveOrExpression.class, 2 );
    }

    public void testAndExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i & j" ), AstAndExpression.class, 2 );
    }

    public void testEqualityExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i == j" ), AstEqualityExpression.class, 2 );
        assertIsExpression( parseExpression( "i != j" ), AstEqualityExpression.class, 2 );
    }

    public void testRelationalExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i < j" ), AstRelationalExpression.class, 2 );
        assertIsExpression( parseExpression( "i > j" ), AstRelationalExpression.class, 2 );
        assertIsExpression( parseExpression( "i <= j" ), AstRelationalExpression.class, 2 );
        assertIsExpression( parseExpression( "i >= j" ), AstRelationalExpression.class, 2 );
    }

    public void testShiftExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i << j" ), AstShiftExpression.class, 2 );
        assertIsExpression( parseExpression( "i >> j" ), AstShiftExpression.class, 2 );
    }

    public void testAdditiveExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i + j" ), AstAdditiveExpression.class, 2 );
        assertIsExpression( parseExpression( "i - j" ), AstAdditiveExpression.class, 2 );
    }

    public void testMultiplicativeExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i * j" ), AstMultiplicativeExpression.class, 2 );
        assertIsExpression( parseExpression( "i / j" ), AstMultiplicativeExpression.class, 2 );
        assertIsExpression( parseExpression( "i % j" ), AstMultiplicativeExpression.class, 2 );
    }

    public void testPointerToMemberExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "i .* j" ), AstPointerToMemberExpression.class, 2 );
        assertIsExpression( parseExpression( "i ->* j" ), AstPointerToMemberExpression.class, 2 );
    }

    public void testCastExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "(int) i" ), AstCastExpression.class, 1 );
        assertIsExpression( parseExpression( "(MyType) i" ), AstCastExpression.class, 1 );
    }

    public void testUnaryExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "++ i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "-- i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "& i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "* i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "+ i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "- i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "~ i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "! i" ), AstUnaryExpression.class, 1 );
        assertIsExpression( parseExpression( "sizeof i" ), AstUnaryExpression.class, 1 );
        // FIXME assertIsLeaf because i considered as type_id()
        assertIsLeaf( parseExpression( "sizeof( i )" ), AstUnaryExpression.class );
    }

    public void testFunctionCallExpression() throws ParseException // TODO
    {
        // assertIsExpression( parseExpression( "i()" ), AstFunctionCallExpression.class, 1 );
    }

    public void testPostfixExpression() throws ParseException // TODO
    {
        // assertIsExpression( parseExpression( "i ++" ), AstPostfixExpression.class, 1 );
        // assertIsExpression( parseExpression( "i --" ), AstPostfixExpression.class, 1 );
    }

    public void testPrimaryExpression() throws ParseException
    {
        assertIsLeaf( parseExpression( "this" ), AstPrimaryExpression.class );
        assertIsExpression( parseExpression( "( i )" ), AstPrimaryExpression.class, 1 );
    }

    public void testNewExpression() throws ParseException
    {
        assertIsLeaf( parseExpression( "::new MyType" ), AstNewExpression.class );
        assertIsLeaf( parseExpression( "new MyType" ), AstNewExpression.class );
        assertIsLeaf( parseExpression( "new MyType()" ), AstNewExpression.class );
        assertIsExpression( parseExpression( "new MyType( i )" ), AstNewExpression.class, 1 );
        assertIsExpression( parseExpression( "new (i) MyType" ), AstNewExpression.class, 1 );
        assertIsExpression( parseExpression( "new MyType[i]" ), AstNewExpression.class, 1 );
    }

    public void testDeleteExpression() throws ParseException
    {
        assertIsExpression( parseExpression( "::delete i" ), AstDeleteExpression.class, 1 );
        assertIsExpression( parseExpression( "delete i" ), AstDeleteExpression.class, 1 );
        assertIsExpression( parseExpression( "delete[] i" ), AstDeleteExpression.class, 1 );
    }

    public void testTypeIdExpression() throws ParseException // FIXME consider as function call ? // TODO
    {
        assertIsLeaf( parseExpression( "typeid( int )" ), AstTypeIdExpression.class );
        // assertIsExpression( parseExpression( "typeid( MyType& )" ), AstTypeIdExpression.class, 1 );
        // assertIsExpression( parseExpression( "typeid( i )" ), AstTypeIdExpression.class, 1 );
    }

    public void testClassDefinition() throws ParseException
    {
        assertIsBranch( parse( "class {};" ), AstClassDefinition.class );
        assertIsBranch( parse( "class MyClass {};" ), AstClassDefinition.class );
    }
}
