package starmap;

import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 *
 * @author 7058191
 */
public class Star {
    // Identity
    private int hrNumber;
    private String name;
    private String commonName;
    
    // Given data
    private double radians;
    private double declination;
    private double magnitude;
    
    // Computed data
    private double altitude;
    private double azimuth;

    public Star(int hrNumber, String name, String commonName, double radians, double declination, double magnitude)
    {
        this.hrNumber = hrNumber;
        this.name = name;
        this.commonName = commonName;
        this.radians = radians;
        this.declination = declination;
        this.magnitude = magnitude;
        
        computePosition(0, 0);
    }
    
    /**
     * Computes number of days elapsed since June 10, 2005 6:45:14 GMT
     * Author: John M. Weiss, Ph.D.
     * @return 
     */
    private static double elapsed_days( )
    {
        // e.g., suppose current time is Oct 29, 2012 11:00:00 MST
        GregorianCalendar now_cal = new GregorianCalendar();
        GregorianCalendar then_cal = new GregorianCalendar();
        now_cal.set( 2012, 10, 29, 11, 0, 0 );
        then_cal.set( 2005, 5, 10, 6, 45, 14 );

        // need current time in GMT (MST + 6 hours, or MST 7 hours if not daylight savings time)
        long now_msec = now_cal.getTimeInMillis() + 6 * 3600 * 1000;
        long then_msec = then_cal.getTimeInMillis();
        double diff_days = ( now_msec - then_msec ) / 1000.0 / ( 24.0 * 3600.0 );
        return diff_days;
    }

    /**
     * Given observer position (lat,lon), convert star position in (ra,dec) to (azi, alt)
     * Author: John M. Weiss, Ph.D.
     * @return 
     */
    public void computePosition( double lat, double lon )
    {
        double t = elapsed_days();
        double tG = Math.IEEEremainder( 360.0 * 1.0027379093 * t, 360.0 );
        double psi = tG + Math.toDegrees( lon ) + 90;

        // rename ala formulas in Don's paper
        double alpha = radians;
        double beta  = lat;
        double delta = declination;
        psi = Math.toRadians( psi );

        double X =  Math.cos( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  + Math.sin( psi ) * Math.cos( delta ) * Math.sin( alpha );
        double Y = -Math.sin( beta ) * Math.sin( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  + Math.sin( beta ) * Math.cos( psi ) * Math.cos( delta ) * Math.sin( alpha )
                  + Math.cos( beta ) * Math.sin( delta );
        double Z =  Math.cos( beta ) * Math.sin( psi ) * Math.cos( delta ) * Math.cos( alpha )
                  - Math.cos( beta ) * Math.cos( psi ) * Math.cos( delta ) * Math.sin( alpha )
                  + Math.sin( beta ) * Math.sin( delta );
        // System.out.printf( "(X,Y,Z) = (%.3f,%.3f,%3f)\n\n", X, Y, Z );

        // finally compute alt/azi values
        altitude = Math.atan( Z / Math.sqrt( X * X + Y * Y ) );
        azimuth = Math.acos( Y / Math.sqrt( X * X + Y * Y ) );
        if ( X < 0.0 ) azimuth = 2.0 * Math.PI - azimuth;
    }
}
