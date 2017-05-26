package main;

import field.Field;
import field.location.Location;
import field.use.Dijkstra;

import java.util.Map;

/**
 * @author peter-mueller
 */
public class State {
    public final Field field;
    public final Map<Location, Double> use;

    public State(Field field) {
        this.field = field;
        this.use = Dijkstra.use(field, new Location(0, 0));
    }
}
