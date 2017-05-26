package field;

import field.cell.Cell;
import field.location.Location;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter-mueller
 */
public class Fields {
    public static Set<Cell> moore(Field field, Location center) {
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
                .map(field::at)
                .collect(Collectors.toSet());
    }

    public static Field parseStringMap(final String map) {
        final String[] rows = map.split("\n");
        final int sizeX = rows[0].length();
        final int sizeY = rows.length;
        final Field field = new Field(sizeX, sizeY);

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                final Cell cell = field.at(Location.of(x, y));
                final char c = rows[y].charAt(x);
                fitCell(cell, c);
            }
        }

        return field;
    }

    public static String toStringMap(Field field) {
        final StringBuffer buffer = new StringBuffer();
        for (int y = 0; y < field.sizeY; y++) {
            for (int x = 0; x < field.sizeX; x++) {
                final Cell cell = field.at(Location.of(x, y));
                buffer.append(cell.isOccupied() ? '1' : '0');
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

    private static void fitCell(Cell cell, char c) {
        switch (c) {
            case '0':
                break;
            case '1':
                cell.occupy();
                break;
            default:
                final String s = String.format("Could not parse map with char %s!", c);
                throw new IllegalArgumentException(s);
        }
    }
}
