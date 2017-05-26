package field;

import field.cell.Cell;
import field.location.Location;

import java.util.*;

public class Field {
    private final Map<Location, Cell> cells = new HashMap<>();

    public Field() {this(0,0);}

    public Field(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                put(new Cell(Location.of(x,y)));
            }
        }
    }

    public void put(Cell cell) {
        cells.put(cell.location(), cell);
    }

    public Collection<Cell> cells() {
        return cells.values();
    }

    public Set<Location> locations() {
        return cells.keySet();
    }
    public Cell at(Location location) {
        if (!has(location)) {
            final String message = String.format(
                    "Location %s is not in the field!",
                    location);
            throw new IllegalArgumentException(message);
        }
        return cells.get(location);
    }

    public boolean has(Location location) {
        return cells.containsKey(location);
    }
}
