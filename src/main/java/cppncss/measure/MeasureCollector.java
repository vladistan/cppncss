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

package cppncss.measure;

import java.util.TreeSet;
import java.util.Vector;
import org.picocontainer.Startable;
import cppncss.analyzer.FileObserver;
import cppncss.counter.CounterObserver;

/**
 * Collects measures.
 * <p>
 * The results are sorted according to the value of the first measure.
 * <p>
 * The different measures for a given item must be recorded one after another.
 *
 * @author Mathieu Champlon
 */
public final class MeasureCollector implements CounterObserver, FileObserver, Startable
{
    private static final int THRESHOLD = 30;
    private final TreeSet<Measure> result;
    private final MeasureObserver observer;
    private String index;
    private String filename;
    private final Vector<String> labels;

    /**
     * Create a measure collector.
     *
     * @param observer an observer to be notified of the results
     */
    public MeasureCollector( final MeasureObserver observer )
    {
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        this.observer = observer;
        this.result = new TreeSet<Measure>();
        this.labels = new Vector<String>();
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final String item, final int line, final int count )
    {
        if( index == null )
            index = label;
        if( !labels.contains( label ) )
            labels.add( label );
        if( this.index.equals( label ) )
            insert( item, line, count );
        else
            update( item, line, count );
    }

    private boolean update( final String item, final int line, final int count )
    {
        for( Measure measure : result )
            if( measure.update( item, filename, line, count ) )
                return true;
        return false;
    }

    private void insert( final String item, final int line, final int count )
    {
        result.add( new Measure( item, filename, line, count ) );
        if( result.size() > THRESHOLD )
            result.remove( result.last() );
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        this.filename = filename;
    }

    /**
     * {@inheritDoc}
     */
    public void start()
    {
        observer.notify( labels );
        for( Measure measure : result )
            measure.accept( observer );
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
    }
}