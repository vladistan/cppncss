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

package cppncss.counter;

import cppast.AbstractVisitor;

/**
 * Factorizes counters common behaviours.
 *
 * @author Mathieu Champlon
 */
public class AbstractCounter extends AbstractVisitor implements Counter
{
    private final String name;
    private final FunctionObserver observer;
    private int count;

    /**
     * Create an abstract counter.
     *
     * @param name the name of the counter
     * @param observer a function observer
     */
    public AbstractCounter( final String name, final FunctionObserver observer )
    {
        if( observer == null )
            throw new IllegalArgumentException( "argument 'observer' is null" );
        this.name = name;
        this.observer = observer;
        this.count = 0;
    }

    /**
     * {@inheritDoc}
     */
    public final void flush( final String function, final int line )
    {
        observer.notify( name, function, line, count );
        count = 0;
    }

    /**
     * Increments the counter.
     */
    protected final void increment()
    {
        ++count;
    }
}
