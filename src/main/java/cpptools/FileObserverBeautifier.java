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

package cpptools;

import java.util.List;

/**
 * Beautifies the filename by removing a given prefix.
 *
 * @author Mathieu Champlon
 */
public final class FileObserverBeautifier implements FileObserver
{
    private final String prefix;
    private final FileObserver observer;

    /**
     * Create a file observer beautifier.
     *
     * @param options the options
     * @param observer the file observer
     */
    public FileObserverBeautifier( final Options options, final FileObserver observer )
    {
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        this.prefix = getPrefix( options );
        this.observer = observer;
    }

    private String getPrefix( final Options options )
    {
        final List<String> prefixes = options.getOptionPropertyValues( "p" );
        if( prefixes.size() > 0 )
            return prefixes.get( 0 );
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        observer.changed( filter( filename ) );
    }

    private String filter( final String filename )
    {
        if( filename.startsWith( prefix ) )
            return filename.substring( prefix.length() );
        return filename;
    }
}
