package time.events;

import field.Fields;
import field.location.Location;
import field.location.Locations;
import field.use.PersonalSpace;
import main.Simulation;
import outputFile.OutputFile;
import person.Person;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author peter-mueller
 */
public class PersonMoveEvent extends BaseEvent {

    private final Person person;
    private final Simulation simulation;


    public PersonMoveEvent(BigDecimal time, Simulation simulation, Person person) {
        super(time);
        this.simulation = simulation;
        this.person = person;
    }

    @Override
    public List<Event> execute() {
        final List<Event> newEvents = new ArrayList<>();

        final Location locationOfPerson = simulation.field.locationOf(person);
        if (simulation.field.isTarget(locationOfPerson)) {
            simulation.field.removePerson(person);
            return newEvents;
        }

        final Set<Location> range = Fields.moore(simulation.field, locationOfPerson).parallelStream()
                .filter(simulation.field::isFree)
                .collect(Collectors.toSet());
        range.add(locationOfPerson);

        final Location bestTarget = range.parallelStream()
                .max(Comparator.comparingDouble((Location target) -> {
                    final double use = simulation.getUse().get(target);
                    final double personPotential = simulation.getPersonalSpace().use(simulation.field, target, locationOfPerson);
                    return use + personPotential;
                }).thenComparingDouble( target ->  -Locations.distance(target, locationOfPerson)))
                .orElseThrow(() -> new AssertionError("cannot happen!"));

        if (bestTarget.equals(locationOfPerson)) {
            final BigDecimal timeForWait = BigDecimal.valueOf(simulation.field.getCellSize() / person.getVelocity());
            final BigDecimal nextMove = getTime().add(timeForWait);
            person.step(new BigDecimal(0), timeForWait);
            final PersonMoveEvent event = new PersonMoveEvent(nextMove, simulation, person);
            newEvents.add(event);
            return newEvents;
        }

        simulation.field.putPerson(person, bestTarget);
        final double distance = Locations.distance(locationOfPerson, bestTarget) * simulation.getConfiguration().getCellSize();
        final BigDecimal timeForMove = BigDecimal.valueOf(distance / person.getVelocity());
        final BigDecimal nextMove = getTime().add(timeForMove);
        final PersonMoveEvent event = new PersonMoveEvent(nextMove, simulation, person);
        person.step(new BigDecimal(distance), timeForMove);
        if (simulation.getOutputFile() != null) {
            simulation.getOutputFile().addMoveEvent(simulation.getClock().systemTime(), person.getId(), bestTarget.x, bestTarget.y);
        }
        newEvents.add(event);


        return newEvents;
    }
}
