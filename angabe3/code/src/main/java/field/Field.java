package field;

import field.cell.Cell;
import field.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field {
    public final int sizeX;
    public final int sizeY;
    private final Cell[][] cells;

    public Field(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cells = new Cell[sizeX][sizeY];
        this.init();
    }

    private void init() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                cells[x][y] = new Cell(new Location(x, y));
            }
        }
    }

    @Override
    public String toString() {
        return "Field{" +
                "cells=" + Arrays.deepToString(cells) +
                '}';
    }

    public List<Cell> asList() {
        final List<Cell> list = new ArrayList<>();
        for (Cell[] row : cells) {
            list.addAll(Arrays.asList(row));
        }
        return list;
    }

    public Cell at(Location location) {
        if (!has(location)) {
            final String message = String.format(
                    "Location %s is not in the field!",
                    location);
            throw new IllegalArgumentException(message);
        }
        return cells[location.x][location.y];
    }

    public boolean has(Location location) {
        final int x = location.x;
        final int y = location.y;
        return x < sizeX && y < sizeY && x >= 0 && y >= 0;
    }
}
