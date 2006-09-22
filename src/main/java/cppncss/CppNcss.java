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

import tools.Options;
import cppast.VisitorComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.FileVisitor;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;

/**
 * Provides code measurement for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppNcss
{
    private static final String INDEX = "NCSS";
    private static final int THRESHOLD = 30;

    private CppNcss()
    {
    }

    public static void main( final String[] args )
    {
        final Collector collector = new Collector( INDEX, THRESHOLD );
        final Collector collector2 = new Collector( INDEX, THRESHOLD );
        final FileObserverComposite observer = new FileObserverComposite();
        observer.register( collector );
//        observer.register( collector2 );
        final Analyzer analyzer = createAnalyzer( args, observer );
        final VisitorComposite visitor = new VisitorComposite();
        visitor.register( new FunctionVisitor( new NcssCounter( collector ) ) ); // FIXME first counter must be INDEX
        visitor.register( new FunctionVisitor( new CcnCounter( collector ) ) );
        final FileVisitor fv1 = new FileVisitor( new NcssCounter( collector2 ) );
        final FileVisitor fv2 = new FileVisitor( new CcnCounter( collector2 ) );
        observer.register( fv1 );
        observer.register( fv2 );
        visitor.register( fv1 );
        visitor.register( fv2 );
        analyzer.accept( visitor );
        final ConsoleLogger logger = new ConsoleLogger( "Function" );
        logger.register( "NCSS" ); // FIXME registration order must be the same as for counters
        logger.register( "CCN" );
        collector.accept( logger );
        final ConsoleLogger logger2 = new ConsoleLogger( "File" );
        logger2.register( "NCSS" ); // FIXME registration order must be the same as for counters
        logger2.register( "CCN" );
        collector2.accept( logger2 );
    }

    private static Analyzer createAnalyzer( final String[] args, final FileObserver observer )
    {
        final Options options = new Options( args );
        final boolean debug = options.hasOption( "d" );
        final boolean verbose = debug || options.hasOption( "v" );
        return new Analyzer( options, observer, new ConsoleEventHandler( debug, verbose ) );
    }
}
