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
public class MeasureTest extends TestCase
{
    public void testMeasureComparedToItselfEqualsZero()
    {
        final Measure measure = new Measure( "item", "filename", 12, 42 );
        assertEquals( 0, measure.compareTo( measure ) );
        assertEquals( measure, measure );
    }

    public void testMeasuresWithDifferentCountsComparedToOneAnotherEqualZero()
    {
        final Measure measure1 = new Measure( "item", "filename", 12, 42 );
        final Measure measure2 = new Measure( "item", "filename", 12, 17 );
        assertEquals( 0, measure1.compareTo( measure2 ) );
        assertEquals( 0, measure2.compareTo( measure1 ) );
        assertFalse( measure1.equals( measure2 ) );
        assertFalse( measure2.equals( measure1 ) );
    }

    public void testDifferentsMeasuresWithSameCountComparedToOneAnotherEqualOne()
    {
        final Measure measure1 = new Measure( "item 1", "filename 1", 12, 42 );
        final Measure measure2 = new Measure( "item 2", "filename 2", 17, 42 );
        assertEquals( 1, measure1.compareTo( measure2 ) );
        assertEquals( 1, measure2.compareTo( measure1 ) );
        assertFalse( measure1.equals( measure2 ) );
        assertFalse( measure2.equals( measure1 ) );
    }
}
