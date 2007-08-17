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

package cppstyle.checks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import cpptools.FileReader;

/**
 * Checks for the validity of file headers.
 *
 * @author Mathieu Champlon
 */
public final class HeaderCheck implements FileContentObserver
{
    private final CheckListener listener;
    private final String[] expected;
    private final List<Integer> ignoredLines;

    /**
     * Create a file header check.
     *
     * @param listener the check listener
     * @param properties the available properties
     * @throws IOException upon error
     */
    public HeaderCheck( final CheckListener listener, final Properties properties ) throws IOException
    {
        if( listener == null )
            throw new IllegalArgumentException( "argument 'listener' is null" );
        this.listener = listener;
        this.expected = split( getExpected( properties ) );
        this.ignoredLines = getIgnoredLines( properties );
    }

    private String[] split( final String string )
    {
        if( string == null )
            return new String[0];
        return string.split( "\r\n|\n|\r" );
    }

    private String getExpected( final Properties properties ) throws IOException
    {
        final String content = properties.getProperty( "header" );
        if( content != null )
            return content;
        final String filename = properties.getProperty( "headerFile" );
        if( filename != null )
            return FileReader.read( filename );
        throw new IllegalArgumentException( "missing property 'headerFile' or 'header'" );
    }

    private List<Integer> getIgnoredLines( final Properties properties )
    {
        final List<Integer> lines = new ArrayList<Integer>();
        final String values = properties.getProperty( "ignoreLines" );
        if( values != null )
            for( final String value : values.split( "," ) )
                lines.add( Integer.parseInt( value.trim() ) );
        return lines;
    }

    private static final class Interval
    {
        private final int start;
        private int end;

        public Interval( final int start )
        {
            this.start = start;
            this.end = start;
        }

        public boolean merge( final int value )
        {
            if( value > end + 1 )
                return false;
            end = Math.max( end, value );
            return true;
        }

        public void notify( final CheckListener listener )
        {
            listener.fail( "file header mismatch", start, end );
        }
    }

    /**
     * {@inheritDoc}
     */
    public void notify( final String content )
    {
        notify( compare( split( content ) ) );
    }

    private void notify( final List<Interval> intervals )
    {
        for( final Interval interval : intervals )
            interval.notify( listener );
    }

    private List<Interval> compare( final String[] actual )
    {
        final List<Interval> intervals = new ArrayList<Interval>();
        for( int line = 0; line < expected.length; ++line )
            if( !matches( actual, line ) && !merge( intervals, line + 1 ) )
                intervals.add( new Interval( line + 1 ) );
        return intervals;
    }

    private boolean matches( final String[] actual, final int line )
    {
        return line < actual.length && (ignoredLines.contains( line + 1 ) || actual[line].equals( expected[line] ));
    }

    private boolean merge( final List<Interval> intervals, final int line )
    {
        return !intervals.isEmpty() && intervals.get( intervals.size() - 1 ).merge( line );
    }
}
