package starmap;

import java.util.GregorianCalendar;
import org.jdom2.Element;

/**
 * Data class describing a single star, with methods to draw on a panel.
 * Contains methods by Dr. John Weiss provided for CSC421 GUI/OOP class.
 * @author John Brink
 */
public class Star {
    // Identity
    public int hrNumber;
    public String name;
    public String commonName;
    public String constellation;
    public String starClass;
    
    // Given data
    public double rightAscension;
    public double declination;
    public double magnitude;
    
    // Computed data
    public double altitude;
    public double azimuth;
    
    private double x;
    private double y;
    private boolean visible;

    public Star(int hrNumber, String name, String constellation, double radians,
            double declination, double magnitude, String starClass, String commonName)
    {
        this.hrNumber = hrNumber;
        this.name = name;
        this.commonName = commonName;
        this.constellation = constellation;
        this.starClass = starClass;
        
        this.rightAscension = radians;
        this.declination = declination;
        this.magnitude = magnitude;
        
        computePosition(0, 0, 0, 0);
    }
    
    /**
     * Computes number of days elapsed since June 10, 2005 6:45:14 GMT
     * @author John M. Weiss, Ph.D.
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
    
    public final void computePosition(double lat, double lon, double alt, double azi)
    {
        computeAltAzi(lat, lon);
        computeXY(alt, azi);
    }

    /**
     * Given observer position (lat,lon), convert star position in (ra,dec) to (azi, alt)
     * @author John M. Weiss, Ph.D.
     * @param lat The current viewing latitude
     * @param lon The current viewing longitude
     */
    private void computeAltAzi( double lat, double lon )
    {
        double t = elapsed_days();
        double tG = Math.IEEEremainder( 360.0 * 1.0027379093 * t, 360.0 );
        double psi = tG + Math.toDegrees( lon ) + 90;

        // rename ala formulas in Don's paper
        double alpha = rightAscension;
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

        // finally compute alt/azi values
        altitude = Math.atan( Z / Math.sqrt( X * X + Y * Y ) );
        azimuth = Math.acos( Y / Math.sqrt( X * X + Y * Y ) );
        if ( X < 0.0 ) azimuth = 2.0 * Math.PI - azimuth;
    }
    
    /**
     * Computes the X/Y position of a star, given viewer's position
     * @author John M. Weiss, Ph.D.
     * @param viewerAlt Altitude of the viewer
     * @param viewerAzi Azimuth of the viewer
     */
    private void computeXY(double viewerAlt, double viewerAzi)
    {
        double R = 1.0;	// distance to star: assume all stars are located on sphere of radius 1
        x = R * Math.cos( altitude ) * Math.sin( azimuth - viewerAzi );
        y = R * ( Math.cos( viewerAlt ) * Math.sin( altitude )
                - Math.sin( viewerAlt ) * Math.cos( altitude ) * Math.cos( azimuth - viewerAzi ) );
        double clip = Math.sin( viewerAlt ) * Math.sin ( altitude )
                + Math.cos( viewerAlt ) * Math.cos( altitude ) * Math.cos( azimuth - viewerAzi );
        
        // How to tell if star is visible?!
        //visible = true;
        visible = (clip >= 0);
        //visible = Math.abs(azimuth - viewerAzi) < 90;
    }
    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public static Star deserialize(Element elem)
    {
        int hrNumber = Integer.parseInt(elem.getChild("HRnumber").getTextTrim());
        String name = FileLoader.getAttribute(elem, "name", "");
        String constellation = FileLoader.getAttribute(elem, "constellation", "");
        double radians = parseRa(elem.getChild("ra").getTextTrim());
        double declination = parseDec(elem.getChild("dec").getTextTrim());
        double magnitude = Double.parseDouble(elem.getChild("vmag").getTextTrim());
        String className = FileLoader.getAttribute(elem, "class", "");
        String commonName = FileLoader.getAttribute(elem, "common_name", "");
        
        return new Star(hrNumber, name, constellation, radians, declination, magnitude, className, commonName);
    }
    
    /**
     * Given a hours-minutes-seconds printout as a string, returns the value
     * of right ascension
     * @author John Brink, John M. Weiss, Ph.D.
     * @param s
     * @return 
     */
    private static double parseRa(String s)
    {
        //Sample: "6 45 8.90"
        String[] parts = s.split(" ");
        
        double hr = Double.parseDouble(parts[0]);
        double min = Double.parseDouble(parts[1]);
        double sec = Double.parseDouble(parts[2]);
        
        return Math.toRadians( ( hr + min / 60 + sec / 3600 ) * 15 );
    }
    
    /**
     * Given a degrees-minutes-seconds printout as a string, returns the value
     * of declination
     * @author John Brink, John M. Weiss, Ph.D.
     * @param s
     * @return 
     */
    private static double parseDec(String s)
    {
        //Sample: "6 45 8.90"
        String[] parts = s.split(" ");
        
        double deg = Double.parseDouble(parts[0]);
        double min = Double.parseDouble(parts[1]);
        double sec = Double.parseDouble(parts[2]);
        
        return Math.toRadians( Math.abs( deg ) + min / 60 + sec / 3600 );
    }
    
    @Override
    public String toString()
    {
        if(commonName == null)
        {
            return name;
        }
        else
        {
            return String.format("%s (%s)", name, commonName);
        }
    }
}
