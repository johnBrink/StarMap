/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.io.File;
import java.util.ArrayList;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * A static class that reads a set of stars and constellations from a file
 * @author John Brink
 */
public class FileLoader {
    public static ArrayList<Constellation> getConstellations(String starsFile, String constellationsFile)
    {
        ArrayList<Star> stars = new ArrayList<>();
        ArrayList<Constellation> constellations = new ArrayList<>();
        System.out.print(starsFile);
        System.out.print("\n");
        // Find all the stars
        try
        {
            SAXBuilder builder = new SAXBuilder();
            File file = new File(starsFile);
            Document doc = (Document)builder.build(file);
            Element root = doc.getRootElement();
            
            for(Element node : root.getChildren())
            {
                stars.add(Star.deserialize(node));
            }
            
            // Root element is "xml"
            // Convert each child into Star object
        }
        catch(Exception e)
        {
            System.out.printf("Error reading Stars XML file: %s\n", e.getMessage());
            return constellations;
        }
        
        // Build the constellations
        try
        {
            SAXBuilder builder = new SAXBuilder();
            File file = new File(constellationsFile);
            Document doc = (Document)builder.build(file);
            Element root = doc.getRootElement();
            
            for(Element node : root.getChildren())
            {
                constellations.add(Constellation.deserialize(node, stars));
            }    
        }
        catch(Exception e)
        {
            System.out.printf("Error reading Constellations XML file: %s\n", e.getMessage());
        }
        
        return constellations;
    }
}
