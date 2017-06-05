package field.view;

import field.Field;
import field.location.Location;

import java.util.Map;

/**
 * @author peter-mueller
 */
public class StringView {
    public static String personMap(final Field field) {
        final StringBuilder buffer = new StringBuilder();

        final Location edge = field.getLocations().stream()
                .max(Location::compareTo).orElse(Location.of(0, 0));

        for (int y = 0; y <= edge.y; y++) {
            for (int x = 0; x <= edge.x; x++) {
                final Location point = Location.of(x, y);
                if (field.has(point)) {
                    if (field.isTarget(point)) {
                        buffer.append("[X]");
                    } else if (field.isFree(point)) {
                        buffer.append("[ ]");
                    } else {
                        buffer.append("[p]");
                    }
                } else {
                    buffer.append("   ");
                }
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static String useMap(final Field field, final Map<Location, Double> use) {
        final StringBuilder buffer = new StringBuilder();

        final Location edge = field.getLocations().stream()
                .max(Location::compareTo).orElse(Location.of(0, 0));

        for (int y = 0; y <= edge.y; y++) {
            for (int x = 0; x <= edge.x; x++) {
                final Location point = Location.of(x, y);
                if (field.has(point)) {
                    if (use.containsKey(point)) {
                        buffer.append(String.format("[%5.02f]", use.get(point)));
                    } else {
                        buffer.append("[?????]");
                    }
                } else {
                    buffer.append("       ");
                }
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static Field parseStringMap(final String map) {
        final String[] rows = map.split("\n");
        final Field field = new Field(rows[0].length(), rows.length);

        for (int y = 0; y < rows.length; y++) {
            final String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                final char c = row.charAt(x);
                if (c == '0') {
                    field.addLocation(Location.of(x, y));
                } else if (c == 'X') {
                    field.setTarget(Location.of(x, y));
                }
            }
        }

        return field;
    }

    public static String toStringMap(Field field) {
        final StringBuilder buffer = new StringBuilder();

        final Location edge = field.getLocations().stream()
                .max(Location::compareTo).orElse(Location.of(0, 0));

        for (int y = 0; y <= edge.y; y++) {
            for (int x = 0; x <= edge.x; x++) {
                if (field.has(Location.of(x, y))) {
                    buffer.append("0");
                } else if (field.isTarget(Location.of(x, y))) {
                    buffer.append("X");
                } else {
                    buffer.append(" ");
                }
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
