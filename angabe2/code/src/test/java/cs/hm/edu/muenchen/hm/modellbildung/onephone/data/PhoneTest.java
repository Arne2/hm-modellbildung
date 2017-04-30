package cs.hm.edu.muenchen.hm.modellbildung.onephone.data;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author peter-mueller
 */
public class PhoneTest {

    @Test
    public void isOccupied() throws Exception {
        final Phone phone = new Phone();
        if (phone.isOccupied()) {
            Assert.fail("Phone must not be occupied!");
        }
        phone.setUser(new Person());
        if (!phone.isOccupied()) {
            Assert.fail("Phone must be occupied!");
        }
        phone.removeUser();
        if (phone.isOccupied()) {
            Assert.fail("Phone must not be occupied!");
        }
    }

    @Test
    public void setUser() throws Exception {
        final Person person = new Person();
        final Phone phone = new Phone();
        phone.setUser(person);
    }
    @Test(expected = IllegalArgumentException.class)
    public void setUserFails() throws Exception {
        final Phone phone = new Phone();
        phone.setUser(null);
    }

    @Test
    public void removeUser() throws Exception {
        final Person person = new Person();
        final Phone phone = new Phone();
        phone.setUser(person);
        if (!phone.removeUser().equals(person)) {
            Assert.fail("Wrong user Person was returned!");
        }
    }
}