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
     * Opens a new scope with a given name.
     *
     * @param name a non-null scope name
     */
    public final void openScope( final String name )
    {
        if( name == null )
            throw new IllegalArgumentException( "scope name is null" );
        openScope( findScope( name ) );
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
        final Scope scope = new Scope( name, current );
        current.putTypeName( name, scope );
        return scope;
    }

    /**
     * Opens a given scope.
     *
     * @param scope a possibly null scope
     * @return whether the scope has been opened or not
     */
    public final boolean openScope( final Scope scope )
    {
        if( scope != null )
        {
            current = scope;
            return true;
        }
        return false;
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
    public final void putTypeName( final String name )
    {
        current.putTypeName( name );
    }

    private String getLastName( final String name )
    {
        if( name.equals( "::" ) )
            return name;
        final int index = name.lastIndexOf( "::" );
        if( index == -1 )
            return name;
        return name.substring( index + 2 );
    }

    public final Scope getCurrentScope()
    {
        return current;
    }

    /**
     * Returns the parent scope of a fully scoped name.
     * <p>
     * e.g. the scope B in A::B::C.
     *
     * @param name the name of the scope
     * @return the parent scope
     */
    private Scope getScope( final String name )
    {
        if( name == null )
            return null;
        if( name.indexOf( "::" ) == 0 )
            return getScope( root, name.substring( 2 ) );
        return getScope( current, name );
    }

    private Scope getScope( final Scope scope, final String name )
    {
        final int index = name.lastIndexOf( "::" );
        if( index == -1 )
            return scope;
        return scope.getScope( name.substring( 0, index ) );
    }

    /**
     * Extend the current scope with another scope.
     *
     * @param name the name of the extension scope
     */
    public final void extend( final String name )
    {
        final Scope scope = getScope( name );
        if( scope != null )
            current.extend( scope.getScope( getLastName( name ) ) );
    }
}
