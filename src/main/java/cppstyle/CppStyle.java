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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import cppast.ParserVisitor;
import cppast.VisitorComposite;
import cppstyle.checks.CheckListener;
import cpptools.Analyzer;
import cpptools.ConsoleLogger;
import cpptools.FileObserverBeautifier;
import cpptools.FileObserverComposite;
import cpptools.Logger;
import cpptools.Options;
import cpptools.Usage;

/**
 * Provides style checking for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppStyle
{
    private final VisitorComposite visitors = new VisitorComposite();
    private final FileObserverComposite observers = new FileObserverComposite();
    private final CheckListener output;
    private final Analyzer analyzer;

    /**
     * Create a CppStyle instance.
     *
     * @param options the options
     * @param logger the logger
     * @throws Exception if an error occurs
     */
    public CppStyle( final Options options, final Logger logger ) throws Exception
    {
        if( !options.hasOption( "c" ) )
            throw new IllegalArgumentException( "missing mandatory configuration file" );
        output = createOutput( options );
        observers.register( logger );
        analyzer = new Analyzer( options, visitors, new FileObserverBeautifier( options, observers ), logger );
        populate( options.getOptionPropertyValues( "c" ).get( 0 ) );
    }

    private CheckListener createOutput( final Options options ) throws FileNotFoundException
    {
        final PrintStream stream = createStream( options );
        final AsciiCheckListener listener = new AsciiCheckListener( stream );
        observers.register( listener );
        return listener;
    }

    private PrintStream createStream( final Options options ) throws FileNotFoundException
    {
        if( options.hasOption( "f" ) )
            return new PrintStream( new FileOutputStream( options.getOptionPropertyValues( "f" ).get( 0 ) ) );
        return System.out;
    }

    private void populate( final String filename ) throws Exception
    {
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final NodeList nodes = builder.parse( filename ).getChildNodes();
        if( nodes.getLength() > 0 && nodes.item( 0 ).getNodeName().equals( "cppstyle" ) )
        {
            final Node root = nodes.item( 0 );
            for( int index = 0; index < root.getChildNodes().getLength(); ++index )
            {
                final Node node = root.getChildNodes().item( index );
                if( node.getNodeName().equals( "module" ) )
                {
                    final String name = extract( node, "name" );
                    final Properties properties = transform( node.getChildNodes() );
                    visitors.register( load( name, properties ) );
                }
            }
        }
    }

    private String extract( final Node node, final String s1 )
    {
        return node.getAttributes().getNamedItem( s1 ).getNodeValue();
    }

    private Properties transform( final NodeList nodes )
    {
        final Properties properties = new Properties();
        for( int index = 0; index < nodes.getLength(); ++index )
        {
            final Node node = nodes.item( index );
            if( node.getNodeName().equals( "property" ) )
            {
                final String name = extract( node, "name" );
                final String value = extract( node, "value" );
                properties.setProperty( name, value );
            }
        }
        return properties;
    }

    private ParserVisitor load( final String module, final Properties properties ) throws Exception
    {
        return (ParserVisitor)Class.forName( "cppstyle.checks." + module + "Check" ).getConstructor( new Class[]
        {
                CheckListener.class, Properties.class
        } ).newInstance( new Object[]
        {
                output, properties
        } );
    }

    /**
     * Run the analysis.
     */
    public void run()
    {
        analyzer.run();
    }

    /**
     * Run the application.
     *
     * @param args the arguments
     * @throws Exception if an error occurs
     */
    public static void main( final String[] args ) throws Exception
    {
        if( !check( args ) )
            return;
        final Options options = new Options( args );
        new CppStyle( options, new ConsoleLogger( options ) ).run();
    }

    private static boolean check( final String[] args )
    {
        if( args.length > 0 && !args[0].equals( "-h" ) )
            return true;
        usage();
        return false;
    }

    private static void usage()
    {
        final Usage usage = new Usage( "cppstyle", "http://cppncss.sourceforge.net", "1.0.0" );
        usage.addOption( "h", "print this message" );
        usage.addOption( "d", "print debugging information" );
        usage.addOption( "v", "be extra verbose" );
        usage.addOption( "k", "keep going on parsing errors" );
        usage.addOption( "r", "process directories recursively" );
        usage.addOption( "x", "output result as xml" );
        usage.addOption( "c=<file>", "use the given configuration file" );
        usage.addOption( "f=<file>", "output result to the given file" );
        usage.addOption( "D<symbol>[=[<value>]]", "replace define <symbol> with <value>" );
        usage.addOption( "M<symbol>[=[<value>]]", "replace macro <symbol> with <value>" );
        usage.addOption( "p=<path>", "remove <path> prefix when displaying file names" );
        usage.display();
    }
}
