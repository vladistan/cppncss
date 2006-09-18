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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Represents a scope.
 *
 * @author Mathieu Champlon
 */
public final class Scope
{
    private final String name;
    private final Hashtable<String, Scope> scopes = new Hashtable<String, Scope>();
    private Scope parent;

    /**
     * Creates a scope object with a given name.
     *
     * @param name the name of the scope
     * @param parent the parent of the scope
     */
    private Scope( final String name, final Scope parent )
    {
        if( name == null )
            throw new IllegalArgumentException( "scope name is null" );
        if( parent == null )
            throw new IllegalArgumentException( "scope parent is null" );
        if( name.contains( "::" ) )
            throw new IllegalArgumentException( "scope name '" + name + "' contains '::'" );
        this.name = name;
        this.parent = parent;
    }

    /**
     * Creates an unnamed empty scope.
     */
    public Scope()
    {
        this.name = "";
        this.parent = null;
    }

    /**
     * Create a sub-scope to the scope.
     *
     * @param name the name of the sub-scope
     * @return the new sub-scope
     */
    public Scope createScope( final String name )
    {
        final Scope scope = new Scope( name, this );
        scopes.put( name, scope );
        return scope;
    }

    /**
     * Extend the scope with another one.
     * <p>
     * Types from the given scope are added to the extended scope.
     *
     * @param scope the scope to merge into the current scope
     */
    public void extend( final Scope scope )
    {
        if( scope != null )
            scopes.putAll( scope.scopes );
    }

    /**
     * Retrieve the scope of a given name.
     *
     * @param name the name of the scope
     * @return the matching scope
     */
    public Scope getScope( final String name )
    {
        final int index = name.indexOf( "::" );
        if( index != -1 )
        {
            final Scope scope = getScope( name.substring( 0, index ) );
            if( scope != null )
                return scope.getScope( name.substring( index + 2 ) );
            return null;
        }
        if( scopes.containsKey( name ) )
            return scopes.get( name );
        if( parent != null )
            return parent.getScope( name );
        return null;
    }

    /**
     * Close the scope.
     *
     * @return the parent scope
     */
    public Scope close()
    {
        if( parent == null )
            return this;
        return parent;
    }

    /**
     * Resolve a name into the scope.
     *
     * @param name the name
     * @return the fully scoped name
     */
    public String resolve( final String name )
    {
        final int index = name.lastIndexOf( "::" );
        if( index == -1 )
            return toString() + name;
        final Scope scope = getScope( name.substring( 0, index ) );
        if( scope == null )
            return toString() + name;
        return scope.resolve( name.substring( index + 2 ) );
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        if( parent == null )
            return "";
        return parent.toString() + name + "::";
    }
}
