package field.location;

import config.Configuration;

/**
 * @author peter-mueller
 */
public class Locations {
    private final static Configuration conf = Configuration.INSTANCE;

    public static double distance(Location a, Location b) {
        if (a.equals(b)) {
            return 0;
        }
       return Math.hypot(a.x - b.x, a.y-b.y) * conf.cellSize;
    }
}
