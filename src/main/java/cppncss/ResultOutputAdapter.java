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

import java.util.List;
import cppncss.measure.AverageObserver;
import cppncss.measure.MeasureObserver;
import cppncss.measure.SumObserver;

/**
 * Provides an adapter between measurement observers and result outputs.
 *
 * @author Mathieu Champlon
 */
public final class ResultOutputAdapter implements MeasureObserver, AverageObserver, SumObserver
{
    private final String type;
    private final ResultOutput output;

    /**
     * Create a result handler.
     *
     * @param type the type of measurement
     * @param output the output
     */
    public ResultOutputAdapter( final String type, final ResultOutput output )
    {
        this.type = type;
        this.output = output;
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final List<String> labels )
    {
        output.notify( type, labels );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String item, final int count )
    {
        output.notify( type, item, count );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final float average )
    {
        output.notify( type, label, average );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String label, final long sum )
    {
        output.notify( type, label, sum );
    }
}
