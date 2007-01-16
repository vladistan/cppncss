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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import tools.Component;

/**
 * Implements an xml result output.
 *
 * @author Mathieu Champlon
 */
public final class XmlResultOutput extends AbstractResultOutput implements Component
{
    private final PrintStream stream;
    private final Element root;
    private Element node;

    /**
     * Create an xml result output.
     *
     * @param stream the stream to write to
     */
    public XmlResultOutput( final PrintStream stream )
    {
        this.stream = stream;
        this.node = DocumentHelper.createDocument().addElement( "cppncss" );
        this.root = node;
    }

    /**
     * {@inheritDoc}
     */
    protected void printHeaders( final String type, final List<String> labels )
    {
        node = root.addElement( "measure" );
        node.addAttribute( "type", type );
        node = node.addElement( "labels" );
        node.addElement( "label" ).addText( "Nr." );
        for( String label : labels )
            if( !label.startsWith( type ) )
                node.addElement( "label" ).addText( label );
        node = node.getParent();
    }

    /**
     * {@inheritDoc}
     */
    protected void printItem( final String item )
    {
        node = node.getParent();
    }

    /**
     * {@inheritDoc}
     */
    protected void printMeasurement( final String label, final int count )
    {
        node.addElement( "value" ).addText( Integer.toString( count ) );
    }

    /**
     * {@inheritDoc}
     */
    protected void printIndex( final String item, final int index )
    {
        node = node.addElement( "item" );
        node.addAttribute( "name", item );
        node.addElement( "value" ).addText( Integer.toString( index ) );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String type, final String label, final float average )
    {
        if( !label.startsWith( type ) )
            node.addElement( "average" ).addAttribute( "label", label )
                    .addAttribute( "value", Float.toString( average ) ); // FIXME format 2 decimals
        // stream.format( Locale.US, " <average label=\"%s\" value=\"%.2f\"/>", label, average );
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String type, final String label, final long sum )
    {
        node.addElement( "sum" ).addAttribute( "label", label ).addAttribute( "value", Long.toString( sum ) );
    }

    /**
     * {@inheritDoc}
     */
    public void start()
    {
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        try
        {
            new XMLWriter( stream, OutputFormat.createPrettyPrint() ).write( root );
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace(); // FIXME stupid checked exception
        }
        catch( IOException e )
        {
            e.printStackTrace(); // FIXME stupid checked exception
        }
    }
}
