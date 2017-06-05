package time.events;

import field.Fields;
import field.location.Location;
import field.location.Locations;
import main.Simulation;
import outputFile.OutputFile;
import person.Person;

import java.math.BigDecimal;
import java.util.*;

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

        final Location center = simulation.field.locationOf(person);
        if (simulation.field.isTarget(center)) {
            simulation.field.removePerson(person);
            return newEvents;
        }

        final Set<Location> moore = Fields.moore(simulation.field, center);
        final Location bestTarget = moore.stream()
                .filter(simulation.field::isFree)
                .max(Comparator.comparingDouble(simulation.getUse()::get))
                .orElse(null);



        if (bestTarget == null) {
            //TODO how long to wait if no move possible?
            final BigDecimal timeForMove = BigDecimal.valueOf(0.05);
            final BigDecimal nextMove = getTime().add(timeForMove);
            final PersonMoveEvent event = new PersonMoveEvent(nextMove, simulation, person);
            newEvents.add(event);
            return newEvents;
        }


        simulation.field.putPerson(person, bestTarget);
        final double distance = Locations.distance(center, bestTarget) * simulation.getConfiguration().getCellSize();
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
