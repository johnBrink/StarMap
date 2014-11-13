/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.io.InputStream;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * A static class that reads a set of stars and constellations from a file
 * @author John Brink
 */
public class FileLoader {
    private static ArrayList<Star> stars = null;
    private static ArrayList<Constellation> constellations = null;
    
    public static ArrayList<Star> getStars()
    {
        if(stars == null)
        {
            // Find all the stars
            try
            {
                InputStream istream = FileLoader.class.getResourceAsStream("/starmap/resources/stars.xml");
                SAXBuilder builder = new SAXBuilder();
                Document doc = (Document)builder.build(istream);
                Element root = doc.getRootElement();

                System.out.printf("Parsing in %d stars\n", root.getChildren().size());

                for(Element node : root.getChildren())
                {
                    Star newStar = Star.deserialize(node);
                    stars.add(newStar);
                    //System.out.printf("Parsed a star called %s\n", newStar.toString());
                }

                // Root element is "xml"
                // Convert each child into Star object
            }
            catch(Exception e)
            {
                System.out.printf("%s error reading Stars XML file: %s\n", e.getClass().getName(), e.getMessage());
            }
        }
        
        return stars;
    }
    
    public static ArrayList<Constellation> getConstellations()
    {   
        if(constellations == null)
        {
            if(stars == null)
                getStars();
            
            // Build the constellations
            try
            {
                InputStream istream = FileLoader.class.getResourceAsStream("/starmap/resources/constellations.xml");
                SAXBuilder builder = new SAXBuilder();
                Document doc = (Document)builder.build(istream);
                Element root = doc.getRootElement();

                for(Element node : root.getChildren())
                {
                    Constellation newConst = Constellation.deserialize(node, stars);
                    constellations.add(newConst);
                    //System.out.printf("Parsed a constellation called %s\n", newConst.toString());
                }    
            }
            catch(Exception e)
            {
                System.out.printf("%s error reading Constellations XML file: %s\n", e.getClass().getName(), e.getMessage());
            }
        }
        
        return constellations;
    }
    
    public static String getAttribute(Element elem, String name, String defaultValue)
    {
        try
        {
            return elem.getChildTextTrim(name);
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }       
}
