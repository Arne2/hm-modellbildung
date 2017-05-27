package field;

import field.location.Location;
import field.view.StringView;
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
        field.addLocation(Location.of(0, 0));
        field.addLocation(Location.of(1, 0));
        field.addLocation(Location.of(0, 1));
        field.addLocation(Location.of(1, 1));
        final String map = StringView.toStringMap(field);
        if (!map.equals("00\n00\n")) {
            Assert.fail("Failed to make string map representation!");
        }

        final Field field2 = new Field();
        field2.addLocation(Location.of(0, 0));
        field2.addLocation(Location.of(0, 1));
        field2.addLocation(Location.of(1, 1));
        final String map2 = StringView.toStringMap(field2);
        if (!map2.equals("0 \n00\n")) {
            Assert.fail("Failed to make string map2 representation!");
        }

    }

    @Test
    public void moore() throws Exception {
        final String map = "0 00\n" +
                "0 00\n" +
                " 000\n";
        final Field field = StringView.parseStringMap(map);
        final Set<Location> moore = Fields.moore(field, Location.of(0, 0));
        final Set<Location> expected = new HashSet<>();
        expected.add(Location.of(0, 1));
        if (!moore.equals(expected)) {
            Assert.fail("Moore does not match expected result!");
        }
    }

    @Test
    public void parseStringMap() throws Exception {
        final String map = "0 00\n0 00\n 000\n";
        final Field field = StringView.parseStringMap(map);

        if (field.has(Location.of(1, 1))) {
            Assert.fail("Map should not have 1,1!");
        }
        if (!field.has(Location.of(0, 0))) {
            Assert.fail("Map should have 0,0!");
        }
        if (!map.equals(StringView.toStringMap(field))) {
            Assert.fail("Map was not correctly parsed!");
        }
    }

}