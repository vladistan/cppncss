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
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.AntlibDefinition;
import org.apache.tools.ant.types.FileSet;

/**
 * @author Mathieu Champlon
 */
public final class CppNcssTask extends AntlibDefinition
{
    private final List<FileSet> filesets = new ArrayList<FileSet>();
    private String prefix;
    private String filename;

    /**
     * Add a set of source files.
     *
     * @param fileset a set of source files.
     */
    public void addFileset( final FileSet fileset )
    {
        filesets.add( fileset );
    }

    public void setPrefix( final String prefix )
    {
        this.prefix = format( prefix );
    }

    public void setToFile( final String filename )
    {
        this.filename = filename;
    }

    private String format( final String path )
    {
        String result = path.replace( '/', File.separatorChar ).replace( '\\', File.separatorChar );
        if( result.charAt( result.length() - 1 ) != File.separatorChar )
            result += File.separatorChar;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void execute()
    {
        if( filename == null )
            throw new BuildException( "Missing 'tofile' attribute to specify output file name" );
        CppNcss.run( buildArgs(), AntLogger.class );
    }

    private String[] buildArgs()
    {
        final List<String> args = new ArrayList<String>();
        args.add( "-x" );
        args.add( "-f=" + filename );
        if( prefix != null )
            args.add( "-p=" + prefix );
        for( int j = 0; j < filesets.size(); ++j )
        {
            final FileSet set = filesets.get( j );
            final DirectoryScanner scanner = set.getDirectoryScanner( getProject() );
            final String directory = format( scanner.getBasedir().toString() );
            final String[] files = scanner.getIncludedFiles();
            for( int i = 0; i < files.length; ++i )
                args.add( directory + files[i] );
        }
        return args.toArray( new String[args.size()] );
    }
}
