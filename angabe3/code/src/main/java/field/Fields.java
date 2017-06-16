package field;

import field.location.Location;
import field.location.Locations;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter-mueller
 */
public class Fields {
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
        return Arrays.stream(moore)
                .map(center::relative)
                .filter(field::has)
                .collect(Collectors.toSet());
    }

    public static Set<Location> square(Field field, Location center, int distance) {
        final int width = distance * 2;
        final Location[] square = new Location[width * width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                square[x * width + y] = Location.of(x - distance, y - distance);
            }
        }
        return Arrays.stream(square)
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
        return Arrays.stream(neumann)
                .map(center::relative)
                .filter(field::has)
                .collect(Collectors.toSet());
    }
}
