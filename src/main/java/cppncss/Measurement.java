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

/**
 * Provides a result measurement for functions.
 *
 * @author Mathieu Champlon
 */
public class Measurement
{
    private final String function;
    private final int count;
    private final Vector<Integer> counts;

    /**
     * Create a measurement.
     *
     * @param function the name of the function
     * @param count the value of the measurement
     */
    public Measurement( final String function, final int count )
    {
        if( function == null )
            throw new IllegalArgumentException( "argument 'function' is null" );
        if( count < 0 )
            throw new IllegalArgumentException( "argument 'count' is < 0" );
        this.function = function;
        this.count = count;
        counts = new Vector<Integer>();
    }

    /**
     * Compare to another measurement for sorting purpose.
     *
     * @param other the compared measurement
     * @return the difference between the other measurement value and the value of this measurement
     */
    public final int compare( final Measurement other )
    {
        return other.count - count;
    }

    /**
     * {@inheritDoc}
     */
    public final String toString()
    {
        return count + " " + counts.toString() + " " + function;
    }

    /**
     * Add a measurement value to the recorder values.
     * <p>
     * If the function name does not match the name of the measurement the value is not recorded.
     *
     * @param function the function name
     * @param count the measurement to record
     * @return whether the measurement has been added or not
     */
    public final boolean update( final String function, final int count )
    {
        if( !this.function.equals( function ) )
            return false;
        counts.add( count );
        return true;
    }
}
