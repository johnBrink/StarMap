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
    
    public static class Line
    {
        public Star star1;
        public Star star2;
    }
    
    public static Constellation deserialize(Element elem, ArrayList<Star> allStars)
    {
        Constellation c = new Constellation();
        
        c.name = elem.getChildTextTrim("name");
        c.abbreviation = elem.getChildTextTrim("abbr");
        
        for(Element node : elem.getChildren("line"))
        {
            Line l = getLine(node.getTextTrim(), allStars);
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
    
    private static Line getLine(String str, ArrayList<Star> allStars)
    {
        Line l = new Line();
        String[] words = str.split(" ");
        
        for(Star s : allStars)
        {
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
        
        return l;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (%s)", name, abbreviation);
    }
}
