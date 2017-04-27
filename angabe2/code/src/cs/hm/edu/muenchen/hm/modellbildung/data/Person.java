package cs.hm.edu.muenchen.hm.modellbildung.data;

/**
 * @author peter-mueller
 */
public class Person {
    private final int id;

    private final boolean resident;

    public Person(int id, boolean resident) {
        this.id = id;
        this.resident = resident;
    }

    public int getId() {
        return id;
    }

    public boolean isResident() {
        return resident;
    }
}
