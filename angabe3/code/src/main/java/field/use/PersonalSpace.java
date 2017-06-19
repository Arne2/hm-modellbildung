package field.use;

import field.Field;
import field.Fields;
import field.location.Location;
import field.location.Locations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author peter-mueller
 */
public class PersonalSpace {

    private final Map<Double, Double> cacheP1 = new HashMap<>();
    private final Map<Double, Double> cacheP2 = new HashMap<>();
    private final double cellSize;
    private final Location[] selector;

    public PersonalSpace(double cellSize) {
        this.cellSize = cellSize;
        final int cells = (int)Math.ceil(PERSONAL_SPACE/cellSize);
        selector = Fields.circleSelector(cells);
    }


    private double calculate(double distance) {
        final double uP = 100;
        final double aP = 1;
        final double bP = 2;

        final double p1;
        if (!cacheP1.containsKey(distance)) {
            final double v1 = distance / (PERSONAL_SPACE);
            p1 = uP * Math.exp(4 / (Math.pow(v1, 2) - 1));
            cacheP1.put(distance, p1);
        } else {
            p1 = cacheP1.get(distance);
        }

        final double p2;
        if (!cacheP2.containsKey(distance)) {
            final double v2 = distance / (INTIMATE_SPACE);
            final double pow = Math.pow(v2, 2 * bP);
            p2 = p1 + ((uP / aP) * Math.exp(4 / (pow - 1)));
            cacheP2.put(distance, p2);
        } else {
            p2 = cacheP2.get(distance);
        }


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
        double use = 0;
        final Set<Location> circle = Fields.of(field, target, selector);
        for (Location location : circle) {
            if (location.equals(self)) {continue;}
            if (!field.getPersonLocations().contains(location)) {continue;}

            final double distance = Locations.distance(location, target) * cellSize;
            if (distance >= PERSONAL_SPACE) {continue;}
            use += calculate(distance);
        }
        return -1 * use;
    }

    private static final double PEDESTRIAN_SIZE = 0.20;
    private static final double INTIMATE_SPACE = 0.45 + PEDESTRIAN_SIZE;
    private static final double PERSONAL_SPACE = 1.20 + PEDESTRIAN_SIZE;
}

