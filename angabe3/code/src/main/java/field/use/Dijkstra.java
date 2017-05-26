package field.use;

import field.Field;
import field.Fields;
import field.cell.Cell;
import field.location.Location;

import java.util.*;
import java.util.stream.Collectors;

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

        unvisited = field.asList().parallelStream()
                .filter(c -> !c.isOccupied())
                .map(Cell::location)
                .collect(Collectors.toSet());
        distance.put(start, 0.0);
    }

    private void run() {
        while (!unvisited.isEmpty()) {
            final Location u = unvisited.parallelStream()
                    .min(Comparator.comparingDouble(key -> distance.getOrDefault(key, Double.POSITIVE_INFINITY)))
                    .orElseThrow(() -> new AssertionError("unvisited must have not been empty!"));
            unvisited.remove(u);

            final Set<Cell> moore = Fields.moore(field, u);
            moore.stream()
                    .filter(c -> !c.isOccupied())
                    .map(Cell::location)
                    .filter(unvisited::contains)
                    .forEach(v -> {
                        final double alt = distance.get(u) + 1;
                        if (distance.get(v) == null || alt < distance.get(v)) {
                            distance.put(v, alt);
                            prev.put(v, u);
                        }
                    });
        }
    }

    public static Map<Location, Double> use(Field field, Location target) {
        final Dijkstra dijkstra = new Dijkstra(field, target);
        dijkstra.run();
        return dijkstra.distance;
    }
}
