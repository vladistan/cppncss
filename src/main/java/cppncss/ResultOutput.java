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

/**
 * Defines a component to output results.
 *
 * @author Mathieu Champlon
 */
public interface ResultOutput
{
    /**
     * Notify about a list of labels.
     *
     * @param type the type of measure
     * @param labels a list of measurements
     */
    void notify( final String type, final List<String> labels );

    /**
     * Notify about a measurement.
     *
     * @param type the type of measure
     * @param item the measured item
     * @param count the result
     */
    void notify( final String type, final String item, final int count );

    /**
     * Notify about an average.
     *
     * @param type the type of measure
     * @param item the measured item
     * @param average the result
     */
    void notify( final String type, final String item, final float average );

    /**
     * Notify about a sum.
     *
     * @param type the type of measure
     * @param item the measured item
     * @param sum the result
     */
    void notify( final String type, final String item, final long sum );

    /**
     * Notify about the end of the result flow.
     */
    void flush();
}
