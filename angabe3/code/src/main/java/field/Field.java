package field;

import field.location.Location;
import person.Person;

import java.util.*;

/**
 * Provides a class that describes a area with a set of locations that describe
 * the possible cells a person can walk on. Persons can move on these locations
 * and the field has one single target location.
 */
public class Field {
    /** Possible cells a person can walk to */
    private final Set<Location> locations = new HashSet<>();
    /** All persons with their locations. */
    private final Map<Person, Location> persons = new HashMap<>();
    /** The single target in this field. */

    private Set<Location> targets = new HashSet<>();

    private final int width;

    private final int height;

    public Field() {
        this(0, 0);
    }

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void fillField(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                locations.add(Location.of(x, y));
            }
        }
    }

    public Map<Person, Location> getPersons() {
        return Collections.unmodifiableMap(persons);
    }


    public Set<Location> getTargets(){
        return this.targets;
    }

    public void addTarget(Location target){
        locations.add(target);
        targets.add(target);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean addLocation(Location location) {
        return locations.add(location);
    }

    public boolean isTarget(Location target) {
        return targets.contains(target);
    }

    public void putPerson(Person person, Location location) {
        persons.put(person, location);
    }

    public Set<Location> getLocations() {
        return Collections.unmodifiableSet(locations);
    }

    public Location locationOf(Person person) {
        return persons.get(person);
    }

    public boolean isFree(Location location) {
        return !persons.containsValue(location);
    }

    public boolean has(Location location) {
        return locations.contains(location);
    }

    public void removePerson(Person person) {
        persons.remove(person);
    }

    public int numberOfPersons(){
        return persons.size();
    }
}
