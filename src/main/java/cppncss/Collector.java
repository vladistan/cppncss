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

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import cppncss.counter.FunctionObserver;

/**
 * Collects function measurements.
 * <p>
 * The results are sorted according to the value of the first measurement.
 * <p>
 * The different measurements for a given function must be recorded one after another.
 * 
 * @author Mathieu Champlon
 */
public final class Collector implements FunctionObserver, FileObserver
{
    /**
     * @author Mathieu Champlon
     */
    private static final class MeasurementComparator implements Comparator<Measurement>, Serializable
    {
        private static final long serialVersionUID = 3425352873656018004L;

        /**
         * {@inheritDoc}
         */
        public int compare( final Measurement m1, final Measurement m2 )
        {
            return m1.compare( m2 );
        }
    }

    private final Vector<Measurement> result = new Vector<Measurement>();
    private final Comparator<Measurement> comparator = new MeasurementComparator();
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
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String name, final String function, final int line, final int count )
    {
        if( this.index.equals( name ) )
            insert( function, line, count );
        else
            update( function, line, count );
    }

    private boolean update( final String function, final int line, final int count )
    {
        final Iterator<Measurement> iterator = result.iterator();
        while( iterator.hasNext() )
            if( iterator.next().update( function, filename, line, count ) )
                return true;
        return false;
    }

    private void insert( final String function, final int line, final int count )
    {
        result.add( new Measurement( function, filename, line, count ) );
        Collections.sort( result, comparator );
        if( result.size() > threshold )
            result.remove( result.size() - 1 );
    }

    /**
     * Accept a visitor.
     * 
     * @param visitor the visitor
     */
    public void accept( final MeasurementVisitor visitor )
    {
        final Iterator<Measurement> iterator = result.iterator();
        while( iterator.hasNext() )
            iterator.next().accept( visitor );
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        this.filename = filename;
    }
}
