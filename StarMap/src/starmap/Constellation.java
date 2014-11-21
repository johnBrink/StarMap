package starmap;

import java.util.ArrayList;
import org.jdom2.Element;

/**
 * A collection of Stars and lines connecting them, along with an identifier.
 * @author John Brink
 */
public class Constellation {
    public String name;
    public String abbreviation;
    public ArrayList<Star> stars = new ArrayList<>();
    public ArrayList<Line> lines = new ArrayList<>();
    public double centerX;
    public double centerY;
    
    public static class Line
    {
        public Star star1;
        public Star star2;
    }
    
    public static Constellation deserialize(Element elem, ArrayList<Star> allStars)
    {
        Constellation c = new Constellation();
        
        c.name = FileLoader.getAttribute(elem, "name", null);
        c.abbreviation = FileLoader.getAttribute(elem, "abbr", null);
        
        for(Element node : elem.getChildren("line"))
        {
            Line l = getLine(c, node.getTextTrim(), allStars);
            if(l == null)
            {
                // If couldn't find both stars, don't add the line
                continue;
            }
            
            if(!c.stars.contains(l.star1))
            {
                c.stars.add(l.star1);
            }
            if(!c.stars.contains(l.star2))
            {
                c.stars.add(l.star2);
            }
            c.lines.add(l);
        }
        
        return c;
    }
    
    private static Line getLine(Constellation c, String str, ArrayList<Star> allStars)
    {
        Line l = new Line();
        String[] words = str.split(" ");
        
        for(Star s : allStars)
        {
            if(!s.constellation.equals(c.abbreviation))
            {
                continue;
            }
            
            if(words[0].equals(s.name) || words[0].equals(s.commonName))
            {
                l.star1 = s;
            }
            else if(words[2].equals(s.name) || words[2].equals(s.commonName))
            {
                l.star2 = s;
            }
            
            if(l.star1 != null && l.star2 != null)
            {
                break;
            }
        }
        
        if(l.star1 == null || l.star2 == null)
        {
            System.out.printf("Error: could not find stars for line [%s]\n", str);
            if(l.star1 == null)
                System.out.printf("    Missing %s\n", words[0]);
            if(l.star2 == null)
                System.out.printf("    Missing %s\n", words[2]);
            return null;
        }
        
        return l;
    }
    
    /**
     * Sets the Constellation's center position to the center of all the star's positions
     */
    public void findCenter()
    {
        double minX = 999;
        double maxX = -999;
        double minY = 999;
        double maxY = -999;
        for(Star s : stars)
        {
            double x = s.getX();
            double y = s.getY();
            
            if(x < minX)
                minX = x;
            if(x > maxX)
                maxX = x;
            if(y < minY)
                minY = y;
            if(y > maxY)
                maxY = y;
        }
        
        centerX = (minX + maxX) / 2;
        centerY = (minY + maxY) / 2;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (%s)", name, abbreviation);
    }
}
