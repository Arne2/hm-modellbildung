package field;

import field.location.Location;

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

    public static Field parseStringMap(final String map) {
        final String[] rows = map.split("\n");
        final Field field = new Field();

        for (int y = 0; y < rows.length; y++) {
            final String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                final char c = row.charAt(x);
                if (c == '0') {
                    field.add(Location.of(x, y));
                }
            }
        }

        return field;
    }

    public static String toStringMap(Field field) {
        final StringBuilder buffer = new StringBuilder();

        final Location edge = field.locations().stream()
                .max(Location::compareTo).orElse(Location.of(0, 0));

        for (int y = 0; y <= edge.y; y++) {
            for (int x = 0; x <= edge.x; x++) {
                if (field.has(Location.of(x, y))) {
                    buffer.append("0");
                } else {
                    buffer.append(" ");
                }
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
