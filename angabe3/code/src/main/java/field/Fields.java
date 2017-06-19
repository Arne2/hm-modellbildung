package field;

import field.location.Location;
import field.location.Locations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter-mueller
 * Collection of static helping methods for using the Field class.
 */
public class Fields {
    /**
     * Returns a Set of the Moore-Neighbour Fields.
     * @param field
     * @param center
     * @return Moore-Neighbours.
     */
    public static Set<Location> moore(Field field, Location center) {
        if (field == null) {
            throw new IllegalArgumentException("Field cannot be null!");
        }
        if (center == null) {
            throw new IllegalArgumentException("Center location cannot be null!");
        }
        final Location[] moore = new Location[]{
                new Location(-1, -1), new Location(0, -1), new Location(1, -1),
                new Location(-1, 0), new Location(1, 0),
                new Location(-1, 1), new Location(0, 1), new Location(1, 1),
        };
        return of(field, center, moore);
    }

    /**
     * Returns an array of all Location up to a certain distance.
     * @param distance
     * @return area of Locations.
     */
    public static Location[] circleSelector(int distance) {
        final int width = distance * 2 + 1;
        final List<Location> circle = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (Math.hypot(x-distance,y-distance) < distance) {
                    circle.add(Location.of(x-distance, y-distance));
                }
            }
        }
        return circle.toArray(new Location[0]);
    }

    /**
     * Use the selector mask repositioned on the center location to receive the neighbourhood around it.
     *
     * @param field field to find the location in
     * @param center center to put the selector on
     * @param selector selector (relative) locations to be used aound the center
     * @return the existing locations in the field defined by selector and center
     */
    public static Set<Location> of(Field field, Location center, Location[] selector) {
        return Arrays.stream(selector)
                .map(center::relative)
                .filter(field::has)
                .collect(Collectors.toSet());
    }

    public static Set<Location> neumann(Field field, Location center) {
        if (field == null) {
            throw new IllegalArgumentException("Field cannot be null!");
        }
        if (center == null) {
            throw new IllegalArgumentException("Center location cannot be null!");
        }
        final Location[] neumann = new Location[]{
                new Location(0, -1), new Location(-1, 0),
                new Location(1, 0), new Location(0, 1),
        };
        return of(field, center, neumann);
    }
}
