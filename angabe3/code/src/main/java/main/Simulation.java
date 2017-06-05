package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.use.Dijkstra;
import field.use.EuclidDistance;
import field.use.FastMarching;
import field.use.FastMarching2;
import field.view.StringView;
import outputFile.OutputFile;
import person.Person;
import time.Clock;
import time.EventList;
import time.events.Event;
import time.events.PersonMoveEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author peter-mueller
 */
public class Simulation {
    public final Field field;


    private final Map<Location, Double> use;
    private final Clock clock = new Clock();
    private final EventList events = new EventList();
    private final List<Person> persons = new ArrayList<Person>();
    private final Configuration configuration;
    private final OutputFile outputFile;

    public Simulation(Field field, Configuration configuration, OutputFile outputFile) {
        this.configuration = configuration;
        this.field = field;
        this.outputFile = outputFile;
        if(configuration.getAlgorithm() == Configuration.AlgorithmType.eEuclid){
            this.use = EuclidDistance.use(field);
        }
        else if(configuration.getAlgorithm() == Configuration.AlgorithmType.eFastMarching){
            this.use = FastMarching.use(field);
        }
        else{
            this.use = Dijkstra.use(field);
        }

        outputFile.setDistances(this.use);
    }

    public Person spawnPerson(Location location) {

        Random rand = new Random(System.nanoTime());
        final double velocity = this.configuration.getVelocity();//rand.nextGaussian() * configuration.getDeviation() + configuration.getVelocity();
        final Person person = new Person(velocity);
        field.putPerson(person, location);
        final PersonMoveEvent event = new PersonMoveEvent(clock.systemTime(), this, person);
        this.events.add(event);
        persons.add(person);
        if(outputFile != null) {
            outputFile.addPawnEvent(clock.systemTime(), person.getId(), location.x, location.y);
        }
        return person;
    }
    public void run(BigDecimal maxSimulationTime) {
        while (events.hasNext() & clock.systemTime().compareTo(maxSimulationTime) < 0) {
            System.out.println(clock.systemTime());
            System.out.println(StringView.personMap(this.field));
            for (Person p: persons) {
                System.out.println("v given: " + p.getVelocity() + "  v: " + p.getMeanVelocity());

            }
            final Event event = events.nextEvent();
            clock.advanceTo(event.getTime());

            /* JUST TO VISUALIZE CHANGES
            long timeToWait = (event.getTime().subtract(clock.systemTime())
                    .multiply(new BigDecimal(1000)).longValue());

            System.out.println(timeToWait);
            try {
                TimeUnit.MILLISECONDS.sleep(timeToWait);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }*/
            final List<Event> newEvents = event.execute();
            events.addAll(newEvents);
        }
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
}
