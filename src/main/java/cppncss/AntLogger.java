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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import cppncss.analyzer.EventHandler;

/**
 * Implements an event handler for logging to Ant.
 *
 * @author Mathieu Champlon
 */
public final class AntLogger implements EventHandler
{
    private static final double MS_PER_S = 1000.0;
    private static final int ERROR_LINES_DISPLAYED = 3;
    private long start;
    private final ProjectComponent project;

    /**
     * Create an event output.
     *
     * @param project the project instance
     */
    public AntLogger( final ProjectComponent project )
    {
        this.project = project;
    }

    /**
     * {@inheritDoc}
     */
    public void started()
    {
        start = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    public void finished( final int parsed, final int total )
    {
        final long end = System.currentTimeMillis();
        final double time = (end - start) / MS_PER_S;
        project.log( "Successfully parsed " + parsed + " / " + total + " files in " + time + " s", Project.MSG_INFO );
    }

    /**
     * {@inheritDoc}
     */
    public void error( final String filename, final Throwable throwable, final String reason )
    {
        project.log( throwable.getMessage(), Project.MSG_DEBUG );
        project.log( "Skipping " + filename + " : " + reason, Project.MSG_VERBOSE );
    }

    /**
     * {@inheritDoc}
     */
    public void display( final String filename, final int line, final int column )
    {
        try
        {
            displayLocation( line, ERROR_LINES_DISPLAYED, filename );
            displayCursor( column );
        }
        catch( IOException e )
        {
            error( "internal", e, "while trying to display error location" );
        }
    }

    private void displayCursor( final int column )
    {
        final StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < column - 1; ++i )
            buffer.append( ' ' );
        buffer.append( '^' );
        project.log( buffer.toString(), Project.MSG_VERBOSE );
    }

    private void displayLocation( final int start, final int lines, final String filename ) throws IOException
    {
        final BufferedReader reader = new BufferedReader( new FileReader( filename ) );
        for( int i = 0; i < start - lines; i++ )
            reader.readLine();
        for( int i = 0; i < lines; ++i )
            project.log( reader.readLine(), Project.MSG_VERBOSE );
        reader.close();
    }

    /**
     * {@inheritDoc}
     */
    public void changed( final String filename )
    {
        project.log( "Parsing " + filename, Project.MSG_DEBUG );
    }
}
