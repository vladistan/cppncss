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
import java.util.ArrayList;
import java.util.List;
import tools.Options;
import tools.Usage;
import cppast.VisitorComposite;
import cppncss.analyzer.Analyzer;
import cppncss.analyzer.EventHandler;
import cppncss.analyzer.FileObserverComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.Counter;
import cppncss.counter.FileVisitor;
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
    private final FileObserverComposite observers = new FileObserverComposite();
    private final VisitorComposite visitors = new VisitorComposite();
    private final List<Collector> collectors = new ArrayList<Collector>();
    private final ResultOutput output;
    private final Analyzer analyzer;

    /**
     * Implements a factory to create visitors from a given counter.
     *
     * @author Mathieu Champlon
     */
    private interface VisitorFactory
    {
        void register( Counter counter );
    }

    private final VisitorFactory functionVisitorFactory = new VisitorFactory()
    {
        public void register( final Counter counter )
        {
            visitors.register( new FunctionVisitor( counter ) );
        }
    };
    private final VisitorFactory fileVisitorFactory = new VisitorFactory()
    {
        public void register( final Counter counter )
        {
            final FileVisitor visitor = new FileVisitor( counter );
            observers.register( visitor );
            visitors.register( visitor );
        }
    };

    /**
     * Create a CppNcss instance.
     *
     * @param options the options
     * @param handler the log handler
     * @throws FileNotFoundException when the log file fails
     */
    public CppNcss( final Options options, final EventHandler handler ) throws FileNotFoundException
    {
        output = createOutput( options );
        analyzer = new Analyzer( options, visitors, observers, handler );
        register( new MeasureCollector( new ResultOutputAdapter( "Function", output ) ), functionVisitorFactory );
        register( new AverageCollector( new ResultOutputAdapter( "Function", output ) ), functionVisitorFactory );
        register( new MeasureCollector( new ResultOutputAdapter( "File", output ) ), fileVisitorFactory );
        register( new AverageCollector( new ResultOutputAdapter( "File", output ) ), fileVisitorFactory );
        register( new SumCollector( new ResultOutputAdapter( "Project", output ) ), fileVisitorFactory );
    }

    private void register( final Collector collector, final VisitorFactory factory )
    {
        collectors.add( collector );
        observers.register( collector );
        factory.register( new NcssCounter( collector ) );
        factory.register( new CcnCounter( collector ) );
        factory.register( new FunctionCounter( collector ) );
    }

    /**
     * Run the analyzis.
     */
    public void run()
    {
        analyzer.run();
        for( Collector collector : collectors )
            collector.flush();
        output.flush();
    }

    private ResultOutput createOutput( final Options options ) throws FileNotFoundException
    {
        final PrintStream stream = createStream( options );
        if( !options.hasOption( "x" ) )
            return new AsciiResultOutput( stream );
        return new XmlResultOutput( stream );
    }

    private PrintStream createStream( final Options options ) throws FileNotFoundException
    {
        if( options.hasOption( "f" ) )
            return new PrintStream( new FileOutputStream( options.getOptionPropertyValues( "f" ).get( 0 ) ) );
        return System.out;
    }

    /**
     * Run the application.
     *
     * @param args the arguments
     * @throws FileNotFoundException when the log file fails
     */
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
