package field.use;

import field.Field;
import field.Fields;
import field.location.Location;
import field.location.Locations;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides a simple implementation for the dijkstra algorithm to make use
 * values for a field.
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
                        final double d = Locations.distance(u, v) * field.getCellSize();
                        final double alt = distance.get(u) - d;
                        if (distance.get(v) == null || alt > distance.get(v)) {
                            distance.put(v, alt);
                            prev.put(v, u);
                        }
                    });
        }
    }

    public static Map<Location, Double> use(Field field) {
        Set<Map<Location, Double>> distances = new HashSet<>();

        for (Location target: field.getTargets()) {
            final Dijkstra dijkstra = new Dijkstra(field, target);
            dijkstra.run();
            distances.add(dijkstra.distance);
        }

        Map<Location, Double> distance = new HashMap<>();
        for (Location location:field.getLocations() ) {
            Double maxValue = 0.;
            Boolean filled= false;
            for (Map<Location, Double> distmap:distances) {
                if(!filled){
                    filled = true;
                    maxValue = distmap.get(location);
                    continue;
                }
                else{
                    maxValue = Math.max(maxValue, distmap.get(location));
                }

            }
            distance.put(location, maxValue);
        }
        return distance;
    }
}
