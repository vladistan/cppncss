package cppncss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import cppncss.analyzer.EventHandler;
import org.apache.tools.ant.Project;

public final class AntLogger implements EventHandler
{
    private static final double MS_PER_S = 1000.0;
    private static final int ERROR_LINES_DISPLAYED = 3;
    private long start;
    private final Project project;

    /**
     * Create an event output.
     *
     * @param project the project instance
     */
    public AntLogger( final Project project )
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
        project.log( "Successfully parsed " + parsed + " / " + total + " files in " + time + " s", Project.MSG_VERBOSE );
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
        project.log( buffer.toString(),Project.MSG_VERBOSE );
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
