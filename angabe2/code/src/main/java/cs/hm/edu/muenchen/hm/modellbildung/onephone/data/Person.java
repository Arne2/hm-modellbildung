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
    public String toString() {
        return "{" +
                "id: " + id +
                ", resident: " + resident +
                '}';
    }
}
