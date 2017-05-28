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
    private final Location target;

    private EuclidDistance(final Field field_, Location target_) {
        this.field = field_;
        this.target = target_;
        unvisited = new HashSet<>(field.getLocations());
        distance.put(target,0.0);
        this.run();
    }

    private void run(){
        while (!unvisited.isEmpty()){
            final Location u =  unvisited.parallelStream().findAny().get();
            distance.put(u, getEuclidDistance(target, u));
            unvisited.remove(u);
        }
    }

    private double getEuclidDistance(Location target, Location source){

        return -Math.sqrt(Math.pow(target.x - source.x, 2) + Math.pow(target.y - source.y, 2));
    }

    public static Map<Location, Double> use(Field field){
        final EuclidDistance eudistance = new EuclidDistance(field, field.getTarget());
        return eudistance.distance;
    }


}
