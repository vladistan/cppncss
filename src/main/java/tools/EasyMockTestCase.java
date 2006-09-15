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

package tools;

import java.util.Iterator;
import java.util.Vector;
import junit.framework.TestCase;
import org.easymock.classextension.EasyMock;

/**
 * Extends a JUnit TestCase with several EasyMock related features.
 * <p>
 * This extension provides the following benefits :
 * <ul>
 * <li>supports interface and concrete classes through the same creation method
 * <li>resets all expectations before each test
 * <li>verifies all expectations after each test
 * <li>provides helper methods to reset, replay or verify all control objects at once
 * </ul>
 * <p>
 * As a consequence the tests are easier to write and end up being simpler.
 * <p>
 * TODO add nice and strict mock support
 *
 * @see <a href="http://junit.org">JUnit</a>
 * @see <a href="http://easymock.org">EasyMock</a>
 * @author Mathieu Champlon
 */
public class EasyMockTestCase extends TestCase
{
    /**
     * The mock objects.
     */
    private final Vector<Object> mocks;
    /**
     * Whether a forced replay is needed before verifying all controls.
     */
    private boolean mustForceReplay;

    /**
     * Create an easy mock test case.
     */
    public EasyMockTestCase()
    {
        mocks = new Vector<Object>();
        mustForceReplay = true;
    }

    /**
     * {@inheritDoc}
     */
    public final void runBare() throws Throwable
    {
        try
        {
            setUp();
            reset();
            try
            {
                runTest();
            }
            finally
            {
                tearDown();
            }
            if( mustForceReplay )
                replay();
            verify();
        }
        finally
        {
            mocks.clear();
            mustForceReplay = true;
        }
    }

    /**
     * Factory method to create a mock object of a given type.
     *
     * @param <T> the type of the created mock object
     * @param type the type of the mock object to create
     * @return the created mock object
     */
    protected final <T> T createMock( final Class<T> type )
    {
        final T mock = EasyMock.createMock( type );
        mocks.add( mock );
        return mock;
    }

    /**
     * Reset all mock objects expectations.
     * <p>
     * The state of mock objects is then the same as at the beginning of the test.
     */
    protected final void reset()
    {
        mustForceReplay = true;
        final Iterator i = mocks.iterator();
        while( i.hasNext() )
            EasyMock.reset( i.next() );
    }

    /**
     * Set all mock objects to replay mode.
     */
    protected final void replay()
    {
        mustForceReplay = false;
        final Iterator i = mocks.iterator();
        while( i.hasNext() )
            EasyMock.replay( i.next() );
    }

    /**
     * Verify all mock objects expectations.
     * <p>
     * This method is automatically called at the end of each test.
     */
    protected final void verify()
    {
        final Iterator i = mocks.iterator();
        while( i.hasNext() )
            EasyMock.verify( i.next() );
    }
}
