package field.location;

/**
 * Provides a toolset for working with locations.
 * @author peter-mueller
 */
public class Locations {
    /**
     * Calculate the (direct, air) distance between two locations.
     * @param a first location
     * @param b second location
     * @return the distance of both locations
     */
    public static double distance(Location a, Location b) {
        if (a.equals(b)) {
            return 0;
        }
       return hypot(a.x - b.x, a.y-b.y);
    }

    private static double hypot(int x, int y) {
        return Math.sqrt(x*x + y*y);
    }
}
