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

/**
 * Manages the symbol table and scopes.
 *
 * @author Mathieu Champlon
 */
public class SymbolTable
{
    private final Scope root;
    private Scope current;

    /**
     * Creates a symbol table.
     */
    public SymbolTable()
    {
        root = new Scope();
        current = root;
    }

    /**
     * Opens a scope of a given name.
     *
     * @param name a non-null scope name
     */
    public final void openScope( final String name )
    {
        final Scope scope = findScope( name );
        if( scope != null )
            current = scope;
    }

    private Scope findScope( final String name )
    {
        final Scope scope = current.getScope( name );
        if( scope != null )
            return scope;
        return createScope( name );
    }

    private Scope createScope( final String name )
    {
        return new Scope( name, current );
    }

    /**
     * Close current scope.
     */
    public final void closeScope()
    {
        current = current.close();
    }

    /**
     * Close all scopes.
     */
    public final void closeScopes()
    {
        current = root;
    }

    /**
     * Add a type name to current scope.
     *
     * @param name the name of the type to add
     */
    public final void addType( final String name )
    {
        current.addType( name );
    }

    public final Scope getCurrentScope()
    {
        return current;
    }

    /**
     * Extend the current scope with another scope.
     *
     * @param name the name of the extension scope
     */
    public final void extend( final String name )
    {
        current.extend( getScope( name ) );
    }

    private Scope getScope( final String name )
    {
        if( name == null )
            return null;
        if( name.indexOf( "::" ) == 0 )
            return root.getScope( name.substring( 2 ) );
        return current.getScope( name );
    }
}
