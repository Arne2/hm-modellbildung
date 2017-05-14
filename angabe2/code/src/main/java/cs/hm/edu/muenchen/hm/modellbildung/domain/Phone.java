package cs.hm.edu.muenchen.hm.modellbildung.domain;

/**
 * This class represents a phone of the Callshop.
 * Its occupied when a Person uses it.
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

    /**
     * When the current user is done the variable is set to null.
     * @return the user
     */
    public Person removeUser() {
        final Person person = this.user;
        this.user = null;
        return person;
    }

    public Person getUser() {
        return user;
    }
}
