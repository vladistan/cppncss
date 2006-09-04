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

import tools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public class SymbolTableTest extends EasyMockTestCase
{
    private SymbolTable symbols;

    protected void setUp()
    {
        symbols = new SymbolTable();
    }

    public void testConstructorMatchesClassDeclaration()
    {
        symbols.openScope( "MyClass" );
        assertTrue( symbols.isConstructor( "MyClass" ) );
        assertTrue( symbols.isConstructor( "MyClass::MyClass" ) );
        assertFalse( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testSeparateConstructorMatchesClassDeclaration()
    {
        symbols.openScope( "MyClass" );
        symbols.closeScope();
        assertFalse( symbols.isConstructor( "MyClass" ) );
        assertTrue( symbols.isConstructor( "MyClass::MyClass" ) );
        assertFalse( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testConstructorInClassAndNamespaceMatchesClassDeclaration()
    {
        symbols.openScope( "my_namespace" );
        symbols.openScope( "MyClass" );
        assertTrue( symbols.isConstructor( "MyClass" ) );
        assertTrue( symbols.isConstructor( "MyClass::MyClass" ) );
        assertTrue( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testConstructorInNamespaceMatchesClassDeclaration()
    {
        symbols.openScope( "my_namespace" );
        symbols.openScope( "MyClass" );
        symbols.closeScope();
        assertFalse( symbols.isConstructor( "MyClass" ) );
        assertTrue( symbols.isConstructor( "MyClass::MyClass" ) );
        assertTrue( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testSeparateConstructorInNamespaceMatchesClassDeclaration()
    {
        symbols.openScope( "my_namespace" );
        symbols.openScope( "MyClass" );
        symbols.closeScope();
        symbols.closeScope();
        assertFalse( symbols.isConstructor( "MyClass" ) );
        assertFalse( symbols.isConstructor( "MyClass::MyClass" ) );
        assertTrue( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testExtendingWithNonExistingScopeIsNoOp()
    {
        symbols.extend( "std::runtime_error" );
    }

    public void testExtendingScopeAllowsToFindClassDeclaration()
    {
        symbols.openScope( "my_namespace" );
        symbols.openScope( "MyClass" );
        symbols.closeScope();
        symbols.closeScope();
        symbols.extend( "my_namespace" );
        assertFalse( symbols.isConstructor( "MyClass" ) );
        assertTrue( symbols.isConstructor( "MyClass::MyClass" ) );
        assertTrue( symbols.isConstructor( "my_namespace::MyClass::MyClass" ) );
    }

    public void testResolveReturnsFullScope()
    {
        symbols.openScope( "my_namespace" );
        symbols.openScope( "MyClass" );
        final Scope scope1 = symbols.getCurrentScope();
        symbols.closeScope();
        final Scope scope2 = symbols.getCurrentScope();
        symbols.closeScope();
        final Scope scope3 = symbols.getCurrentScope();
        symbols.extend( "my_namespace" );
        final Scope scope4 = symbols.getCurrentScope();
        assertEquals( "my_namespace::MyClass::Anything", scope1.resolve( "Anything" ) );
        assertEquals( "my_namespace::MyClass::Anything", scope2.resolve( "MyClass::Anything" ) );
        assertEquals( "my_namespace::MyClass::Anything", scope3.resolve( "my_namespace::MyClass::Anything" ) );
        assertEquals( "my_namespace::MyClass::Anything", scope4.resolve( "MyClass::Anything" ) );
    }
}
