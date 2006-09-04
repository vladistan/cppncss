
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
    private final Vector<Object> mocks_;
    /**
     * Whether a forced replay is needed before verifying all controls.
     */
    private boolean mustForceReplay_;

    /**
     * Create an easy mock test case.
     */
    public EasyMockTestCase()
    {
        mocks_ = new Vector<Object>();
        mustForceReplay_ = true;
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
            if( mustForceReplay_ )
                replay();
            verify();
        }
        finally
        {
            mocks_.clear();
            mustForceReplay_ = true;
        }
    }

    /**
     * Factory method to create a mock object of a given type.
     *
     * @param type the type of the mock object to create
     * @return the created mock object
     */
    protected final <T> T createMock( final Class<T> type )
    {
        final T mock = EasyMock.createMock( type );
        mocks_.add( mock );
        return mock;
    }

    /**
     * Reset all mock objects expectations.
     * <p>
     * The state of mock objects is then the same as at the beginning of the test.
     */
    protected final void reset()
    {
        mustForceReplay_ = true;
        final Iterator i = mocks_.iterator();
        while( i.hasNext() )
            EasyMock.reset( i.next() );
    }

    /**
     * Set all mock objects to replay mode.
     */
    protected final void replay()
    {
        mustForceReplay_ = false;
        final Iterator i = mocks_.iterator();
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
        final Iterator i = mocks_.iterator();
        while( i.hasNext() )
            EasyMock.verify( i.next() );
    }

    public final void testMethodToRemoveJUnitWarningAboutMissingTests()
    {
    }
}
