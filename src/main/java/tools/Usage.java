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

package tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a means to format an application usage.
 *
 * @author Mathieu Champlon
 */
public final class Usage
{
    private static final int SECURITY_PADDING = 5;
    private static final int DEFAULT_PADDING = 2;
    private final List<String> options = new ArrayList<String>();
    private final List<String> descriptions = new ArrayList<String>();
    private final String name;
    private final String url;
    private final String version;
    private int padding;

    /**
     * Create a usage helper.
     *
     * @param name the application name
     * @param url the application site url
     * @param version the application version
     */
    public Usage( final String name, final String url, final String version )
    {
        this.name = name;
        this.url = url;
        this.version = version;
        this.padding = DEFAULT_PADDING;
    }

    /**
     * Add an option description.
     *
     * @param option the name of the option
     * @param description the description of the option
     */
    public void addOption( final String option, final String description )
    {
        options.add( option );
        padding = Math.max( padding, option.length() + SECURITY_PADDING );
        descriptions.add( description );
    }

    /**
     * Display the usage information.
     */
    public void display()
    {
        System.out.println();
        System.out.println( "Usage: " + name + " [options] [file [file2 [directory [directory2] ...]]]" );
        System.out.println( "Version: " + version );
        System.out.println();
        System.out.println( "Options:" );
        for( int index = 0; index < options.size(); ++index )
        {
            final String string = "  -" + options.get( index );
            System.out.print( string );
            pad( string );
            System.out.println( descriptions.get( index ) );
        }
        System.out.println();
        System.out.println( "See " + url + " for more information." );
    }

    private void pad( final String string )
    {
        for( int i = padding - string.length(); i > 0; --i )
            System.out.print( ' ' );
    }
}
