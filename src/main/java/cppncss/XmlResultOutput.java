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

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implements an XML result output.
 *
 * @author Mathieu Champlon
 */
public final class XmlResultOutput extends AbstractResultOutput
{
    private final Document document;
    private final PrintStream stream;
    private final Element root;
    private Element current;

    /**
     * Create an XML result output.
     *
     * @param stream the stream to write to
     * @throws Exception if an error occurs
     */
    public XmlResultOutput( final PrintStream stream ) throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        document = factory.newDocumentBuilder().newDocument();
        this.stream = stream;
        this.root = document.createElement( "cppncss" );
        this.current = root;
    }

    /**
     * {@inheritDoc}
     */
    protected void printHeaders( final String type, final List<String> labels )
    {
        current = addElement( root, "measure" );
        current.setAttribute( "type", type );
        current = addElement( current, "labels" );
        addElement( current, "label" ).setTextContent( "Nr." );
        for( String label : labels )
            if( !label.startsWith( type ) )
                addElement( current, "label" ).setTextContent( label );
        current = (Element)current.getParentNode();
    }

    private Element addElement( final Element element, final String name )
    {
        final Element result = document.createElement( name );
        element.appendChild( result );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    protected void printItem( final String item )
    {
        current = (Element)current.getParentNode();
    }

    /**
     * {@inheritDoc}
     */
    protected void printMeasurement( final String label, final int count )
    {
        addElement( current, "value" ).setTextContent( Integer.toString( count ) );
    }

    /**
     * {@inheritDoc}
     */
    protected void printIndex( final String item, final int index )
    {
        current = addElement( current, "item" );
        current.setAttribute( "name", item );
        addElement( current, "value" ).setTextContent( Integer.toString( index ) );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String type, final String label, final float average )
    {
        if( !label.startsWith( type ) )
        {
            final Element element = addElement( current, "average" );
            element.setAttribute( "label", label );
            element.setAttribute( "value", Float.toString( average ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String type, final String label, final long sum )
    {
        final Element element = addElement( current, "sum" );
        element.setAttribute( "label", label );
        element.setAttribute( "value", Long.toString( sum ) );
    }

    /**
     * {@inheritDoc}
     */
    public void flush()
    {
        try
        {
            final Result result = new StreamResult( new OutputStreamWriter( stream ) );
            final TransformerFactory factory = TransformerFactory.newInstance();
            factory.setAttribute( "indent-number", 2 );
            final Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty( "indent", "yes" );
            transformer.transform( new DOMSource( root ), result );
        }
        catch( TransformerConfigurationException e )
        {
            e.printStackTrace(); // FIXME stupid checked exception
        }
        catch( TransformerException e )
        {
            e.printStackTrace(); // FIXME stupid checked exception
        }
    }
}
