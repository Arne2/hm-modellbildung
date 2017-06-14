package measurement;

import config.Configuration;
import field.Field;
import field.location.Location;
import person.Person;

import java.io.Console;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Set;


/**
 * Created by user on 14.06.17.
 */
public class Measurement {

    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;

    Configuration config;

    public Measurement(Set<Location> measurementPoints, Configuration config){
        this.config = config;
        if (measurementPoints.size() == 0) {
            fromX = 0;
            fromY = 0;
            toY = 0;
            toX = 0;
        }
        else{
            toX =  measurementPoints.stream().max(Comparator.comparingInt((Location loc) ->loc.getX())).get().getX();
            toY =  measurementPoints.stream().max(Comparator.comparingInt((Location loc) -> loc.getY() )).get().getY();
            fromX =  measurementPoints.stream().min(Comparator.comparingInt((Location loc) ->loc.getX())).get().getX();
            fromY =  measurementPoints.stream().min(Comparator.comparingInt((Location loc) -> loc.getY() )).get().getY();

        }
    }

    public void measure(Field field, BigDecimal time){
        BigDecimal sum_v = new BigDecimal(0);
        BigDecimal num_p = new BigDecimal(0);
        for (Person p: field.getPersons().keySet()) {
            if(     field.getPersons().get(p).getX() > toX ||
                    field.getPersons().get(p).getX() < fromX ||
                    field.getPersons().get(p).getY() > toY ||
                    field.getPersons().get(p).getY() > fromY){
                continue;
            }
            sum_v = sum_v.add(p.getMeanVelocity());
            num_p = num_p.add(new BigDecimal(1));
        }
        if(num_p.equals(new BigDecimal(0))) return;
        BigDecimal meanv = sum_v.divide(num_p);


    }


}
