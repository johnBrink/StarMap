/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starmap;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author John Brink
 */
public class FileLoader {
    public static ArrayList<Star> getStars(String filename)
    {
        ArrayList<Star> stars = new ArrayList<Star>();
        try
        {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
            // Root element is "xml"
            // Convert each child into Star object
        }
        catch(FileNotFoundException e)
        {
        }
        
        return stars;
    }
}
