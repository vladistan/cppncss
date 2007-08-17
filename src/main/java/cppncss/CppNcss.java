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
import cppast.VisitorComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.Counter;
import cppncss.counter.CounterObserver;
import cppncss.counter.FileVisitor;
import cppncss.counter.FunctionCounter;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;
import cppncss.measure.AverageCollector;
import cppncss.measure.Collector;
import cppncss.measure.MeasureCollector;
import cppncss.measure.SumCollector;
import cpptools.Analyzer;
import cpptools.ConsoleLogger;
import cpptools.FileObserverBeautifier;
import cpptools.FileObserverComposite;
import cpptools.Logger;
import cpptools.Options;
import cpptools.Usage;

/**
 * Provides code measures for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppNcss
{
    private final VisitorComposite visitors = new VisitorComposite();
    private final FileObserverComposite observers = new FileObserverComposite();
    private final List<Collector> collectors = new ArrayList<Collector>();
    private final ResultOutput output;
    private final Analyzer analyzer;

    /**
     * Provides a factory to create visitor wrappers from counters.
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
     * @param logger the logger
     * @throws Exception if an error occurs
     */
    public CppNcss( final Options options, final Logger logger ) throws Exception
    {
        output = createOutput( options );
        observers.register( logger );
        analyzer = new Analyzer( options, visitors, new FileObserverBeautifier( options, observers ), logger );
        register( options, new MeasureCollector( options, new ResultOutputAdapter( "Function", output ) ), functionVisitorFactory );
        register( options, new AverageCollector( new ResultOutputAdapter( "Function", output ) ), functionVisitorFactory );
        register( options, new MeasureCollector( options, new ResultOutputAdapter( "File", output ) ), fileVisitorFactory );
        register( options, new AverageCollector( new ResultOutputAdapter( "File", output ) ), fileVisitorFactory );
        register( options, new SumCollector( new ResultOutputAdapter( "Project", output ) ), fileVisitorFactory );
    }

    private void register( final Options options, final Collector collector, final VisitorFactory factory )
    {
        collectors.add( collector );
        observers.register( collector );
        for( final String counter : filter( options ) )
            factory.register( create( collector, counter ) );
    }

    private List<String> filter( final Options options )
    {
        final List<String> counters = new ArrayList<String>();
        for( final String value : extract( options ).split( "," ) )
            if( !counters.contains( value ) )
                counters.add( value );
        return counters;
    }

    private String extract( final Options options )
    {
        final List<String> values = options.getOptionPropertyValues( "m" );
        if( values.isEmpty() )
            return "NCSS,CCN,function";
        return values.get( 0 );
    }

    private Counter create( final CounterObserver observer, final String counter )
    {
        if( counter.equals( "NCSS" ) )
            return new NcssCounter( observer );
        if( counter.equals( "CCN" ) )
            return new CcnCounter( observer );
        if( counter.equals( "function" ) )
            return new FunctionCounter( observer );
        throw new IllegalArgumentException( "invalid measurement '" + counter + "'" );
    }

    /**
     * Run the analysis.
     */
    public void run()
    {
        analyzer.run();
        for( final Collector collector : collectors )
            collector.flush();
        output.flush();
    }

    private ResultOutput createOutput( final Options options ) throws Exception
    {
        final PrintStream stream = createStream( options );
        if( options.hasOption( "x" ) )
            return new XmlResultOutput( stream );
        return new AsciiResultOutput( stream );
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
     * @throws Exception if an error occurs
     */
    public static void main( final String[] args ) throws Exception
    {
        if( !check( args ) )
            return;
        final Options options = new Options( args );
        new CppNcss( options, new ConsoleLogger( options ) ).run();
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
        final Usage usage = new Usage( "cppncss", "http://cppncss.sourceforge.net", "1.0.3" );
        usage.addOption( "h", "print this message" );
        usage.addOption( "d", "print debugging information" );
        usage.addOption( "v", "be extra verbose" );
        usage.addOption( "k", "keep going on parsing errors" );
        usage.addOption( "r", "process directories recursively" );
        usage.addOption( "x", "output result as xml" );
        usage.addOption( "m=<measurements>", "output the <measurements> sorted in given order, default is equivalent to -m=NCSS,CCN,function" );
        usage.addOption( "n=<number>", "output only the top <number> results" );
        usage.addOption( "f=<file>", "output result to <file>" );
        usage.addOption( "D<symbol>[=[<value>]]", "replace define <symbol> with <value>" );
        usage.addOption( "M<symbol>[=[<value>]]", "replace macro <symbol> with <value>" );
        usage.addOption( "p=<path>", "remove <path> prefix when displaying file names" );
        usage.display();
    }
}
