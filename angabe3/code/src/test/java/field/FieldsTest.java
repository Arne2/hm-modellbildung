package field;

import field.cell.Cell;
import field.location.Location;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author peter-mueller
 */
public class FieldsTest {
    @Test
    public void toStringMap() throws Exception {
        final Field field = new Field(3, 2);
        final String map = Fields.toStringMap(field);
        if (!map.equals("000\n000\n")) {
            Assert.fail("Failed to make string map representation!");
        }

        field.asList().forEach(Cell::occupy);
        final String mapOccupied = Fields.toStringMap(field);
        if (!mapOccupied.equals("111\n111\n")) {
            Assert.fail("Failed to make string map occupied representation!");
        }
    }

    @Test
    public void moore() throws Exception {
        final String map = "0100\n" +
                "0100\n" +
                "1000\n";
        final Field field = Fields.parseStringMap(map);
        final Set<Cell> moore = Fields.moore(field, Location.of(0, 0));
        final Set<Cell> expected = new HashSet<>();
        expected.add(new Cell(Location.of(0, 1), false));
        expected.add(new Cell(Location.of(1, 0), true));
        expected.add(new Cell(Location.of(1, 1), true));
        if (!moore.equals(expected)) {
            Assert.fail("Moore does not match expected result!");
        }
    }

    @Test
    public void parseStringMap() throws Exception {
        final String map = "0100\n0100\n1000\n";
        final Field field = Fields.parseStringMap(map);

        if (!field.at(Location.of(1, 1)).isOccupied()) {
            Assert.fail("Map has no wall at location 1,1!");
        }
        if (field.at(Location.of(0, 0)).isOccupied()) {
            Assert.fail("Map has a wall at location 0,0!");
        }
        if (!map.equals(Fields.toStringMap(field))) {
            Assert.fail("Map was not correctly parsed!");
        }
    }

}