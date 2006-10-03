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

/**
 * Computes the average of a series of values.
 *
 * @author Mathieu Champlon
 */
public final class Average
{
    private final String label;
    private float average;
    private float count;

    /**
     * Create an average.
     *
     * @param label the label of the measurement
     * @param value the first value of the measurement
     */
    public Average( final String label, final int value )
    {
        this.label = label;
        this.average = value;
        this.count = 1;
    }

    /**
     * Add a value to the average and recompute it.
     * <p>
     * If the label does not match the label given at creation the measurement is ignored.
     *
     * @param label the label of the measurement
     * @param value the value of the measurement
     * @return whether the measurement has been accepted or not
     */
    public boolean update( final String label, final int value )
    {
        if( !this.label.equals( label ) )
            return false;
        average = (average * count + value) / (count + 1);
        ++count;
        return true;
    }

    /**
     * Accept a visitor.
     *
     * @param observer an average observer
     */
    public void accept( final AverageObserver observer )
    {
        observer.notify( label, average );
    }
}
