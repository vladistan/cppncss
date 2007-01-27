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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.AntlibDefinition;
import org.apache.tools.ant.types.FileSet;
import tools.Options;

/**
 * Provides an Apache Ant task implementation for CppNcss.
 *
 * @author Mathieu Champlon
 */
public final class CppNcssTask extends AntlibDefinition
{
    private final List<FileSet> filesets = new ArrayList<FileSet>();
    private final List<Symbol> symbols = new ArrayList<Symbol>();
    private boolean keepGoing = false;
    private String filename;
    private String prefix;

    /**
     * Add a set of source files.
     *
     * @param fileset a set of source files.
     */
    public void addFileset( final FileSet fileset )
    {
        filesets.add( fileset );
    }

    /**
     * Defines a prefix.
     * <p>
     * Not required.
     *
     * @param prefix the prefix
     */
    public void setPrefix( final String prefix )
    {
        this.prefix = format( prefix );
    }

    /**
     * Sets the name of the output file.
     * <p>
     * Required.
     *
     * @param filename the file name
     */
    public void setToFile( final String filename )
    {
        this.filename = filename;
    }

    /**
     * Set whether the analyzis should stop upon error or not.
     * <p>
     * Not required. Default is false.
     *
     * @param keepGoing if the analyzis should keep going upon error
     */
    public void setKeepGoing( final boolean keepGoing )
    {
        this.keepGoing = keepGoing;
    }

    private String format( final String path )
    {
        String result = path.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );
        if( result.charAt( result.length() - 1 ) != File.separatorChar )
            result += File.separatorChar;
        return result;
    }

    /**
     * Add a define definition.
     *
     * @param define the define
     */
    public void addConfiguredDefine( final Define define )
    {
        if( define.getName() == null )
            throw new BuildException( "Missing required 'name' for define" );
        symbols.add( define );
    }

    /**
     * Add a macro definition.
     *
     * @param macro the macro
     */
    public void addConfiguredMacro( final Macro macro )
    {
        if( macro.getName() == null )
            throw new BuildException( "Missing required 'name' for macro" );
        symbols.add( macro );
    }

    /**
     * {@inheritDoc}
     */
    public void execute()
    {
        if( filename == null )
            throw new BuildException( "Missing 'tofile' attribute to specify output file name" );
        try
        {
            new CppNcss( new Options( buildArgs() ), new AntLogger( this ) ).run();
        }
        catch( FileNotFoundException e )
        {
            throw new BuildException( e );
        }
    }

    private String[] buildArgs()
    {
        final List<String> args = new ArrayList<String>();
        args.add( "-x" );
        args.add( "-f=" + filename );
        if( keepGoing )
            args.add( "-k" );
        if( prefix != null )
            args.add( "-p=" + prefix );
        for( Symbol symbol : symbols )
            args.add( symbol.toArgument() );
        for( FileSet fileset : filesets )
        {
            final DirectoryScanner scanner = fileset.getDirectoryScanner( getProject() );
            final String directory = format( scanner.getBasedir().toString() );
            for( String file : scanner.getIncludedFiles() )
                args.add( directory + file );
        }
        return args.toArray( new String[args.size()] );
    }

    /**
     * Provides a symbol definition.
     *
     * @author Mathieu Champlon
     */
    public static class Symbol
    {
        private String name;
        private String value;
        private final String prefix;

        /**
         * Create a symbol.
         *
         * @param prefix the command line option prefix
         */
        protected Symbol( final String prefix )
        {
            this.prefix = prefix;
        }

        /**
         * Sets the name.
         * <p>
         * Required.
         *
         * @param name the name
         */
        public final void setName( final String name )
        {
            this.name = name;
        }

        /**
         * Retrieve the name.
         *
         * @return the name
         */
        public final String getName()
        {
            return name;
        }

        /**
         * Sets the macro value.
         * <p>
         * Required.
         *
         * @param value the value
         */
        public final void setValue( final String value )
        {
            this.value = value;
        }

        /**
         * Retrieve the value.
         *
         * @return the value
         */
        public final String getValue()
        {
            return value;
        }

        /**
         * Create the corresponding command line argument.
         */
        public final String toArgument()
        {
            if( value == null )
                return "-" + prefix + name;
            return "-" + prefix + name + '=' + value;
        }
    }

    /**
     * Provides a define definition.
     *
     * @author Mathieu Champlon
     */
    public static final class Define extends Symbol
    {
        /**
         * Create a define symbol.
         */
        public Define()
        {
            super( "D" );
        }
    }

    /**
     * Provides a macro definition.
     *
     * @author Mathieu Champlon
     */
    public static final class Macro extends Symbol
    {
        /**
         * Create a macro symbol.
         */
        public Macro()
        {
            super( "M" );
        }
    }
}
