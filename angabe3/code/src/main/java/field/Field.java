package field;

import field.location.Location;
import person.Person;

import java.util.*;

public class Field {
    private final Set<Location> locations = new HashSet<>();

    public Map<Person, Location> getPersons() {
        return Collections.unmodifiableMap(persons);
    }

    public Location getTarget() {
        return target;
    }

    private final Map<Person, Location> persons = new HashMap<>();

    public void setTarget(Location target) {
        addLocation(target);
        this.target = target;
    }

    private Location target = Location.of(0, 0);

    public Field() {
        this(0, 0);
    }

    public Field(int width, int height) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                locations.add(Location.of(x, y));
            }
        }
    }

    public boolean addLocation(Location location) {
        return locations.add(location);
    }

    public boolean isTarget(Location target) {
        return target.equals(this.target);
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
}
