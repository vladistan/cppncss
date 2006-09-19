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

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import tools.EasyMockTestCase;
import cppast.ParseException;
import cppast.Parser;

/**
 * @author Mathieu Champlon
 */
public class CcnTest extends EasyMockTestCase
{
    private Parser parser;
    private FunctionObserver observer;

    /**
     * {@inheritDoc}
     */
    protected void setUp()
    {
        observer = createMock( FunctionObserver.class );
        parser = new Parser( new StringReader( "" ) );
    }

    private void parse( final String name ) throws IOException, ParseException
    {
        final URL resource = CcnTest.class.getClassLoader().getResource( name );
        if( resource == null )
            throw new IOException( "resource not found : " + name );
        parser.ReInit( resource.openStream() );
        parser.translation_unit().jjtAccept( new FunctionVisitor( new CcnCounter( observer ) ), null );
    }

    public void testMethodDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyMethod()", 3, 1 );
        replay();
        parse( "test001.h" );
    }

    public void testSeparateMethodDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyMethod()", 1, 1 );
        replay();
        parse( "test001.cpp" );
    }

    public void testVirtualMethodDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyMethod()", 3, 1 );
        replay();
        parse( "test002.h" );
    }

    public void testMethodDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test003.h" );
    }

    public void testFunctionDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 1 );
        replay();
        parse( "test004.h" );
    }

    public void testFunctionDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test005.h" );
    }

    public void testConstructorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyClass()", 3, 1 );
        replay();
        parse( "test006.h" );
    }

    public void testConstructorDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test007.h" );
    }

    public void testSeparateConstructorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyClass()", 1, 1 );
        replay();
        parse( "test007.h" );
        parse( "test007.cpp" );
    }

    public void testDestructorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::~MyClass()", 3, 1 );
        replay();
        parse( "test008.h" );
    }

    public void testDestructorDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test009.h" );
    }

    public void testSeparateDestructorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::~MyClass()", 1, 1 );
        replay();
        parse( "test009.h" );
        parse( "test009.cpp" );
    }

    public void testOperatorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::operator ==( const MyClass& )", 3, 1 );
        replay();
        parse( "test010.h" );
    }

    public void testOperatorDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test011.h" );
    }

    public void testSeparateOperatorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::operator ==( const MyClass& )", 1, 1 );
        replay();
        parse( "test011.h" );
        parse( "test011.cpp" );
    }

    public void testConstMethodWithReturnTypeAndParametersDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyMethod( int, float&, const double& )", 3, 1 );
        replay();
        parse( "test012.h" );
    }

    public void testConstMethodWithReturnTypeAndParametersDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test013.h" );
    }

    public void testConstMethodWithReturnTypeAndParametersSeparateDefinitionIsValid() throws IOException,
            ParseException
    {
        observer.notify( "MyClass::MyMethod( int, float&, const double& )", 1, 1 );
        replay();
        parse( "test013.h" );
        parse( "test013.cpp" );
    }

    public void testConstructorWithParametersDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyClass( int, float&, const double )", 3, 1 );
        replay();
        parse( "test014.h" );
    }

    public void testConstructorWithParametersDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test015.h" );
    }

    public void testConstructorWithParametersSeparateDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::MyClass( int, float&, const double )", 1, 1 );
        replay();
        parse( "test015.h" );
        parse( "test015.cpp" );
    }

    public void testConversionOperatorDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::operator const char*()", 3, 1 );
        replay();
        parse( "test016.h" );
    }

    public void testConversionOperatorDeclarationIsInvalid() throws IOException, ParseException
    {
        replay();
        parse( "test017.h" );
    }

    public void testConversionOperatorSeparateDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "MyClass::operator char*()", 1, 1 );
        replay();
        parse( "test017.h" );
        parse( "test017.cpp" );
    }

    public void testFunctionWithDirectControlFlowHasCcnValueOfOne() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 1 );
        replay();
        parse( "test018.cpp" );
    }

    public void testFunctionWithIfHasCcnValueOfTwo() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parse( "test019.cpp" );
    }

    public void testFunctionWithForHasCcnValueOfTwo() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parse( "test020.cpp" );
    }

    public void testFunctionWithWhileHasCcnValueOfTwo() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parse( "test021.cpp" );
    }

    public void testFunctionWithSwitchIncrementsOneCcnPerCase() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 3 );
        replay();
        parse( "test022.cpp" );
    }

    public void testFunctionWithTryCatchIncrementsOneCcnPerCatch() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 3 );
        replay();
        parse( "test023.cpp" );
    }

    public void testFunctionWithDoWhileHasCcnValueOfTwo() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 2 );
        replay();
        parse( "test024.cpp" );
    }

    public void testFunctionsWithHiddenIfHaveCcnValueOfTwo() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 2, 2 );
        observer.notify( "MyOtherFunction()", 7, 2 );
        observer.notify( "MyThirdFunction()", 12, 2 );
        observer.notify( "MyAssignmentFunction()", 17, 2 );
        observer.notify( "MyAssignmentOtherFunction()", 22, 2 );
        observer.notify( "MyAssignmentThirdFunction()", 27, 2 );
        replay();
        parse( "test025.cpp" );
    }

    public void testFunctionInNamespaceDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "my_namespace::MyFunction()", 4, 1 );
        replay();
        parse( "test026.cpp" );
    }

    public void testFunctionInAnonymousNamespaceDefinitionIsValid() throws IOException, ParseException
    {
        observer.notify( "anonymous::MyFunction()", 4, 1 );
        replay();
        parse( "test027.cpp" );
    }

    public void testSeparateDestructorDefinitionInNamespaceIsValid() throws IOException, ParseException
    {
        observer.notify( "my_namespace::MyClass::~MyClass()", 4, 1 );
        replay();
        parse( "test028.h" );
        parse( "test028.cpp" );
    }

    public void testSeparateDestructorDefinitionUsingNamespaceIsValid() throws IOException, ParseException
    {
        observer.notify( "my_namespace::MyClass::~MyClass()", 3, 1 );
        replay();
        parse( "test029.h" );
        parse( "test029.cpp" );
    }

    public void testSeparateDestructorDefinitionWithNamespacePrefixIsValid() throws IOException, ParseException
    {
        observer.notify( "my_namespace::MyClass::~MyClass()", 1, 1 );
        replay();
        parse( "test030.h" );
        parse( "test030.cpp" );
    }

    public void testUnaryTildeOperatorIsNotDestructor() throws IOException, ParseException
    {
        observer.notify( "MyFunction()", 1, 1 );
        replay();
        parse( "test031.cpp" );
    }
}
