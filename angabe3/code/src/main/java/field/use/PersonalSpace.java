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

    private boolean isInRange(double distance) {
        return distance < PERSONAL_SPACE;
    }

    private double calculate(double distance) {
        // TODO ???
        final double uP = 60;
        final double aP = 1;
        final double bP = 1;

        final double v1 = distance / (PERSONAL_SPACE);
        final double p1 = uP * Math.exp(4 / (Math.pow(v1, 2) - 1));

        final double v2 = distance / (INTIMATE_SPACE);
        final double p2 = p1 + ((uP / aP) * Math.exp(4 / (Math.pow(v2, 2 * bP) - 1)));

        if (distance < PEDESTRIAN_SIZE) {
            return Double.POSITIVE_INFINITY;
        } else if (distance < INTIMATE_SPACE) {
            return p2;
        } else if (distance < PERSONAL_SPACE) {
            return p1;
        }
        throw new AssertionError("Should have been filtered out!");
    }

    public double use(Field field, Location target, Location self) {
        final Map<Person, Location> persons = field.getPersons();

        return -1 * persons.values().stream()
                .filter(l -> !l.equals(self))
                .filter(l -> {
                    final double distance = Locations.distance(l, target) * field.getCellSize();
                    return isInRange(distance);
                })
                .mapToDouble(l -> calculate(Locations.distance(l, target) * field.getCellSize()))
                .sum();
    }

    private static final double PEDESTRIAN_SIZE = 0.20;
    private static final double INTIMATE_SPACE = 0.45 + PEDESTRIAN_SIZE;
    private static final double PERSONAL_SPACE = 1.20 + PEDESTRIAN_SIZE;

}

