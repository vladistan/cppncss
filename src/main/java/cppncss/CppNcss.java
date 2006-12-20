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

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.DefaultPicoContainer;
import tools.Options;
import tools.Usage;
import cppast.ParserVisitor;
import cppast.VisitorComposite;
import cppncss.analyzer.Analyzer;
import cppncss.analyzer.FileObserver;
import cppncss.analyzer.FileObserverComposite;
import cppncss.counter.CcnCounter;
import cppncss.counter.FileVisitor;
import cppncss.counter.FunctionCounter;
import cppncss.counter.FunctionVisitor;
import cppncss.counter.NcssCounter;
import cppncss.measure.AverageCollector;
import cppncss.measure.MeasureCollector;
import cppncss.measure.SumCollector;

/**
 * Provides code measures for C++.
 *
 * @author Mathieu Champlon
 */
public final class CppNcss
{
    private CppNcss()
    {
    }

    public static void main( final String[] args )
    {
        if( !check( args ) )
            return;
        final MutablePicoContainer parent = new DefaultPicoContainer();
        parent.registerComponentImplementation( Options.class, Options.class, new Parameter[]
        {
            new ConstantParameter( args )
        } );
        final Options options = (Options)parent.getComponentInstance( Options.class );
        if( !options.hasOption( "f" ) )
            parent.registerComponentInstance( System.out.toString(), System.out );
        else
        {
            parent.registerComponentImplementation( PrintStream.class, PrintStream.class );
            parent.registerComponentImplementation( OutputStream.class, FileOutputStream.class, new Parameter[]
            {
                new ConstantParameter( options.getOptionPropertyValues( "f" ).get( 0 ) )
            } );
        }
        if( options.hasOption( "x" ) )
            parent.registerComponentImplementation( ResultOutput.class, XmlResultOutput.class );
        else
            parent.registerComponentImplementation( ResultOutput.class, AsciiResultOutput.class );
        registerCollector( parent, "Function", FunctionVisitor.class, MeasureCollector.class );
        registerCollector( parent, "Function", FunctionVisitor.class, AverageCollector.class );
        registerCollector( parent, "File", FileVisitor.class, MeasureCollector.class );
        registerCollector( parent, "File", FileVisitor.class, AverageCollector.class );
        registerCollector( parent, "Project", FileVisitor.class, SumCollector.class );
        final MutablePicoContainer main = new DefaultPicoContainer( parent );
        main.registerComponentImplementation( VisitorComposite.class, VisitorComposite.class, new Parameter[]
        {
            new ComponentParameter( ParserVisitor.class, false )
        } );
        main.registerComponentImplementation( FileObserverComposite.class, FileObserverComposite.class, new Parameter[]
        {
            new ComponentParameter( FileObserver.class, false )
        } );
        main.registerComponentImplementation( EventOutput.class );
        main.registerComponentImplementation( Analyzer.class );
        main.addChildContainer( parent );
        main.start();
        main.stop();
    }

    private static void registerCollector( final MutablePicoContainer parent, final String name,
            final Class visitorType, final Class collectorType )
    {
        final MutablePicoContainer local = new DefaultPicoContainer( parent );
        local.registerComponentImplementation( collectorType );
        local.registerComponentImplementation( ResultOutputAdapter.class, ResultOutputAdapter.class, new Parameter[]
        {
                new ConstantParameter( name ), new ComponentParameter( ResultOutput.class )
        } );
        registerVisitor( parent, local, visitorType, NcssCounter.class );
        registerVisitor( parent, local, visitorType, CcnCounter.class );
        registerVisitor( parent, local, visitorType, FunctionCounter.class );
        final Object collector = local.getComponentInstance( collectorType );
        parent.registerComponentInstance( collector.toString(), collector );
    }

    private static void registerVisitor( final MutablePicoContainer parent, final MutablePicoContainer local,
            final Class visitorType, final Class counterType )
    {
        final MutablePicoContainer inner = new DefaultPicoContainer( local );
        inner.registerComponentImplementation( counterType );
        inner.registerComponentImplementation( visitorType );
        final Object visitor = inner.getComponentInstance( visitorType );
        parent.registerComponentInstance( visitor.toString(), visitor );
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
        final Usage usage = new Usage( "cppncss", "http://cppncss.sourceforge.net", "1.0.1" );
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
