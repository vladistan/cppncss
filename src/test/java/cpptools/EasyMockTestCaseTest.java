
package cpptools;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import junit.framework.TestResult;

/**
 * Test case for EasyMockTestCase.
 *
 * @author Mathieu Champlon
 * @version $Revision: 1064 $ $Date: 2005-08-27 03:11:01 +0900 (sam., 27 août 2005) $
 */
public class EasyMockTestCaseTest extends TestCase
{
    /**
     * Tested object.
     */
    private EasyMockTestCase test;

    protected void setUp()
    {
        test = new EasyMockTestCase();
    }

    public void testCreateMockFromInterfaceProvidesNonNullObject()
    {
        assertNotNull( test.createMock( Collection.class ) );
    }

    public void testCreateMockFromClassProvidesNonNullObject()
    {
        assertNotNull( test.createMock( ArrayList.class ) );
    }

    public void testCreateMockAndRunTestPasses()
    {
        final EasyMockTestCase test = new EasyMockTestCase()
        {
            protected void setUp()
            {
                createMock( Collection.class );
            }

            protected void runTest()
            {
            }
        };
        assertSuccess( test );
    }

    public void testCreateMockWithExpectationAndRunTestFails()
    {
        final EasyMockTestCase test = new EasyMockTestCase()
        {
            private Collection< ? > mock;

            protected void setUp()
            {
                mock = createMock( Collection.class );
            }

            protected void runTest()
            {
                mock.add( null );
            }
        };
        assertError( test );
    }

    public void testCreateMockWithExpectationAndRunTestPasses()
    {
        final EasyMockTestCase test = new EasyMockTestCase()
        {
            private Collection< ? > mock;

            protected void setUp()
            {
                mock = createMock( Collection.class );
            }

            protected void runTest()
            {
                mock.add( null );
                replay();
                mock.add( null );
            }
        };
        assertError( test );
    }

    private void assertSuccess( final TestCase test )
    {
        final TestResult result = test.run();
        assertEquals( 1, result.runCount() );
        assertEquals( 0, result.failureCount() );
        assertEquals( 0, result.errorCount() );
    }

    private void assertError( final TestCase test )
    {
        final TestResult result = test.run();
        assertEquals( 1, result.runCount() );
        assertEquals( 0, result.failureCount() );
        assertEquals( 1, result.errorCount() );
    }
}
