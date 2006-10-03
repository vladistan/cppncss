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

import java.util.Vector;
import org.picocontainer.Startable;
import cppncss.counter.CounterObserver;

/**
 * Collects average of measures.
 * <p>
 * The results are sorted according to the value of the first measure.
 * <p>
 * The different measures for a given item must be recorded one after another.
 *
 * @author Mathieu Champlon
 */
public final class AverageCollector implements CounterObserver, FileObserver, Startable
{
    private final Vector<Average> result;
    private final AverageObserver observer;

    /**
     * Create a collector indexed by a given measure name.
     *
     * @param observer a measure observer to be notified of the results
     * @param threshold the number of measures to keep
     */
    public AverageCollector( final AverageObserver observer )
    {
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        this.observer = observer;
        this.result = new Vector<Average>();
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final String item, final int line, final int count )
    {
        if( !update( label, count ) )
            result.add( new Average( label, count, observer ) );
    }

    private boolean update( String label, int count )
    {
        for( Average average : result )
            if( average.update( label, count ) )
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void start()
    {
        for( Average average : result )
            average.start();
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void changed( String filename )
    {
        // TODO Auto-generated method stub

    }
}
