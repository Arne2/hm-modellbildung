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
        final Field field = new Field();
        field.put(new Cell(Location.of(0,0)));
        field.put(new Cell(Location.of(1,0)));
        field.put(new Cell(Location.of(0,1)));
        field.put(new Cell(Location.of(1,1)));
        final String map = Fields.toStringMap(field);
        if (!map.equals("00\n00\n")) {
            Assert.fail("Failed to make string map representation!");
        }

        final Field field2 = new Field();
        field2.put(new Cell(Location.of(0,0)));
        field2.put(new Cell(Location.of(0,1)));
        field2.put(new Cell(Location.of(1,1)));
        final String map2 = Fields.toStringMap(field2);
        if (!map2.equals("0 \n00\n")) {
            Assert.fail("Failed to make string map2 representation!");
        }

    }

    @Test
    public void moore() throws Exception {
        final String map = "0 00\n" +
                "0 00\n" +
                " 000\n";
        final Field field = Fields.parseStringMap(map);
        final Set<Cell> moore = Fields.moore(field, Location.of(0, 0));
        final Set<Cell> expected = new HashSet<>();
        expected.add(new Cell(Location.of(0, 1), false));
        if (!moore.equals(expected)) {
            Assert.fail("Moore does not match expected result!");
        }
    }

    @Test
    public void parseStringMap() throws Exception {
        final String map = "0 00\n0 00\n 000\n";
        final Field field = Fields.parseStringMap(map);

        if (field.has(Location.of(1, 1))) {
            Assert.fail("Map should not have 1,1!");
        }
        if (!field.has(Location.of(0, 0))) {
            Assert.fail("Map should have 0,0!");
        }
        if (!map.equals(Fields.toStringMap(field))) {
            Assert.fail("Map was not correctly parsed!");
        }
    }

}