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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import cpptools.EasyMockTestCase;

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

    private Document parse() throws ParserConfigurationException, SAXException, IOException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse( new ByteArrayInputStream( stream.toString().getBytes() ) );
    }

    private NodeList selectNodes( final Document document, final String expression ) throws XPathExpressionException
    {
        final XPathFactory factory = XPathFactory.newInstance();
        final XPathExpression expr = factory.newXPath().compile( expression );
        return (NodeList)expr.evaluate( document, XPathConstants.NODESET );
    }

    private Node selectSingleNode( final Document document, final String expression ) throws XPathExpressionException
    {
        final NodeList nodes = selectNodes( document, expression );
        if( nodes.getLength() > 0 )
            return nodes.item( 0 );
        return null;
    }

    private List<String> makeLabels()
    {
        final List<String> labels = new ArrayList<String>();
        labels.add( "first label" );
        labels.add( "second label" );
        return labels;
    }

    public void testNoNotificationGeneratesEmptyRootElement() throws Exception
    {
        output.flush();
        final Document document = parse();
        assertNotNull( selectSingleNode( document, "/cppncss" ) );
    }

    public void testLabelsNotificationGeneratesLabelsElement() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.flush();
        final Document document = parse();
        assertNotNull( selectSingleNode( document, "/cppncss/measure/labels" ) );
        final NodeList labels = selectNodes( document, "/cppncss/measure/labels/label" );
        assertEquals( 3, labels.getLength() );
        assertEquals( "Nr.", labels.item( 0 ).getTextContent() );
        assertEquals( "first label", labels.item( 1 ).getTextContent() );
        assertEquals( "second label", labels.item( 2 ).getTextContent() );
    }

    public void testTypeNameBeingTheBeginningOfLabelIsSkipped() throws Exception
    {
        output.notify( "first", makeLabels() );
        output.flush();
        final Document document = parse();
        assertNotNull( selectSingleNode( document, "/cppncss/measure/labels" ) );
        final NodeList labels = selectNodes( document, "/cppncss/measure/labels/label" );
        assertEquals( 2, labels.getLength() );
        assertEquals( "Nr.", labels.item( 0 ).getTextContent() );
        assertEquals( "second label", labels.item( 1 ).getTextContent() );
    }

    public void testItemNotificationsGenerateItemElement() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my item", 123 );
        output.notify( "type", "my item", 45 );
        output.flush();
        final Document document = parse();
        assertEquals( "type", selectSingleNode( document, "/cppncss/measure/@type" ).getTextContent() );
        assertNotNull( selectSingleNode( document, "/cppncss/measure/item" ) );
        final NodeList values = selectNodes( document, "/cppncss/measure/item/value" );
        assertEquals( 3, values.getLength() );
        assertEquals( "1", values.item( 0 ).getTextContent() );
        assertEquals( "123", values.item( 1 ).getTextContent() );
        assertEquals( "45", values.item( 2 ).getTextContent() );
    }

    public void testMeasurementTypeNameBeingTheBeginningOfLabelIsSkipped() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.notify( "first", "item", 12 );
        output.notify( "type", "item", 42 );
        output.flush();
        final Document document = parse();
        assertEquals( "type", selectSingleNode( document, "/cppncss/measure/@type" ).getTextContent() );
        assertNotNull( selectSingleNode( document, "/cppncss/measure/item" ) );
        final NodeList values = selectNodes( document, "/cppncss/measure/item/value" );
        assertEquals( 2, values.getLength() );
        assertEquals( "1", values.item( 0 ).getTextContent() );
        assertEquals( "42", values.item( 1 ).getTextContent() );
    }

    public void testAverageNotificationGeneratesAverageElement() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my label", 12.41782F );
        output.flush();
        final Document document = parse();
        assertEquals( "my label", selectSingleNode( document, "/cppncss/measure/average/@label" ).getTextContent() );
        assertEquals( "12.41782", selectSingleNode( document, "/cppncss/measure/average/@value" ).getTextContent() );
    }

    public void testAverageTypeNameBeingTheBeginningOfLabelIsSkipped() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.notify( "lab", "label", 12f );
        output.flush();
        final Document document = parse();
        assertNull( selectSingleNode( document, "/cppncss/measure/average" ) );
    }

    public void testSumNotificationGeneratesSumElement() throws Exception
    {
        output.notify( "type", makeLabels() );
        output.notify( "type", "my label", 1242L );
        output.flush();
        final Document document = parse();
        assertEquals( "my label", selectSingleNode( document, "/cppncss/measure/sum/@label" ).getTextContent() );
        assertEquals( "1242", selectSingleNode( document, "/cppncss/measure/sum/@value" ).getTextContent() );
    }
}
