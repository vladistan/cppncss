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

package cpptools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Captures behaviors common to loggers.
 *
 * @author Mathieu Champlon
 */
public abstract class AbstractLogger implements Logger
{
    private static final double MS_PER_S = 1000.0;
    private static final int ERROR_LINES_DISPLAYED = 3;
    private long start;

    /**
     * {@inheritDoc}
     */
    public final void started()
    {
        start = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    public final void finished( final int parsed, final int total )
    {
        final long end = System.currentTimeMillis();
        final double time = (end - start) / MS_PER_S;
        log( "Successfully parsed " + parsed + " / " + total + " files in " + time + " s" );
    }

    /**
     * Log a given message.
     *
     * @param message the message
     */
    protected abstract void log( String message );

    /**
     * {@inheritDoc}
     */
    public final void display( final String filename, final int line, final int column )
    {
        try
        {
            displayLocation( line, ERROR_LINES_DISPLAYED, filename );
            displayCursor( column );
        }
        catch( final IOException e )
        {
            error( "internal", e, "while trying to display error location" );
        }
    }

    private void displayCursor( final int column )
    {
        final StringBuffer line = new StringBuffer();
        for( int i = 0; i < column - 1; ++i )
            line.append( ' ' );
        log( line.append( '^' ).toString() );
    }

    private void displayLocation( final int start, final int lines, final String filename ) throws IOException
    {
        final BufferedReader reader = new BufferedReader( new FileReader( filename ) );
        try
        {
            for( int i = 0; i < start - lines; i++ )
                reader.readLine();
            for( int i = 0; i < lines; ++i )
                log( reader.readLine() );
        }
        finally
        {
            reader.close();
        }
    }
}
