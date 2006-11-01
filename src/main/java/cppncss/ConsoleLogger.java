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

import java.io.PrintStream;
import java.util.List;
import java.util.Locale;
import cppncss.average.AverageObserver;
import cppncss.measure.MeasureObserver;
import cppncss.sum.SumObserver;

/**
 * Implements a console measure logger.
 *
 * @author Mathieu Champlon
 */
public final class ConsoleLogger implements MeasureObserver, AverageObserver, SumObserver
{
    private int current;
    private int index;
    private final String item;
    private final PrintStream stream = System.out;
    private List<String> labels;

    /**
     * Create a logger to the console.
     *
     * @param item the name of the measured item
     */
    public ConsoleLogger( final String item )
    {
        this.current = 0;
        this.index = 0;
        this.item = item;
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final List<String> labels )
    {
        this.labels = labels;
        printHeaders( this.labels );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String item, final int count )
    {
        if( current == 0 )
            printIndex( ++index );
        printMeasurement( labels.get( current ), count );
        ++current;
        current %= labels.size();
        if( current == 0 )
            printItem( item );
    }

    private void printHeaders( final List<String> labels )
    {
        stream.println();
        stream.print( "Nr. " );
        for( String label : labels )
            if( !label.startsWith( item ) )
                stream.print( label + " " );
        stream.println( item );
    }

    private void printIndex( final int index )
    {
        stream.format( "%3d", index );
    }

    private void printMeasurement( final String label, final int count )
    {
        if( !label.startsWith( item ) )
            stream.format( " %" + label.length() + "d", count );
    }

    private void printItem( final String item )
    {
        stream.format( " %s", item );
        stream.println();
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final float average )
    {
        if( !label.startsWith( item ) )
        {
            stream.format( Locale.US, "Average %s %s: %.2f", item, label, average );
            stream.println();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final long sum )
    {
        stream.println( item + " " + label + ": " + sum );
    }
}
