package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.use.Dijkstra;
import field.use.EuclidDistance;
import person.Person;
import time.Clock;
import time.EventList;
import time.events.Event;
import time.events.PersonMoveEvent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author peter-mueller
 */
public class Simulation {
    public final Field field;


    public final Map<Location, Double> use;
    public final Clock clock = new Clock();
    public final EventList events = new EventList();
    public final Configuration configuration;

    public Simulation(Field field, Configuration configuration) {
        this.configuration = configuration;
        this.field = field;
        if(configuration.getAlgorithm() == Configuration.AlgorithmType.eEuclid){
            this.use = EuclidDistance.use(field);
        }
        else if(configuration.getAlgorithm() == Configuration.AlgorithmType.eFastMarching){
            //TODO: fast marching Algorithm
            //here: Euclid to be replaced by fast marching
            this.use = EuclidDistance.use(field);
        }
        else{
            this.use = Dijkstra.use(field);
        }

    }

    public Person spawnPerson(Location location) {
        final double velocity = configuration.getVelocity();
        final Person person = new Person(velocity);
        field.putPerson(person, location);
        final PersonMoveEvent event = new PersonMoveEvent(clock.systemTime(), this, person);
        this.events.add(event);
        return person;
    }
    public void run(BigDecimal maxSimulationTime) {
        while (events.hasNext() & clock.systemTime().compareTo(maxSimulationTime) < 0) {
            final Event event = events.nextEvent();
            clock.advanceTo(event.getTime());
            final List<Event> newEvents = event.execute();
            events.addAll(newEvents);
        }
    }
}
