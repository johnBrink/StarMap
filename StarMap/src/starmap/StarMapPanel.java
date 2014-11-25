/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JPanel;
import starmap.Constellation.Line;

/**
 * A JPanel that displays a set of stars and constellations
 * @author John Brink
 */
public class StarMapPanel extends JPanel implements MouseMotionListener, MouseWheelListener
{
    public enum SearchResult
    {
        OK,
        NOTVISIBLE,
        NOTFOUND;
    }
    
    // Pan/zoom constants
    private static final double DRAG_SCALE = 0.0025;
    private static final double ZOOM_SCALE = 0.02;
    private static final double ZOOM_MAX = 2.0;
    private static final double ZOOM_MIN = 0.5;
    
    // Data to draw
    private ArrayList<Star> stars = new ArrayList<>();
    private ArrayList<Constellation> constellations = new ArrayList<>();
    
    // Observer data
    public Date date = new Date();
    public double latitude = 0;
    public double longitude = 0;
    public double altitude = 0;
    public double azimuth = 0;
    
    // Settings
    private boolean showConstellations = true;
    private double scale = 1;
    
    // Window data
    private int offsetX = 0;
    private int offsetY = 0;
    
    // Last mouse position
    private int mouseX;
    private int mouseY;
    
    // Star info panel
    public StarInfo infoPanel;
    
    public StarMapPanel()
    {
        super();
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    /**
     * Sets a new observer position and updates the display accordingly.
     * @param date
     * @param lat
     * @param lon
     * @param alt
     * @param az 
     */
    public void setPosition(Date date, double lat, double lon, double alt, double az)
    {
        this.date = date;
        this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
        this.azimuth = az;
        updatePositions();
        repaint();
    }
    
    /**
     * Loads the stars and constellations to display
     * @param stars
     * @param constellations 
     */
    public void loadConstellations(ArrayList<Star> stars, ArrayList<Constellation> constellations)
    {
        this.stars = stars;
        this.constellations = constellations;
        updatePositions();
    }
    
    /**
     * Attempts to zoom in on a star or constellation given its name or common name.
     * @param name
     * @return Found, Not Found, or Not Visible
     */
    public SearchResult goTo(String name)
    {
        // Check if name matches a star.
        for(Star s : stars)
        {
            if(s.commonName.equalsIgnoreCase(name) || s.name.equalsIgnoreCase(name))
            {
                if(s.altitude >= 0 && s.altitude <= 90)
                {
                    //setPosition(latitude, longitude, s.altitude, s.azimuth);
                    setPosition(date, latitude, longitude, 0, s.azimuth);
                    scale = ZOOM_MAX;
                    return SearchResult.OK;
                }
                return SearchResult.NOTVISIBLE;
            }
        }
        
        // Check if name matches a constellation 
        for(Constellation c : constellations)
        {
            if(c.name.equalsIgnoreCase(name))
            {
                // Try to zoom in on its first star
                if(c.stars.get(0).altitude >= 0 && c.stars.get(0).altitude <= 90)
                {
                    setPosition(date, latitude, longitude, c.stars.get(0).altitude - .25, c.stars.get(0).azimuth);
                    //setPosition(latitude, longitude, 0, c.stars.get(0).azimuth);
                    scale = ZOOM_MAX;
                    return SearchResult.OK;
                }
                return SearchResult.NOTVISIBLE;
            }
        }
        
        return SearchResult.NOTFOUND;
    }
    
    private void updatePositions()
    {
        for(Star s : stars)
        {
            s.computePosition(date, latitude, longitude, altitude, azimuth);
        }
        for(Constellation c : constellations)
        {
            c.findCenter();
        }
    }

    /**
     * Sets the visibility of constellation lines and names
     * @param show 
     */
    public void setShowConstellations(boolean show)
    {
        showConstellations = show;
        repaint();
    }

    /**
     * Paints the stars and constellations on the panel
     * @param g 
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        // Calculate offset required to center the display
        if(this.getWidth() > this.getHeight())
        {
            // Wider than tall: center horizontally
            this.offsetY = 0;
            this.offsetX = (this.getWidth() - this.getHeight());
        }
        else if(this.getHeight() > this.getWidth())
        {
            // Taller than wide: center vertically
            this.offsetX = 0;
            this.offsetY = (this.getHeight() - this.getWidth()) / 2;
        }
        else
        {
            // Perfect square (never gonna happen)
            this.offsetX = 0;
            this.offsetY = 0;
        }
        
        
        g.setColor(Color.black);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        
        if(showConstellations)
        {
            for(Constellation c : constellations)
            {
                // Draw constellation name if any of its lines is visible
                boolean isVisible = false;
                
                // Draw constellation lines
                g.setColor(Color.gray);
                for(Line l : c.lines)
                {
                    if(starIsVisible(l.star1) || starIsVisible(l.star2))
                    {
                        isVisible = true;
                        g.drawLine(getStarX(l.star1), getStarY(l.star1),
                            getStarX(l.star2), getStarY(l.star2));
                    }
                }

                // Draw constellation name
                if(starIsVisible(c.stars.get(0)) && c.name != null)
                {
                    g.setColor(Color.red);
                    //g.drawString(c.name,
                    //        (int)(c.centerX * getScale() + offsetX),
                    //        this.getHeight() - (int)(c.centerY * getScale()) + offsetY);
                    g.drawString(c.name, getStarX(c.stars.get(0)) - 16, getStarY(c.stars.get(0)) + 16);
                }
            }
        }
        
        for(Star s : stars)
        {
            if(starIsVisible(s))
            {
                g.setColor(getColor(s));
                int diameter = getDiameter(s);
                int x = getStarX(s) - diameter/2;
                int y = getStarY(s) - diameter/2;
                g.fillOval(x, y, diameter, diameter);
                if(s.commonName != null)
                    g.drawString(s.commonName, x + 4, y - 4);
            }
        }
        
        //g.setColor(Color.black);
        //g.fillRect(0, 0, offsetX, getHeight());
        //g.fillRect(0, 0, getWidth(), offsetY);
    }
    
    private Color getColor(Star s)
    {
        if(s.magnitude <= 1)
            return new Color(0.6f, 0.6f, 1.0f);
        if(s.magnitude <= 2)
            return new Color(0.7f, 0.7f, 1.0f);
        if(s.magnitude <= 3)
            return new Color(0.8f, 0.8f, 1.0f);
        if(s.magnitude <= 4)
            return new Color(0.9f, 0.9f, 1.0f);
        return Color.white;
    }
    
    private double getScale()
    {
        if(this.getHeight() < this.getWidth())
            return this.getHeight() * scale;
        return this.getWidth() * scale;
    }
    
    private int getStarX(Star s)
    {
        double scaledX = s.getX() * getScale();
        return (int)scaledX + offsetX;
    }
    
    private int getStarY(Star s)
    {
        double scaledY = s.getY() * getScale();
        return this.getHeight() - (int)scaledY + offsetY;
    }
    
    private boolean starIsVisible(Star s)
    {
        int x = getStarX(s);
        int y = getStarY(s);
        return s.isVisible()
                && x >= 0 && x <= getWidth()
                && y >= 0 && y <= getWidth();
    }
    
    private int getDiameter(Star s)
    {
        return 9 - (int)s.magnitude;
    }

    /**
     * When the mouse is dragged, pan the display by adjusting azimuth and altitude.
     * @param me 
     */
    @Override
    public void mouseDragged(MouseEvent me)
    {
        double diffX = (me.getX() - mouseX) * DRAG_SCALE / scale;
        double diffY = (me.getY() - mouseY) * DRAG_SCALE / scale;
        
        azimuth -= diffX;
        azimuth %= 360;
        while(azimuth < 0)
            azimuth += 360;
        
        altitude += diffY;
        if(altitude > 90)
            altitude = 90;
        if(altitude < 0)
            altitude = 0;
        
        //System.out.printf("New altitude = %f\n", altitude);
        
        updatePositions();
        repaint();
        
        mouseX = me.getX();
        mouseY = me.getY();
    }

    /**
     * When the mouse moves, check if a star is close to the cursor and give it to the StarInfo panel.
     * If the StarInfo panel is null, do nothing.
     * @param me 
     */
    @Override
    public void mouseMoved(MouseEvent me)
    {
        if(infoPanel == null)
            return;
        
        mouseX = me.getX();
        mouseY = me.getY();
        
        for(Star s : stars)
        {
            if(Math.sqrt(Math.pow(getStarX(s) - mouseX, 2) + Math.pow(getStarY(s) - mouseY, 2)) < getDiameter(s) + 3)
            {
                infoPanel.SetStarLabel(s);
                return;
            }
        }
        
        infoPanel.SetStarLabel(null);
    }

    /**
     * When the mouse wheel is scrolled over the panel, adjust the zoom level.
     * @param mwe 
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe)
    {
        scale -= ZOOM_SCALE * mwe.getWheelRotation();
        if(scale > ZOOM_MAX)
            scale = ZOOM_MAX;
        else if(scale < ZOOM_MIN)
            scale = ZOOM_MIN;
        
        repaint();
    }
}
