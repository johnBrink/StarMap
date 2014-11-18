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
import javax.swing.JPanel;
import starmap.Constellation.Line;

/**
 * A JPanel that displays a set of stars and constellations
 * @author John Brink
 */
public class StarMapPanel extends JPanel implements MouseMotionListener, MouseWheelListener
{
    private static final double DRAG_SCALE = 0.0025;
    private static final double ZOOM_SCALE = 0.01;
    private static final double ZOOM_MAX = 2.0;
    private static final double ZOOM_MIN = 0.5;
    
    private ArrayList<Star> stars = new ArrayList<>();
    private ArrayList<Constellation> constellations = new ArrayList<>();
    private double latitude = 0;
    private double longitude = 0;
    private double altitude = 0;
    private double azimuth = 0;
    
    private boolean showConstellations = true;
    
    private double scale = 1;
    
    private int offsetX = 0;
    private int offsetY = 0;
    
    private int mouseX;
    private int mouseY;
    
    public StarInfo infoPanel;
    
    public StarMapPanel()
    {
        super();
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void setPosition(double lat, double lon, double alt, double az)
    {
        this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
        this.azimuth = az;
        updatePositions();
        repaint();
    }
    
    public void loadConstellations(ArrayList<Star> stars, ArrayList<Constellation> constellations)
    {
        this.stars = stars;
        this.constellations = constellations;
        updatePositions();
    }
    
    public boolean goTo(String name)
    {
        for(Star s : stars)
        {
            if(s.commonName.equalsIgnoreCase(name) || s.name.equalsIgnoreCase(name))
            {
                if(s.altitude >= 0 && s.altitude <= 90)
                {
                    setPosition(latitude, longitude, s.altitude, s.azimuth);
                    return true;
                }
                return false;
            }
        }
        
        return false;
    }
    
    private void updatePositions()
    {
        for(Star s : stars)
        {
            s.computePosition(latitude, longitude, altitude, azimuth);
        }
        for(Constellation c : constellations)
        {
            c.findCenter();
        }
    }
    
    public void setShowConstellations(boolean show)
    {
        showConstellations = show;
        repaint();
    }
    
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
                    if(starIsVisible(l.star1) && starIsVisible(l.star2))
                    {
                        isVisible = true;
                        g.drawLine(getStarX(l.star1), getStarY(l.star1),
                            getStarX(l.star2), getStarY(l.star2));
                    }
                }

                // Draw constellation name
                if(isVisible && c.name != null)
                {
                    g.setColor(Color.red);
                    g.drawString(c.name,
                            (int)(c.centerX * getScale() + offsetX),
                            this.getHeight() - (int)(c.centerY * getScale()) + offsetY);
                }
            }
        }
        
        for(Star s : stars)
        {
            if(starIsVisible(s))
            {
                g.setColor(getColor(s));
                int diameter = getDiameter(s);
                int x = getStarX(s);
                int y = getStarY(s);
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

    @Override
    public void mouseDragged(MouseEvent me) {
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
    public void mouseMoved(MouseEvent me) {
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
