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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import tools.Container;
import tools.Options;
import tools.Usage;
import cppast.VisitorComposite;
import cppncss.analyzer.Analyzer;
import cppncss.analyzer.EventHandler;
import cppncss.analyzer.FileObserverComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.FunctionCounter;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;
import cppncss.measure.AverageCollector;
import cppncss.measure.Collector;
import cppncss.measure.MeasureCollector;
import cppncss.measure.SumCollector;

/**
 * Provides code measures for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppNcss
{
    private final Container container = new Container();
    private final Analyzer analyzer;

    /**
     * Create a CppNcss instance.
     *
     * @param options the options
     * @param handler the log handler
     * @throws FileNotFoundException when the log file fails
     */
    public CppNcss( final Options options, final EventHandler handler ) throws FileNotFoundException
    {
        final ResultOutput output = createOutput( options );
        final FileObserverComposite observers = new FileObserverComposite();
        final VisitorComposite visitors = new VisitorComposite();
        register( new MeasureCollector( new ResultOutputAdapter( "Function", output ) ), visitors, observers );
        register( new AverageCollector( new ResultOutputAdapter( "Function", output ) ), visitors, observers );
        register( new MeasureCollector( new ResultOutputAdapter( "File", output ) ), visitors, observers );
        register( new AverageCollector( new ResultOutputAdapter( "File", output ) ), visitors, observers );
        register( new SumCollector( new ResultOutputAdapter( "Project", output ) ), visitors, observers );
        analyzer = new Analyzer( options, visitors, observers, handler );
    }

    /**
     * Run the analyzis.
     */
    public void run()
    {
        analyzer.run();
        container.start();
        container.stop();
    }

    private void register( final Collector collector, final VisitorComposite visitors,
            final FileObserverComposite observers )
    {
        container.register( collector );
        observers.register( collector );
        visitors.register( new FunctionVisitor( new NcssCounter( collector ) ) );
        visitors.register( new FunctionVisitor( new CcnCounter( collector ) ) );
        visitors.register( new FunctionVisitor( new FunctionCounter( collector ) ) );
    }

    private ResultOutput createOutput( final Options options ) throws FileNotFoundException
    {
        final PrintStream stream = createStream( options );
        if( !options.hasOption( "x" ) )
            return new AsciiResultOutput( stream );
        return container.register( new XmlResultOutput( stream ) );
    }

    private PrintStream createStream( final Options options ) throws FileNotFoundException
    {
        if( options.hasOption( "f" ) )
            return new PrintStream( new FileOutputStream( options.getOptionPropertyValues( "f" ).get( 0 ) ) );
        return System.out;
    }

    public static void main( final String[] args ) throws FileNotFoundException
    {
        if( !check( args ) )
            return;
        final Options options = new Options( args );
        new CppNcss( options, new EventOutput( options ) ).run();
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
        final Usage usage = new Usage( "cppncss", "http://cppncss.sourceforge.net", "1.0.2" );
        usage.addOption( "h", "print this message" );
        usage.addOption( "d", "print debugging information" );
        usage.addOption( "v", "be extra verbose" );
        usage.addOption( "k", "keep going on parsing errors" );
        usage.addOption( "r", "process directories recursively" );
        usage.addOption( "x", "output result as xml" );
        usage.addOption( "f=<file>", "output result to the given file" );
        usage.addOption( "D<symbol>[=[<value>]]", "replace define <symbol> with <value>" );
        usage.addOption( "M<symbol>[=[<value>]]", "replace macro <symbol> with <value>" );
        usage.addOption( "p=<path>", "remove <path> prefix when displaying file names" );
        usage.display();
    }
}
