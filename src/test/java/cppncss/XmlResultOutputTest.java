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

package cppncss;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import tools.EasyMockTestCase;

/**
 * @author Mathieu Champlon
 */
public class XmlResultOutputTest extends EasyMockTestCase
{
    private XmlResultOutput output;
    private ByteArrayOutputStream stream;

    protected void setUp() throws Exception
    {
        stream = new ByteArrayOutputStream();
        output = new XmlResultOutput( new PrintStream( stream ) );
    }

    private Document parse( final String content ) throws DocumentException
    {
        return new SAXReader().read( new StringReader( content ) );
    }

    private Vector<String> makeLabels()
    {
        final Vector<String> labels = new Vector<String>();
        labels.add( "first label" );
        labels.add( "second label" );
        return labels;
    }

    public void testNoNotificationGeneratesEmptyRootElement() throws DocumentException
    {
        output.stop();
        final Document document = parse( stream.toString() );
        assertNotNull( document.selectSingleNode( "/cppncss" ) );
    }

    public void testLabelsNotificationGeneratesLabelsElement() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.stop();
        final Document document = parse( stream.toString() );
        assertNotNull( document.selectSingleNode( "/cppncss/measure/labels" ) );
        final List labels = document.selectNodes( "/cppncss/measure/labels/label" );
        assertEquals( 3, labels.size() );
        assertEquals( "Nr.", ((Node)labels.get( 0 )).getText() );
        assertEquals( "first label", ((Node)labels.get( 1 )).getText() );
        assertEquals( "second label", ((Node)labels.get( 2 )).getText() );
    }

    public void testTypeNameBeingTheBeginningOfLabelIsSkipped() throws DocumentException
    {
        output.notify( "first", makeLabels() );
        output.stop();
        final Document document = parse( stream.toString() );
        assertNotNull( document.selectSingleNode( "/cppncss/measure/labels" ) );
        final List labels = document.selectNodes( "/cppncss/measure/labels/label" );
        assertEquals( 2, labels.size() );
        assertEquals( "Nr.", ((Node)labels.get( 0 )).getText() );
        assertEquals( "second label", ((Node)labels.get( 1 )).getText() );
    }

    public void testItemNotificationsGenerateItemElement() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my item", 123 );
        output.notify( "type", "my item", 45 );
        output.stop();
        final Document document = parse( stream.toString() );
        assertEquals( "type", document.selectSingleNode( "/cppncss/measure/@type" ).getText() );
        assertNotNull( document.selectSingleNode( "/cppncss/measure/item" ) );
        final List values = document.selectNodes( "/cppncss/measure/item/value" );
        assertEquals( 3, values.size() );
        assertEquals( "1", ((Node)values.get( 0 )).getText() );
        assertEquals( "123", ((Node)values.get( 1 )).getText() );
        assertEquals( "45", ((Node)values.get( 2 )).getText() );
    }

    public void testMeasurementTypeNameBeingTheBeginningOfLabelIsSkipped() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.notify( "first", "item", 12 );
        output.notify( "type", "item", 42 );
        output.stop();
        final Document document = parse( stream.toString() );
        assertEquals( "type", document.selectSingleNode( "/cppncss/measure/@type" ).getText() );
        assertNotNull( document.selectSingleNode( "/cppncss/measure/item" ) );
        final List values = document.selectNodes( "/cppncss/measure/item/value" );
        assertEquals( 2, values.size() );
        assertEquals( "1", ((Node)values.get( 0 )).getText() );
        assertEquals( "42", ((Node)values.get( 1 )).getText() );
    }

    public void testAverageNotificationGeneratesAverageElement() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my label", 12.41782F );
        output.stop();
        final Document document = parse( stream.toString() );
        assertEquals( "my label", document.selectSingleNode( "/cppncss/measure/average/@label" ).getText() );
        assertEquals( "12.41782", document.selectSingleNode( "/cppncss/measure/average/@value" ).getText() );
    }

    public void testAverageTypeNameBeingTheBeginningOfLabelIsSkipped() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.notify( "lab", "label", 12f );
        output.stop();
        final Document document = parse( stream.toString() );
        assertNull( document.selectSingleNode( "/cppncss/measure/average" ) );
    }

    public void testSumNotificationGeneratesSumElement() throws DocumentException
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my label", 1242L );
        output.stop();
        final Document document = parse( stream.toString() );
        assertEquals( "my label", document.selectSingleNode( "/cppncss/measure/sum/@label" ).getText() );
        assertEquals( "1242", document.selectSingleNode( "/cppncss/measure/sum/@value" ).getText() );
    }
}
