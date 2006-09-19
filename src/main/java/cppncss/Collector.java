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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/**
 * Collects function measurements.
 * <p>
 * The results are sorted according to the value of the first measurement.
 * <p>
 * The different measurements for a given function must be recorded one after another.
 *
 * @author Mathieu Champlon
 */
public class Collector implements FunctionObserver
{
    private final Vector<Measurement> result = new Vector<Measurement>();
    private static final int THRESHOLD = 30;

    /**
     * {@inheritDoc}
     */
    public final void notify( final String function, final int line, final int count )
    {
        if( !update( function, line, count ) )
            insert( function, line, count );
    }

    private boolean update( final String function, final int line, final int count )
    {
        final Iterator<Measurement> iterator = result.iterator();
        while( iterator.hasNext() )
            if( iterator.next().update( function, line, count ) )
                return true;
        return false;
    }

    private void insert( final String function, final int line, final int count )
    {
        result.add( new Measurement( function, line, count ) );
        Collections.sort( result, new Comparator<Measurement>()
        {
            public int compare( final Measurement m1, final Measurement m2 )
            {
                return m1.compare( m2 );
            }
        } );
        if( result.size() > THRESHOLD )
            result.remove( result.size() - 1 );
    }

    /**
     * Display results to <em>System.out</em>.
     */
    public final void display()
    {
        System.out.println( "NCSS [CCN]" );
        final Iterator<Measurement> iterator = result.iterator();
        while( iterator.hasNext() )
            System.out.println( iterator.next().toString() );
    }
}
