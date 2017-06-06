package field.use;

import field.Field;
import field.location.Location;
import field.view.StringView;
import org.junit.Assert;
import org.junit.Test;
import person.Person;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author peter-mueller
 */
public class PersonalSpaceTest {
    private final PersonalSpace personalSpace = new PersonalSpace(new PersonalSpace.Settings());
    @Test
    public void useNoOtherPersons() throws Exception {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n" +
                        "00000\n", 20
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        final double use = personalSpace.use(field, Location.of(2, 2));
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
                        "00000\n", 20
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(0,0));
        final double use = personalSpace.use(field, Location.of(2, 2));
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
                        "00000\n", 50
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(0,0));
        final double use = personalSpace.use(field, Location.of(2, 2));
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
                        "00000\n", 20
        );

        field.putPerson(new Person(1.0), Location.of(2,2));
        field.putPerson(new Person(1.0), Location.of(0,0));
        final double use = personalSpace.use(field, Location.of(2, 2));
        if (use == 0) {
            final String message = String.format("use should be influenced. was: %s", use);
            Assert.fail(message);
        }

        field.putPerson(new Person(1.0), Location.of(0,1));
        final double use2 = personalSpace.use(field, Location.of(2, 2));
        if (use2 <= use ) {
            final String message = String.format("use should be influenced. was: %s", use2);
            Assert.fail(message);
        }
        field.putPerson(new Person(1.0), Location.of(0,2));
        final double use3 = personalSpace.use(field, Location.of(2, 2));
        if (use3 <= use2 ) {
            final String message = String.format("use should be influenced. was: %s", use3);
            Assert.fail(message);
        }
    }
}