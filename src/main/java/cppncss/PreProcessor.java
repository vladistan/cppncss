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

package cppncss;

import java.util.Iterator;
import java.util.Vector;

/**
 * Provides a simple pre-processor implementation.
 *
 * @author Mathieu Champlon
 */
public class PreProcessor
{
    private final Vector<Define> defines = new Vector<Define>();
    private final Vector<Macro> macros = new Vector<Macro>();

    /**
     * Expand macros and defines.
     *
     * @param text the input text
     * @return the resulting text
     */
    public final String filter( final String text )
    {
        return replaceDefines( replaceMacros( text ) );
    }

    private String replaceMacros( final String text )
    {
        String result = text;
        final Iterator<Macro> names = macros.iterator();
        while( names.hasNext() )
            result = names.next().replace( result );
        return result;
    }

    private String replaceDefines( final String text )
    {
        String result = text;
        final Iterator<Define> iterator = defines.iterator();
        while( iterator.hasNext() )
            result = iterator.next().replace( result );
        return result;
    }

    /**
     * Add a define.
     *
     * @param name the name of the symbol
     * @param value the value of the symbol
     */
    public final void addDefine( final String name, final String value )
    {
        final Iterator<Define> iterator = defines.iterator();
        while( iterator.hasNext() )
            if( iterator.next().matches( name ) )
                throw new RuntimeException( "macro redefinition '" + name + "'" );
        defines.add( new Define( name, value ) );
    }

    /**
     * Add a macro.
     *
     * @param name the name of the symbol
     * @param value the value of the symbol
     */
    public final void addMacro( final String name, final String value )
    {
        final Iterator<Macro> iterator = macros.iterator();
        while( iterator.hasNext() )
            if( iterator.next().matches( name ) )
                throw new RuntimeException( "macro redefinition '" + name + "'" );
        macros.add( new Macro( name, value ) );
    }
}
