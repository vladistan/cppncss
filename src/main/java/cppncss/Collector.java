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
 *
 * $Id: $
 */

package cppncss;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Mathieu Champlon
 */
public class Collector implements CcnObserver
{
    private final Vector<Function> result;
    private final Comparator<Function> comparator;
    private final int THRESHOLD = 30;

    public Collector()
    {
        result = new Vector<Function>();
        comparator = new Comparator<Function>()
        {
            public int compare( final Function f1, final Function f2 )
            {
                return f1.compare( f2 );
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public void notifyCcn( final String function, final int count )
    {
        result.add( new Function( function, count ) );
        Collections.sort( result, comparator );
        if( result.size() > THRESHOLD )
            result.remove( result.size() - 1 );
    }

    /**
     * {@inheritDoc}
     */
    public void notifyNcss( final String function, final int count )
    {
        // TODO Auto-generated method stub
    }

    public void display()
    {
        System.out.println( "CCN" );
        final Iterator<Function> iterator = result.iterator();
        while( iterator.hasNext() )
            System.out.println( iterator.next().toString() );
    }
}
