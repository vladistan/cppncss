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

import java.util.Vector;
import tools.Options;
import cppast.VisitorComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.Counter;
import cppncss.counter.CounterObserver;
import cppncss.counter.FileVisitor;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;

/**
 * Provides code measure for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppNcss
{
    private static final int THRESHOLD = 30;
    private static final CounterFactory[] COUNTER_FACTORIES =
    {
            new CounterFactory()
            {
                public Counter createCounter( final CounterObserver observer )
                {
                    return new NcssCounter( observer );
                }

                public String getLabel()
                {
                    return "NCSS";
                }
            }, new CounterFactory()
            {
                public Counter createCounter( final CounterObserver observer )
                {
                    return new CcnCounter( observer );
                }

                public String getLabel()
                {
                    return "CCN";
                }
            }
    };
    private static final String INDEX = COUNTER_FACTORIES[0].getLabel();
    private static final CollectorFactory[] COLLECTOR_FACTORIES =
    {
            new CollectorFactory()
            {
                public Collector createCollector( final FileObserverComposite observer, final VisitorComposite visitor )
                {
                    final Collector collector = new Collector( INDEX, THRESHOLD );
                    observer.register( collector );
                    for( CounterFactory factory : COUNTER_FACTORIES )
                        visitor.register( new FunctionVisitor( factory.createCounter( collector ) ) );
                    return collector;
                }

                public String getLabel()
                {
                    return "Function";
                }
            }, new CollectorFactory()
            {
                public Collector createCollector( final FileObserverComposite observer, final VisitorComposite visitor )
                {
                    final Collector collector = new Collector( INDEX, THRESHOLD );
                    for( CounterFactory factory : COUNTER_FACTORIES )
                    {
                        final FileVisitor fv = new FileVisitor( factory.createCounter( collector ) );
                        observer.register( fv );
                        visitor.register( fv );
                    }
                    return collector;
                }

                public String getLabel()
                {
                    return "File";
                }
            }
    };

    private CppNcss()
    {
    }

    public static void main( final String[] args )
    {
        final FileObserverComposite observer = new FileObserverComposite();
        final VisitorComposite visitor = new VisitorComposite();
        final Vector<Collector> collectors = new Vector<Collector>();
        for( CollectorFactory factory : COLLECTOR_FACTORIES )
            collectors.add( factory.createCollector( observer, visitor ) );
        final Analyzer analyzer = createAnalyzer( args, observer );
        analyzer.accept( visitor );
        for( int index = 0; index < collectors.size(); ++index )
            collectors.elementAt( index ).accept( createLogger( COLLECTOR_FACTORIES[index].getLabel() ) );
    }

    private static ConsoleLogger createLogger( final String item )
    {
        final ConsoleLogger logger = new ConsoleLogger( item );
        for( CounterFactory factory : COUNTER_FACTORIES )
            logger.register( factory.getLabel() );
        return logger;
    }

    private static Analyzer createAnalyzer( final String[] args, final FileObserver observer )
    {
        final Options options = new Options( args );
        final boolean debug = options.hasOption( "d" );
        final boolean verbose = debug || options.hasOption( "v" );
        return new Analyzer( options, observer, new ConsoleEventHandler( debug, verbose ) );
    }
}
