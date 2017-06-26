package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.use.Dijkstra;
import field.use.EuclidDistance;
import field.use.FastMarching;
import field.use.PersonalSpace;
import measurement.Measurement;
import outputFile.OutputFile;
import outputFile.XYLog;
import outputFile.XYZLog;
import person.Person;
import time.Clock;
import time.EventList;
import time.events.Event;
import time.events.MeasurementEvent;
import time.events.PersonMoveEvent;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peter-mueller
 */
public class Simulation implements AutoCloseable {
    /** Field of the Simulation. */
    public final Field field;

    /** Distance map */
    private final Map<Location, Double> use;

    /** The calculator for the personal space. */
    private final PersonalSpace personalSpace;

    /** The clock to measure the current time of the simulation. */
    private final Clock clock = new Clock();

    /** This List holds all the events to come. */
    private final EventList events = new EventList();

    /** A List of all Persons in the simulation. */
    private final List<Person> persons = new ArrayList<>();

    /** The class to manage the measurement area. */
    private final Measurement measurement;

    /** The Configuration of the simulation. */
    private final Configuration configuration;

    /** The OutputFile for the post visualization. */
    private final OutputFile outputFile;

    public Simulation(Field field, Configuration configuration, OutputFile outputFile) throws Exception {
        this.configuration = configuration;
        this.field = field;
        this.outputFile = outputFile;
        personalSpace = new PersonalSpace(field.getCellSize());

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
            outputFile.setMeasurement(measurement);
        }

        final MeasurementEvent event = new MeasurementEvent(new BigDecimal(0), this);
        this.events.add(event);



    }

    /**
     * Adds a spawn event of a person to the EventList.
     * @param location
     * @param person
     * @return the added Person.
     */
    private Person spawnPersonEvent(Location location, Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person must not be null");
        }
        if (location == null) {
            throw new IllegalArgumentException("Location must not be null");
        }
        final PersonMoveEvent event = new PersonMoveEvent(clock.systemTime(), this, person);
        this.events.add(event);
        persons.add(person);
        if (outputFile != null) {
            outputFile.addPawnEvent(clock.systemTime(), person.getId(), location.x, location.y);
        }
        return person;
    }

    /**
     * Sets a new Person on a given Location.
     * @param location
     * @return the Person.
     */
    public Person spawnPerson(Location location) {

        final double velocity = this.configuration.getVelocity();
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

    /**
     * Runs the entire Simulation.
     * @param maxSimulationTime
     * @throws Exception
     */
    public void run(BigDecimal maxSimulationTime) throws Exception {
        while (events.hasNext() & clock.systemTime().compareTo(maxSimulationTime) < 0) {
            //System.out.println(clock.systemTime());
            final Event event = events.nextEvent();
            clock.advanceTo(event.getTime());

            final List<Event> newEvents = event.execute();
            events.addAll(newEvents);
        }
        close();
    }

    public Measurement getMeasurement() {
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

    @Override
    public void close() throws Exception {
        measurement.close();
    }
}
