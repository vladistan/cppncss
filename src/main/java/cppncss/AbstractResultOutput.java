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

import java.util.ArrayList;
import java.util.List;

/**
 * Captures behaviours common to all result outputs.
 *
 * @author Mathieu Champlon
 */
public abstract class AbstractResultOutput implements ResultOutput
{
    private final List<String> labels = new ArrayList<String>();
    private int current;
    private int index;

    /**
     * {@inheritDoc}
     */
    public final void notify( final String type, final List<String> labels )
    {
        this.labels.clear();
        this.labels.addAll( labels );
        this.current = 0;
        this.index = 0;
        printHeaders( type, labels );
    }

    /**
     * {@inheritDoc}
     */
    public final void notify( final String type, final String item, final int count )
    {
        if( current == 0 )
            printIndex( item, ++index );
        final String label = labels.get( current );
        if( !label.startsWith( type ) )
            printMeasurement( label, count );
        ++current;
        current %= labels.size();
        if( current == 0 )
            printItem( item );
    }

    /**
     * Print measurements headers.
     *
     * @param type the type of the measurement
     * @param labels a list of all measurement names
     */
    protected abstract void printHeaders( String type, List<String> labels );

    /**
     * Print the index of an item.
     *
     * @param item the name of the item
     * @param index the index
     */
    protected abstract void printIndex( String item, int index );

    /**
     * Print an item.
     *
     * @param item the name of the item
     */
    protected abstract void printItem( String item );

    /**
     * Print a measurement.
     *
     * @param label the name of the measurement
     * @param count the result value of the measurement
     */
    protected abstract void printMeasurement( String label, int count );
}
