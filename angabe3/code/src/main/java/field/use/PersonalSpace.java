package field.use;

import field.Field;
import field.location.Location;
import field.location.Locations;
import person.Person;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author peter-mueller
 */
public class PersonalSpace {
    private Settings settings;

    public PersonalSpace(Settings settings) {
        this.settings = settings;
    }

    private boolean isInRange(double distance) {
        return distance < settings.personalSpace + settings.pedestrianSize;
    }

    private double calculate(double distance) {
        final double uP = 5.0;
        final double aP = 1.0;
        final double bP = 1.0;

        final double v1 = distance / settings.pedestrianSize + settings.personalSpace;
        final double p1 = uP * Math.exp(4 / Math.pow(v1, 2) - 1);

        final double v2 = distance / settings.pedestrianSize + settings.intimateSpace;
        final double p2 = p1 + uP / aP * Math.exp(4 / Math.pow(v2, 2 * bP) - 1);

        if (distance < settings.pedestrianSize) {
            return Double.POSITIVE_INFINITY;
        } else if (distance < settings.pedestrianSize + settings.intimateSpace) {
            return p2;
        } else if (distance < settings.pedestrianSize + settings.personalSpace) {
            return p1;
        }
        throw new AssertionError("Should have been filtered out!");
    }

    public double use(Field field, Location center) {
        final Map<Person, Location> persons = field.getPersons();
        final Set<Location> locations = field.getLocations();

        return  -1 * locations.stream()
                .filter(l -> {
                    final double distance = Locations.distance(l, center) * field.getCellSize();
                    return isInRange(distance);
                })
                .filter(l -> !l.equals(center))
                .filter(persons::containsValue)
                .mapToDouble(l -> calculate(Locations.distance(l, center) * field.getCellSize()))
                .sum();
    }

    public static class Settings {
        private final double personalSpace;
        private final double intimateSpace;
        private final double pedestrianSize;

        public Settings(double personalSpace, double intimateSpace, double pedestrianSize, double cellWidth) {
            this.personalSpace = personalSpace;
            this.intimateSpace = intimateSpace;
            this.pedestrianSize = pedestrianSize;
        }

        public Settings() {
            this.pedestrianSize = 20;
            this.intimateSpace = 45;
            this.personalSpace = 120;
        }
    }
}

