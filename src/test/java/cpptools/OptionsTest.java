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
 *
 * $Id: $
 */

package cpptools;

import java.util.List;
import junit.framework.TestCase;

/**
 * @author Mathieu Champlon
 */
public class OptionsTest extends TestCase
{
    public void testHasOptionIsTrueWhenOptionIsEnabled()
    {
        assertTrue( new Options( new String[]
        {
            "-option"
        } ).hasOption( "option" ) );
        assertTrue( new Options( new String[]
        {
            "-optionvalue"
        } ).hasOption( "option" ) );
        assertTrue( new Options( new String[]
        {
            "-option=value"
        } ).hasOption( "option" ) );
        assertTrue( new Options( new String[]
        {
            "-optionproperty=value"
        } ).hasOption( "option" ) );
    }

    public void testHasOptionIsFalseWhenOptionNotEnabled()
    {
        assertFalse( new Options( new String[]
        {} ).hasOption( "option" ) );
        assertFalse( new Options( new String[]
        {
            "opt"
        } ).hasOption( "option" ) );
    }

    public void testRetrieveOptionValue()
    {
        final List<String> actual = new Options( new String[]
        {
            "-optionvalue"
        } ).getOptionValues( "option" );
        assertEquals( 1, actual.size() );
        assertEquals( "value", actual.get( 0 ) );
    }

    public void testRetrieveOptionProperty()
    {
        final List<String> actual = new Options( new String[]
        {
            "-optionproperty=value"
        } ).getOptionProperties( "option" );
        assertEquals( 1, actual.size() );
        assertEquals( "property", actual.get( 0 ) );
    }

    public void testRetrieveOptionPropertyValue()
    {
        final List<String> actual = new Options( new String[]
        {
            "-optionproperty=value"
        } ).getOptionPropertyValues( "option" );
        assertEquals( 1, actual.size() );
        assertEquals( "value", actual.get( 0 ) );
    }

    public void testRetrieveOptionPropertyValueWithoutProperty()
    {
        final List<String> actual = new Options( new String[]
        {
            "-option=value"
        } ).getOptionPropertyValues( "option" );
        assertEquals( 1, actual.size() );
        assertEquals( "value", actual.get( 0 ) );
    }

    public void testRetrieveSeveralOptionValues()
    {
        final List<String> actual = new Options( new String[]
        {
                "-optionvalue", "-optionanother_value"
        } ).getOptionValues( "option" );
        assertEquals( 2, actual.size() );
        assertEquals( "value", actual.get( 0 ) );
        assertEquals( "another_value", actual.get( 1 ) );
    }
}
