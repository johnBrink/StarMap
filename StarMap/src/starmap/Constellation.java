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
    public ArrayList<Star> stars;
    public ArrayList<Line> lines;
    
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
            if(s.name.equals(words[0]))
            {
                l.star1 = s;
            }
            else if(s.name.equals(words[2]))
            {
                l.star2 = s;
            }
        }
        
        return l;
    }
}
