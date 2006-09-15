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

/**
 * Manages macro pre-processing.
 *
 * @author Mathieu Champlon
 */
public class Macro
{
    private final String name;
    private final String value;

    /**
     * Create a macro.
     *
     * @param name the name of the symbol
     * @param value the value of the macro
     */
    public Macro( final String name, final String value )
    {
        if( name == null )
            throw new IllegalArgumentException( "parameter 'name' is null" );
        if( value == null )
            throw new IllegalArgumentException( "parameter 'value' is null" );
        this.name = name;
        this.value = value;
    }

    /**
     * Filter define occurences.
     *
     * @param text the text to filter
     * @return the filtered text
     */
    public final String replace( final String text )
    {
        int index = text.indexOf( name );
        if( index == -1 )
            return text;
        final StringBuffer buffer = new StringBuffer();
        int previous = -1;
        while( index != -1 )
        {
            buffer.append( text.substring( previous + 1, index ) );
            final int start = text.indexOf( '(', index );
            if( start == -1 || invalidate( text, index + name.length(), start ) )
            {
                previous = index - 1;
                index = text.indexOf( name, index + 1 );
            }
            else
            {
                previous = findEnd( text, start + 1, text.indexOf( ')', start + 1 ) );
                buffer.append( value );
                index = text.indexOf( name, previous );
            }
        }
        buffer.append( text.substring( previous + 1 ) );
        return buffer.toString();
    }

    private static boolean invalidate( final String text, final int index, final int start )
    {
        for( int i = index; i < start; ++i )
        {
            final char c = text.charAt( i );
            if( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
                return true;
        }
        return false;
    }

    private static int findEnd( final String text, final int from, final int current )
    {
        final int start = text.indexOf( '(', from );
        final int end = text.indexOf( ')', current );
        if( start != -1 && start < end )
            return findEnd( text, start + 1, end + 1 );
        return end;
    }

    /**
     * Test if a given symbol matches.
     *
     * @param name the symbol
     * @return whether the symbol matches or not
     */
    public final boolean matches( final String name )
    {
        return this.name.equals( name );
    }
}
