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

    private void assertIsLeaf( final Node node, final Class c )
    {
        assertEquals( c, node.getClass() );
        assertEquals( 0, node.jjtGetNumChildren() );
    }

    private Node assertIsBranch( final Node node, final Class c )
    {
        assertEquals( 1, node.jjtGetNumChildren() );
        final Node child = node.jjtGetChild( 0 );
        assertTrue( child.getClass().equals( c ) );
        return child;
    }

    private void assertIsFunctionDefinition( final Node node )
    {
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsLeaf( node.jjtGetChild( 1 ), AstFunctionParameters.class );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    private void assertIsParameter( final Node parameter )
    {
        assertEquals( AstParameter.class, parameter.getClass() );
        assertEquals( 2, parameter.jjtGetNumChildren() );
        assertIsLeaf( parameter.jjtGetChild( 0 ), AstParameterType.class );
        assertIsLeaf( parameter.jjtGetChild( 1 ), AstFunctionParameterTypeQualifier.class );
    }

    private void assertIsParameters( final Node parameters, final int number )
    {
        assertEquals( AstFunctionParameters.class, parameters.getClass() );
        assertEquals( number, parameters.jjtGetNumChildren() );
        for( int i = 0; i < number; ++i )
            assertIsParameter( parameters.jjtGetChild( i ) );
    }

    private void assertNoChildOf( final Node root, final Class c )
    {
        for( int i = 0; i < root.jjtGetNumChildren(); ++i )
            assertNotSame( c, root.jjtGetChild( i ) );
    }

    public void testFunctionDefinition() throws ParseException
    {
        final Node root = parse( "void MyFunction() {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertIsFunctionDefinition( node );
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
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testMethodDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { void MyMethod(); };" );
        assertNoChildOf( root, AstFunctionDefinition.class );
    }

    public void testMethodSeparateDefinition() throws ParseException
    {
        final Node root = parse( "void MyClass::MyMethod() {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testConstructorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { MyClass() {} };" );
        final Node node = assertIsBranch( root, AstConstructorDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testConstructorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { MyClass(); };" );
        assertNoChildOf( root, AstFunctionDefinition.class );
    }

    public void testConstructorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::MyClass() {}" );
        final Node node = assertIsBranch( root, AstConstructorDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testDestructorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { ~MyClass() {} };" );
        final Node node = assertIsBranch( root, AstDestructorDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testDestructorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { ~MyClass(); };" );
        assertNoChildOf( root, AstFunctionDefinition.class );
    }

    public void testDestructorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::~MyClass() {}" );
        final Node node = assertIsBranch( root, AstDestructorDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testEqualityOperatorDefinition() throws ParseException
    {
        final Node root = parse( "class MyClass { bool operator==( const MyClass& ) {} };" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertEquals( 3, node.jjtGetNumChildren() );
        assertIsLeaf( node.jjtGetChild( 0 ), AstFunctionName.class );
        assertIsParameters( node.jjtGetChild( 1 ), 1 );
        assertIsLeaf( node.jjtGetChild( 2 ), AstFunctionBody.class );
    }

    public void testEqualityOperatorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { bool operator==( const MyClass& ) {} };" );
        assertNoChildOf( root, AstFunctionDefinition.class );
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
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertIsFunctionDefinition( node );
    }

    public void testConversionOperatorDeclaration() throws ParseException
    {
        final Node root = parse( "class MyClass { operator const char*() const; };" );
        assertNoChildOf( root, AstFunctionDefinition.class );
    }

    public void testConversionOperatorSeparateDefinition() throws ParseException
    {
        final Node root = parse( "MyClass::operator const char*() const {}" );
        final Node node = assertIsBranch( root, AstFunctionDefinition.class );
        assertIsFunctionDefinition( node );
    }
}
