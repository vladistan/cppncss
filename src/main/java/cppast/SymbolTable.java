
package cppast;

/**
 * Manages the symbol table and scopes within a given compilation unit.
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

    public final void closeScope()
    {
        current = current.close();
    }

    public final void closeScopes()
    {
        current = root;
    }

    public final void putTypeName( final String name )
    {
        current.putTypeName( name );
    }

    public final boolean isConstructor( final String name ) // FIXME probably not needed anymore ?
    {
        final Scope scope = getScope( name );
        return scope != null && scope.isConstructor( getLastName( name ) );
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
     * Returns B in A::B::C.
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

    public final void extend( final String name )
    {
        final Scope scope = getScope( name );
        if( scope != null )
            current.extend( scope.getScope( getLastName( name ) ) );
    }
}
