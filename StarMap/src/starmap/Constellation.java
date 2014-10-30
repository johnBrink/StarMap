package starmap;

import java.util.ArrayList;

/**
 *
 * @author 7058191
 */
public class Constellation {
    public ArrayList<Star> stars;
    public ArrayList<Line> lines;
    
    public class Line
    {
        public Star star1;
        public Star star2;
    }
}
