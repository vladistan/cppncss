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

import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class SymbolTableTest extends TestCase
{
    private SymbolTable symbols;

    protected void setUp()
    {
        symbols = new SymbolTable();
    }

    public void testResolveOutsideOfAnyScopeDoesNotAddAnyPrefix()
    {
        assertEquals( "Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testExtendingWithNonExistingScopeIsNoOp()
    {
        symbols.extend( "std::runtime_error" );
        assertEquals( "Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testResolveInsideSimpleScopeAddsPrefix()
    {
        symbols.openScope( "my_scope" );
        assertEquals( "my_scope::Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testResolveInsideComplexScopeAddsPrefix()
    {
        symbols.openScope( "my_scope1" );
        symbols.openScope( "my_scope2" );
        assertEquals( "my_scope1::my_scope2::Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testOpenSeveralScopesAtTheSameTime()
    {
        symbols.openScope( "my_scope1::my_scope2" );
        assertEquals( "my_scope1::my_scope2::Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testOpenSeveralScopesAtTheSameTimeAndCloseOne()
    {
        symbols.openScope( "my_scope1::my_scope2" );
        symbols.closeScope();
        assertEquals( "my_scope1::Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
    }

    public void testResolveSubPathOutsideOfScopeAddsPrefix()
    {
        symbols.openScope( "my_scope1" );
        symbols.openScope( "my_scope2" );
        symbols.closeScope();
        assertEquals( "my_scope1::Symbol", symbols.getCurrentScope().resolve( "Symbol" ) );
        assertEquals( "my_scope1::my_scope2::Symbol", symbols.getCurrentScope().resolve( "my_scope2::Symbol" ) );
    }

    public void testResolveSearchesInExtendingScopes()
    {
        symbols.openScope( "my_scope1" );
        symbols.openScope( "my_scope2" );
        symbols.closeScopes();
        symbols.extend( "my_scope1" );
        assertEquals( "my_scope1::my_scope2::Symbol", symbols.getCurrentScope().resolve( "my_scope2::Symbol" ) );
    }

    public void testResolveSearchesInExtendingSubScopes()
    {
        symbols.openScope( "my_scope1" );
        symbols.openScope( "my_scope2" );
        symbols.openScope( "my_scope3" );
        symbols.closeScopes();
        symbols.extend( "my_scope1::my_scope2" );
        assertEquals( "my_scope1::my_scope2::my_scope3::Symbol", symbols.getCurrentScope().resolve( "my_scope3::Symbol" ) );
    }

    public void testTmp()
    {
        assertEquals("", "::".substring( 2 ));
    }
}
