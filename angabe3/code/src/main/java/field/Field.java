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
    private final TreeSet<Location> personLocations = new TreeSet<>();
    /** The single target in this field. */

    private final Set<Location> measurePoints = new HashSet<>();

    private Set<Location> targets = new HashSet<>();

    /** The size of a quadratic cell. Size equals the with and the height. */
    private final double cellSize;

    public Field(double cellSize) {
        this.cellSize = cellSize;
    }

    public Map<Person, Location> getPersons() {
        return Collections.unmodifiableMap(persons);
    }


    public Set<Location> getTargets(){
        return this.targets;
    }

    public void addMeasurePoint(Location point){
        measurePoints.add(point);
    }

    public Set<Location> getMeasurementPoints(){
        return measurePoints;
    }

    public void addTarget(Location target){
        locations.add(target);
        targets.add(target);

    }

    public int getWidth() {
        return locations.stream()
                .map(location -> location.x)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
    }

    public int getHeight() {
        return locations.stream()
                .map(location -> location.y)
                .max(Integer::compareTo)
                .orElse(-1) + 1;
    }

    public boolean addLocation(Location location) {
        return locations.add(location);
    }

    public boolean isTarget(Location target) {
        return targets.contains(target);
    }

    public void putPerson(Person person, Location location) {
        final Location old = persons.put(person, location);
        if (old != null) {
            personLocations.remove(old);
        }
        personLocations.add(location);
    }

    public Set<Location> getLocations() {
        return Collections.unmodifiableSet(locations);
    }

    public Location locationOf(Person person) {
        return persons.get(person);
    }

    public boolean isFree(Location location) {
        return !personLocations.contains(location);
    }

    public boolean has(Location location) {
        return locations.contains(location);
    }

    public void removePerson(Person person) {
        final Location remove = persons.remove(person);
        personLocations.remove(remove);
    }

    public int numberOfPersons() {
        return persons.size();
    }

    public double getCellSize() {
        return cellSize;
    }
}
