package cs.hm.edu.muenchen.hm.modellbildung.des.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author peter-mueller
 */
public class PersonTest {

    @Test
    public void getId() throws Exception {
        final Person p1 = new Person();
        final Person p2 = new Person();

        if (p1.getId() + 1  != p2.getId() ) {
            Assert.fail("IDs are not correctly set in counting manor!");
        }
    }

    @Test
    public void isResident() throws Exception {
        final Person person = new Person(true);
        if (!person.isResident()) {
            Assert.fail("Person is no resident!");
        }
    }

    @Test
    public void equals() throws Exception {
        final Person p1 = new Person();
        final Person p2 = new Person();
        final Person a3 = new Person() {

        };
        if (p1.equals(p2)) {
            Assert.fail("Persons must not match but did!");
        }
        if (p1.equals(null)) {
            Assert.fail("Person must not equal null!");
        }
        if (!p1.equals(p1)) {
            Assert.fail("Person did not equal itself!");
        }
        if (p1.equals(a3)) {
            Assert.fail("Person must not equal to a different class!");
        }
    }

    @Test
    public void hashCodeTest() throws Exception {
        final Person person = new Person();
        if (person.getId() != person.hashCode()) {
            Assert.fail("Hashcode must be the id of the person.");
        }
    }
}