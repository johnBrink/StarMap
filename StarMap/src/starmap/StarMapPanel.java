/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
import starmap.Constellation.Line;

/**
 * A JPanel that displays a set of stars and constellations
 * @author John Brink
 */
public class StarMapPanel extends JPanel
{
    private ArrayList<Constellation> constellations = new ArrayList<>();
    private double latitude = 0;
    private double longitude = 0;
    private double altitude = 0;
    private double azimuth = 0;

    public void setPosition(double lat, double lon, double alt, double az)
    {
        this.latitude = lat;
        this.longitude = lon;
        this.altitude = alt;
        this.azimuth = az;
        updatePositions();
    }
    
    public void loadConstellations(ArrayList<Constellation> constellations)
    {
        this.constellations = constellations;
        updatePositions();
    }
    
    private void updatePositions()
    {
        for(Constellation c : constellations)
        {
            for(Star s : c.stars)
            {
                s.computePosition(latitude, longitude, altitude, azimuth);
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.setColor(Color.black);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        g.setColor(Color.white);
        for(Constellation c : constellations)
        {
            for(Star s : c.stars)
            {
                if(s.isVisible())
                {
                    int diameter = getDiameter(s);
                    g.fillOval(getStarX(s), getStarY(s), diameter, diameter);
                }
            }
            
            for(Line l : c.lines)
            {
                g.drawLine(getStarX(l.star1), getStarY(l.star1),
                    getStarX(l.star2), getStarY(l.star2));
            }
        }
    }
    
    private int getStarX(Star s)
    {
        return this.getX() + (int)s.getX();
    }
    
    private int getStarY(Star s)
    {
        return this.getY() + (int)s.getY();
    }
    
    private int getDiameter(Star s)
    {
        return 6 - (int)s.magnitude;
    }
}
