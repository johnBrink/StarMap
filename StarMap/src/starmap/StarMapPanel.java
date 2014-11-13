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
import java.util.ArrayList;
import javax.swing.JPanel;
import starmap.Constellation.Line;

/**
 * A JPanel that displays a set of stars and constellations
 * @author John Brink
 */
public class StarMapPanel extends JPanel implements MouseMotionListener
{
    private final double DRAG_SCALE = 0.05;
    
    private ArrayList<Star> stars = new ArrayList<>();
    private ArrayList<Constellation> constellations = new ArrayList<>();
    private double latitude = 0;
    private double longitude = 0;
    private double altitude = 0;
    private double azimuth = 0;
    
    public boolean showConstellations = true;
    
    private int offsetX = 0;
    private int offsetY = 0;
    
    private int mouseX;
    private int mouseY;
    
    public StarMapPanel()
    {
        super();
        addMouseMotionListener(this);
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
    
    private void updatePositions()
    {
        for(Star s : stars)
        {
            s.computePosition(latitude, longitude, altitude, azimuth);
        }
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
            this.offsetX = (this.getWidth() - this.getHeight()) / 2;
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
                // Draw constellation lines
                g.setColor(Color.gray);
                for(Line l : c.lines)
                {
                    g.drawLine(getStarX(l.star1), getStarY(l.star1),
                        getStarX(l.star2), getStarY(l.star2));
                }

                // Draw constellation name
                if(c.name != null)
                {
                    g.setColor(Color.red);
                    c.findCenter();
                    g.drawString(c.name,
                            (int)(c.centerX * getScale()),
                            this.getHeight() - (int)(c.centerY * getScale()));
                }
            }
        }
        
        for(Star s : stars)
        {
            g.setColor(getColor(s));
            if(s.isVisible())
            {
                int diameter = getDiameter(s);
                int x = getStarX(s);
                int y = getStarY(s);
                g.fillOval(x, y, diameter, diameter);
                if(s.commonName != null)
                    g.drawString(s.commonName, x + 4, y - 4);
            }
        }
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
    
    private int getScale()
    {
        if(this.getHeight() < this.getWidth())
            return this.getHeight();
        return this.getWidth();
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
    
    private int getDiameter(Star s)
    {
        return 9 - (int)s.magnitude;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        double diffX = (me.getX() - mouseX) * DRAG_SCALE;
        double diffY = (me.getY() - mouseY) * DRAG_SCALE;
        
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

    @Override
    public void mouseMoved(MouseEvent me) {
        mouseX = me.getX();
        mouseY = me.getY();
    }
}
