package field.use;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import field.Field;
import field.location.Location;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Comparator.comparingDouble;

/**
 * Created by dima on 30.05.17.
 */
public class FastMarching {
    private final Field field;
    private final Location target;
    private final Map<Location, Double> distance = new HashMap<>();
    private final Set<Location> unvisited;
    private final Map<Location, Double> considered = new HashMap<>();

    private FastMarching(final Field field_, Location target_) {
        this.field = field_;
        this.target = target_;
        this.unvisited = new HashSet<>(field.getLocations());

    }


    private double getTopDistance(Location location){
        Location newLoc = Location.of(location.x, location.y + 1);
        if(distance.containsKey(newLoc)){
            return distance.get(newLoc);
        }
        return Double.MAX_VALUE;
    }

    private double getBottomDistance(Location location){
        Location newLoc = Location.of(location.x, location.y - 1);
        if(distance.containsKey(newLoc)){
            return distance.get(newLoc);
        }
        return Double.MAX_VALUE;
    }

    private double getLeftDistance(Location location){
        Location newLoc = Location.of(location.x - 1, location.y);
        if(distance.containsKey(newLoc)){
            return distance.get(newLoc);
        }
        return Double.MAX_VALUE;
    }

    private double getRightDistance(Location location){
        Location newLoc = Location.of(location.x +1, location.y);
        if(distance.containsKey(newLoc)){
            return distance.get(newLoc);
        }
        return Double.MAX_VALUE;
    }


    private double calcUTilde(Location location){
        double UH = Math.min(getTopDistance(location), getBottomDistance(location));
        double UV = Math.min(getLeftDistance(location), getRightDistance(location));

        if( Math.abs(UH - UV) < 1 ){
            BigDecimal BD_UH = new BigDecimal(UH);
            BigDecimal BD_UV = new BigDecimal(UV);
            BigDecimal BD_firstFraction = BD_UH.add(BD_UV).divide(new BigDecimal(2));
            BigDecimal BD_firstSumUnderRoot = (BD_UH.add(BD_UV)).pow(2);
            BigDecimal BD_secondSumUnderRoot = (BD_UH.pow(2).add(BD_UV.pow(2))
                    .subtract(new BigDecimal(1))).multiply(new BigDecimal(2));
            BigDecimal BD_root = sqrt(BD_firstSumUnderRoot.subtract(BD_secondSumUnderRoot));
            BigDecimal BD_value = BD_firstFraction.add(BD_root.multiply(new BigDecimal(0.5)));

            double firstFraction = (UH + UV)/2;
            double firstSumUnderRoot = Math.pow(UH + UV, 2);
            double secondSumUnderRoot = 2 * (UH*UH + UV*UV - 1);
            double root = Math.sqrt(firstSumUnderRoot - secondSumUnderRoot);
            double value = firstFraction + 0.5 * root;
            return BD_value.doubleValue();
            //return firstFraction + 0.5 * root;
        }
        else{
            return  Math.min(UH, UV) + 1;
        }
    }

    public static BigDecimal sqrt(BigDecimal value) {
        BigDecimal x = new BigDecimal(Math.sqrt(value.doubleValue()));
        return x.add(new BigDecimal(value.subtract(x.multiply(x)).doubleValue() / (x.doubleValue() * 2.0)));
    }

    private Set<Location> neighbours(Location location){
        Set<Location> locations = new HashSet<>();
        for(int i = -1; i<2; i++){
            for(int j = -1; j<2; j++){
                if ((i != 0) && (j != 0)){
                    Location loc = Location.of(location.x + i, location.y + j);
                    if(unvisited.contains(loc)) {
                        locations.add(loc);
                    }
                }
            }
        }
        return locations;
    }

    private void run(){


        //Assign every node Xi the value of Ui =  +INFINITY and label them as far;
        for (Location loc: field.getLocations()) {
            distance.put(loc, Double.MAX_VALUE);
        }
        // for all nodes Xi in (Target) set Ui = 0 and label Xi as accepted.
        this.distance.put(target, 0.0);
        unvisited.remove(target);

        //For every far node Xi, use the Eikonal update formula to calculate a new value for Utilde
        for (Location farLocation: unvisited ) {
            double uTilde = calcUTilde(farLocation);
            if(distance.get(farLocation) > uTilde){
                distance.put(farLocation, uTilde);
                considered.put(farLocation, uTilde);
            }
        }

        while (!considered.isEmpty()){
            Location consideredLocation = Collections
                    .min(considered.entrySet(), comparingDouble(Map.Entry::getValue)).getKey();
            unvisited.remove(consideredLocation);
            for (Location neighbourLocation: neighbours(consideredLocation)) {
                double uTilde = calcUTilde(neighbourLocation);
                if(distance.get(neighbourLocation) > uTilde){
                    distance.put(neighbourLocation, uTilde);
                }
                if(!considered.containsKey(neighbourLocation)){
                    considered.put(neighbourLocation, uTilde);
                }
            }

            unvisited.remove(consideredLocation);
            considered.remove(consideredLocation);
        }

        for (Map.Entry<Location, Double> dist: distance.entrySet() ) {
            dist.setValue( - dist.getValue());
            distance.put(dist.getKey(), dist.getValue());
        }

        if(unvisited.isEmpty() != true){

            System.out.println("unvisited nodes detected");
            for (Location loc:unvisited) {
                System.out.println(loc.toString());
            }
        }



    }


    public static Map<Location, Double> use(Field field) {
        final FastMarching fastMarching = new FastMarching(field, field.getTarget());
        fastMarching.run();
        return fastMarching.distance;
    }
}
