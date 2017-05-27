package main;

import field.Field;
import field.location.Location;
import field.use.Dijkstra;
import time.Clock;
import time.EventList;

import java.util.Map;

/**
 * @author peter-mueller
 */
public class State {
    public final Field field;


    public final Map<Location, Double> use;
    public final Clock clock = new Clock();
    public final EventList events = new EventList();

    public State(Field field) {
        this.field = field;
        this.use = Dijkstra.use(field);
    }
}
