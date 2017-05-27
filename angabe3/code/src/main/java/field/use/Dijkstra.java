package field.use;

import field.Field;
import field.Fields;
import field.location.Location;

import java.util.*;

/**
 * @author peter-mueller
 */
public class Dijkstra {
    private final Field field;
    private final Map<Location, Double> distance = new HashMap<>();
    private final Map<Location, Location> prev = new HashMap<>();
    private final Set<Location> unvisited;

    private Dijkstra(final Field field, Location start) {
        this.field = field;

        unvisited = new HashSet<>(field.getLocations());
        distance.put(start, 0.0);
    }

    private void run() {
        while (!unvisited.isEmpty()) {
            final Location u = unvisited.parallelStream()
                    .max(Comparator.comparingDouble(key -> distance.getOrDefault(key, Double.NEGATIVE_INFINITY)))
                    .orElseThrow(() -> new AssertionError("unvisited must have not been empty!"));
            unvisited.remove(u);

            final Set<Location> moore = Fields.moore(field, u);
            moore.stream()
                    .filter(unvisited::contains)
                    .forEach(v -> {
                        final double alt = distance.get(u) - 1;
                        if (distance.get(v) == null || alt > distance.get(v)) {
                            distance.put(v, alt);
                            prev.put(v, u);
                        }
                    });
        }
    }

    public static Map<Location, Double> use(Field field) {
        final Dijkstra dijkstra = new Dijkstra(field, field.getTarget());
        dijkstra.run();
        return dijkstra.distance;
    }
}
