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

import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class MeasurementTest extends TestCase
{
    public void testMeasurementComparedToItselfEqualsZero()
    {
        final Measurement measurement = new Measurement( "item", "filename", 12, 42 );
        assertEquals( 0, measurement.compareTo( measurement ) );
        assertEquals( measurement, measurement );
    }

    public void testMeasurementsWithDifferentCountsComparedToOneAnotherEqualZero()
    {
        final Measurement measurement1 = new Measurement( "item", "filename", 12, 42 );
        final Measurement measurement2 = new Measurement( "item", "filename", 12, 17 );
        assertEquals( 0, measurement1.compareTo( measurement2 ) );
        assertEquals( 0, measurement2.compareTo( measurement1 ) );
        assertFalse( measurement1.equals( measurement2 ) );
        assertFalse( measurement2.equals( measurement1 ) );
    }

    public void testDifferentsMeasurementsWithSameCountComparedToOneAnotherEqualOne()
    {
        final Measurement measurement1 = new Measurement( "item 1", "filename 1", 12, 42 );
        final Measurement measurement2 = new Measurement( "item 2", "filename 2", 17, 42 );
        assertEquals( 1, measurement1.compareTo( measurement2 ) );
        assertEquals( 1, measurement2.compareTo( measurement1 ) );
        assertFalse( measurement1.equals( measurement2 ) );
        assertFalse( measurement2.equals( measurement1 ) );
    }
}
