package field.use;

import field.Field;
import field.location.Location;
import field.view.StringView;
import org.junit.Assert;
import org.junit.Test;
import person.Person;

/**
 * @author peter-mueller
 */
public class PersonalSpaceTest {
    public static final int LARGE_CELL_SIZE = 1;
    public static final double CELL_SIZE = 0.40;

    private final PersonalSpace personalSpace = new PersonalSpace();

    @Test
    public void useNoOtherPersons() throws Exception {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n", CELL_SIZE
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        final double use = personalSpace.use(field, Location.of(2, 2), Location.of(2,2));
        if (use != 0.0) {
            final String message = String.format("use should be zero but was: %s", use);
            Assert.fail(message);
        }
    }

    @Test
    public void oneOtherPerson() throws Exception {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n", CELL_SIZE
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(0,0));
        final double use = personalSpace.use(field, Location.of(2, 2), Location.of(2,2));
        if (use == 0.0) {
            final String message = String.format("use should be influenced but was: %s", use);
            Assert.fail(message);
        }
    }

    @Test
    public void notInRange() throws Exception {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n", LARGE_CELL_SIZE
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(0,0));
        final double use = personalSpace.use(field, Location.of(2, 2), Location.of(2,2));
        if (use != 0.0) {
            final String message = String.format("use should not be influenced, because the person is to far away. was: %s", use);
            Assert.fail(message);
        }
    }

    @Test
    public void multiplePersons() throws Exception {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n", CELL_SIZE
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(2,1));
        final double use = personalSpace.use(field, Location.of(2, 2), Location.of(2,2));
        if (use == 0) {
            final String message = String.format("use should be influenced. was: %s", use);
            Assert.fail(message);
        }

        field.putPerson(new Person(1.0), Location.of(0,1));
        final double use2 = personalSpace.use(field, Location.of(2, 2), Location.of(2,2));
        if (use2 > use ) {
            final String message = String.format("use should be influenced. was: %s", use2);
            Assert.fail(message);
        }
        field.putPerson(new Person(1.0), Location.of(0,2));
        final double use3 = personalSpace.use(field, Location.of(2, 2),Location.of(2,2));
        if (use3 > use2) {
            final String message = String.format("use should be influenced. was: %s", use3);
            Assert.fail(message);
        }
    }
}