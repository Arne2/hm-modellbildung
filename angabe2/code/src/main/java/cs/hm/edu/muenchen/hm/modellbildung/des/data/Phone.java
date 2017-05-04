package cs.hm.edu.muenchen.hm.modellbildung.des.data;

/**
 * Created by Arne on 27.04.2017.
 */
public class Phone {
    private Person user = null;

    private final boolean residentPhone;

    public Phone(){
        this(false);
    }

    public Phone(boolean residentPhone) {
        this.residentPhone = residentPhone;
    }

    public boolean isResidentPhone() {
        return residentPhone;
    }

    public boolean isOccupied() {
        return user != null;
    }

    public void setUser(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null!");
        }
        this.user = person;
    }

    public Person removeUser() {
        final Person person = this.user;
        this.user = null;
        return person;
    }
}
