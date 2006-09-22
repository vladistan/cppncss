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

import java.util.TreeSet;
import cppncss.counter.CounterObserver;

/**
 * Collects measurements.
 * <p>
 * The results are sorted according to the value of the first measurement.
 * <p>
 * The different measurements for a given item must be recorded one after another.
 *
 * @author Mathieu Champlon
 */
public final class Collector implements CounterObserver, FileObserver
{
    private final TreeSet<Measurement> result;
    private final String index;
    private final int threshold;
    private String filename;

    /**
     * Create a collector indexed by a given measurement name.
     *
     * @param index the index measurement name
     * @param threshold the number of measurements to keep
     */
    public Collector( final String index, final int threshold )
    {
        if( index == null )
            throw new IllegalArgumentException( "argument 'index' is null" );
        if( threshold <= 0 )
            throw new IllegalArgumentException( "threshold is <= 0" );
        this.index = index;
        this.threshold = threshold;
        this.result = new TreeSet<Measurement>();
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final String item, final int line, final int count )
    {
        if( this.index.equals( label ) )
            insert( item, line, count );
        else
            update( item, line, count );
    }

    private boolean update( final String item, final int line, final int count )
    {
        for( Measurement measurement : result )
            if( measurement.update( item, filename, line, count ) )
                return true;
        return false;
    }

    private void insert( final String item, final int line, final int count )
    {
        result.add( new Measurement( item, filename, line, count ) );
        if( result.size() > threshold )
            result.remove( result.last() );
    }

    /**
     * Accept a visitor.
     *
     * @param visitor the visitor
     */
    public void accept( final MeasurementVisitor visitor )
    {
        for( Measurement measurement : result )
            measurement.accept( visitor );
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        this.filename = filename;
    }
}
