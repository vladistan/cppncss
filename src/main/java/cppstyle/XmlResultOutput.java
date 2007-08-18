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

package cppstyle;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
 * Implements an XML logger.
 *
 * @author Mathieu Champlon
 */
public final class XmlResultOutput implements ResultOutput
{
    private final PrintStream stream;
    private final Document document;
    private final Element root;
    private Element current;

    /**
     * Create an AsciiCheckListener.
     *
     * @param stream the output stream
     * @throws ParserConfigurationException if an error occurs
     */
    public XmlResultOutput( final PrintStream stream ) throws ParserConfigurationException
    {
        if( stream == null )
            throw new IllegalArgumentException( "parameter 'stream' is null" );
        this.stream = stream;
        this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        this.root = document.createElement( "cppstyle" );
        this.current = root;
    }

    /**
     * {@inheritDoc}
     */
    public void fail( final String reason, final int start, final int end )
    {
        if( start == end )
            fail( reason, start );
        else
        {
            final Element error = addElement( current, "error" );
            error.setAttribute( "line", start + "-" + end );
            error.setAttribute( "message", reason );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void fail( final String reason, final int line )
    {
        final Element error = addElement( current, "error" );
        error.setAttribute( "line", Integer.toString( line ) );
        error.setAttribute( "message", reason );
    }

    /**
     * {@inheritDoc}
     */
    public void fail( final String reason )
    {
        final Element error = addElement( current, "error" );
        error.setAttribute( "message", reason );
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        current = addElement( root, "file" );
        current.setAttribute( "name", filename );
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
        catch( final TransformerConfigurationException e )
        {
            throw new RuntimeException( e );
        }
        catch( final TransformerException e )
        {
            throw new RuntimeException( e );
        }
    }
}
