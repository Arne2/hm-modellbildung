package field.location;

/**
 * @author peter-mueller
 */
public class Locations {
    public static double distance(Location a, Location b) {
        if (a.equals(b)) {
            return 0;
        }
       return Math.hypot(a.x - b.x, a.y-b.y);
    }
}
