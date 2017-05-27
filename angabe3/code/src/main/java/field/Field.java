package field;

import field.location.Location;
import person.Person;

import java.util.*;

public class Field {
    private final Set<Location> locations = new HashSet<>();
    private final Map<Person, Location> persons = new HashMap<>();

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

    public boolean add(Location location) {
        return locations.add(location);
    }

    public Set<Location> locations() {
        return Collections.unmodifiableSet(locations);
    }

    public boolean isFree(Location location) {
        return !persons.containsValue(location);
    }

    public boolean has(Location location) {
        return locations.contains(location);
    }
}
