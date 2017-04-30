package cs.hm.edu.muenchen.hm.modellbildung.onephone.data;

/**
 * @author peter-mueller
 */
public class Person {
    private static int UNIQUE_ID = 0;
    private final int id = ++UNIQUE_ID;

    private final boolean resident;

    public Person() {
        this(false);
    }
    public Person(boolean resident) {
        this.resident = resident;
    }

    public int getId() {
        return id;
    }

    public boolean isResident() {
        return resident;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
