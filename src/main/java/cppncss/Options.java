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

package cppncss;

import java.util.List;
import java.util.Vector;

/**
 * @author Mathieu Champlon
 */
public class Options
{
    private final String[] args;

    public Options( final String[] args )
    {
        this.args = args;
    }

    public final List<String> getArgList()
    {
        final Vector<String> result = new Vector<String>();
        for( int i = 0; i < args.length; ++i )
            if( !args[i].startsWith( "-" ) )
                result.add( args[i] );
        return result;
    }

    public final boolean hasOption( final String name )
    {
        for( int i = 0; i < args.length; ++i )
            if( args[i].equals( '-' + name ) )
                return true;
        return false;
    }

    private boolean isOption( final String arg, final String name )
    {
        return arg.length() > name.length() + 1 && arg.startsWith( '-' + name );
    }

    public final List<String> getOptionValues( final String name )
    {
        final Vector<String> result = new Vector<String>();
        for( int i = 0; i < args.length; ++i )
            if( isOption( args[i], name ) )
                result.add( args[i].substring( 1 + name.length() ) );
        return result;
    }

    public final List<String> getOptionProperties( final String name )
    {
        final Vector<String> result = new Vector<String>();
        for( int i = 0; i < args.length; ++i )
            if( isOption( args[i], name ) )
            {
                final int index = args[i].indexOf( '=' );
                if( index == -1 )
                    result.add( args[i].substring( 1 + name.length() ) );
                else
                    result.add( args[i].substring( 1 + name.length(), index ) );
            }
        return result;
    }

    public final List<String> getOptionPropertyValues( final String name )
    {
        final Vector<String> result = new Vector<String>();
        for( int i = 0; i < args.length; ++i )
            if( isOption( args[i], name ) )
            {
                final int index = args[i].indexOf( '=' );
                if( index == -1 )
                    result.add( "" );
                else
                    result.add( args[i].substring( 1 + index ) );
            }
        return result;
    }
}
