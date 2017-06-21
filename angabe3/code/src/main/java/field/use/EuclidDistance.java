package field.use;

import field.Field;
import field.location.Location;

import java.util.*;

/**
 * Created by Dani on 28.05.2017.
 */
public class EuclidDistance {
    private final Field field;
    private final Map<Location, Double> distance = new HashMap<>();
    private final Set<Location> unvisited;
    private final Set<Location> targets;

    private EuclidDistance(final Field field_, Set<Location> targets_) {
        this.field = field_;
        this.targets = targets_;
        unvisited = new HashSet<>(field.getLocations());

        for (Location target: targets ) {
            distance.put(target,0.0);
        }

        this.run();
    }

    /**
     * Calculates the euclid distances for the locations of a field for all targets.
     */
    private void run(){
        while (!unvisited.isEmpty()){

            final Location u =  unvisited.parallelStream().findAny().get();
            Set<Double> distances = new HashSet<>();
            for (Location target: targets) {
                distances.add(getEuclidDistance(target, u));
            }
            Double minDistance = distances.stream().max(Double::compareTo).get();
            distance.put(u, minDistance);
            unvisited.remove(u);
        }
    }

    /**
     * Returns the euclid distance of to locations.
     * @param target
     * @param source
     * @return
     */
    private double getEuclidDistance(Location target, Location source){

        return -Math.sqrt(Math.pow(target.x - source.x, 2) + Math.pow(target.y - source.y, 2))*field.getCellSize();
    }

    /**
     * Returns a map of locations with their resulting distances.
     * @param field
     * @return resulting map
     */
    public static Map<Location, Double> use(Field field){
        final EuclidDistance eudistance = new EuclidDistance(field, field.getTargets());
        return eudistance.distance;
    }
}
