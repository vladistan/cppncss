
package cppncss.cppast;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class Scope
{
    /**
     * Name of the scope.
     */
    private String name = "";
    /**
     * Type introduced in this scope.
     */
    private final Set<String> types = new HashSet<String>();
    private final Hashtable<String, Scope> scopes = new Hashtable<String, Scope>();
    /**
     * Parent scope. (null if it is the global scope).
     */
    private Scope parent;

    /**
     * Creates a scope object with a given name.
     */
    public Scope( final String name, final Scope parent )
    {
        if( name == null )
            throw new IllegalArgumentException( "name must not be null" );
        if( parent == null )
            throw new IllegalArgumentException( "parent must not be null" );
        if( name.contains( "::" ) )
            throw new IllegalArgumentException( "scope name must not contain '::'" );
        this.name = name;
        this.parent = parent;
    }

    /**
     * Creates an unnamed empty scope.
     */
    public Scope()
    {
    }

    /**
     * Inserts a name into the table to say that it is the name of a type.
     */
    public final void putTypeName( final String name )
    {
        if( name == null )
            throw new IllegalArgumentException( "type name is null" );
        if( name.contains( "::" ) )
            throw new IllegalArgumentException( "type name '" + name + "' contains '::'" );
        if( types.contains( name ) )
            throw new IllegalArgumentException( "type name '" + name + "' already exists in scope : '" + this.name
                    + "'" );
        types.add( name );
    }

    /**
     * A type with a scope (class/struct/union).
     */
    public final void putTypeName( final String name, final Scope scope )
    {
        if( name.contains( "::" ) )
            throw new IllegalArgumentException( "type name must not contain '::'" );
        types.add( name );
        scopes.put( name, scope );
    }

    public final void extend( final Scope scope )
    {
        if( scope != null )
        {
            types.addAll( scope.types );
            scopes.putAll( scope.scopes );
        }
    }

    public Scope getScope( final String name )
    {
        final int index = name.indexOf( "::" );
        if( index != -1 )
        {
            final String prefix = name.substring( 0, index );
            final String postfix = name.substring( index + 2 );
            final Scope scope = getScope( prefix );
            if( scope == null )
                return null;
            return scope.getScope( postfix );
        }
        if( !scopes.containsKey( name ) )
        {
            if( parent == null )
                return null;
            return parent.getScope( name );
        }
        return scopes.get( name );
    }

    /**
     * {@inheritDoc}
     */
    public final String toString()
    {
        if( parent == null )
            return "";
        return parent.toString() + name + "::";
    }

    public final boolean isConstructor( final String name )
    {
        return this.name.equals( name );
    }

    public final Scope close()
    {
        if( parent == null )
            return this;
        return parent;
    }

    public final String resolve( final String name )
    {
        final int index = name.lastIndexOf( "::" );
        if( index == -1 )
            return toString() + name;
        final Scope scope = getScope( name.substring( 0, index ) );
        if( scope == null )
            return toString() + name;
        return scope.toString() + name.substring( index + 2 );
    }
}
