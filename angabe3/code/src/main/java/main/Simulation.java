package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.use.*;
import field.view.StringView;
import field.use.Dijkstra;
import field.use.EuclidDistance;
import field.use.FastMarching;
import measurement.Measurement;
import outputFile.OutputFile;
import person.Person;
import time.Clock;
import time.EventList;
import time.events.Event;
import time.events.PersonMoveEvent;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author peter-mueller
 */
public class Simulation {
    public final Field field;


    private final Map<Location, Double> use;
    private final PersonalSpace personalSpace = new PersonalSpace();
    private final Clock clock = new Clock();
    private final EventList events = new EventList();
    private final List<Person> persons = new ArrayList<>();
    private final Measurement measurement;
    private final Configuration configuration;
    private final OutputFile outputFile;

    public Simulation(Field field, Configuration configuration, OutputFile outputFile) {
        this.configuration = configuration;
        this.field = field;
        this.outputFile = outputFile;
        measurement = new Measurement(field.getMeasurementPoints(), configuration);

        Map<Person, Location> persLoc = field.getPersons();
        for (Map.Entry<Person, Location> entry : persLoc.entrySet()) {
                spawnPersonEvent(entry.getValue(), entry.getKey());
        }


        if (configuration.getAlgorithm() == Configuration.AlgorithmType.eEuclid) {
            this.use = EuclidDistance.use(field);
        } else if (configuration.getAlgorithm() == Configuration.AlgorithmType.eFastMarching) {
            this.use = FastMarching.use(field);
        } else {
            this.use = Dijkstra.use(field);
        }
        if (outputFile != null) {
            outputFile.setDistances(this.use);
        }
    }

    private Person spawnPersonEvent(Location location, Person person){
        final PersonMoveEvent event = new PersonMoveEvent(clock.systemTime(), this, person);
        this.events.add(event);
        persons.add(person);
        if(outputFile != null) {
            outputFile.addPawnEvent(clock.systemTime(), person.getId(), location.x, location.y);
        }
        return person;
    }

    private void addMeasurementEvent(){

    }

    public Person spawnPerson(Location location) {

        Random rand = new Random(System.nanoTime());
        final double velocity = this.configuration.getVelocity();//rand.nextGaussian() * configuration.getDeviation() + configuration.getVelocity();
        final Person person = new Person(velocity);
        field.putPerson(person, location);
        final PersonMoveEvent event = new PersonMoveEvent(clock.systemTime(), this, person);
        this.events.add(event);
        persons.add(person);
        if (outputFile != null) {
            outputFile.addPawnEvent(clock.systemTime(), person.getId(), location.x, location.y);
        }
        return person;
    }

    public void run(BigDecimal maxSimulationTime) {
        while (events.hasNext() & clock.systemTime().compareTo(maxSimulationTime) < 0) {
            System.out.println(clock.systemTime());
            final Event event = events.nextEvent();
            clock.advanceTo(event.getTime());

            final List<Event> newEvents = event.execute();
            events.addAll(newEvents);
        }
    }

    public Measurement getMeasurement(){
        return measurement;
    }

    public Field getField() {
        return field;
    }

    public Map<Location, Double> getUse() {
        return use;
    }

    public Clock getClock() {
        return clock;
    }

    public EventList getEvents() {
        return events;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public OutputFile getOutputFile() {
        return outputFile;
    }

    public PersonalSpace getPersonalSpace() {
        return personalSpace;
    }
}
