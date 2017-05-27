package time.events;

import field.Field;
import field.Fields;
import field.location.Location;
import field.location.Locations;
import main.State;
import person.Person;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author peter-mueller
 */
public class PersonMoveEvent extends BaseEvent {

    private final Person person;
    private final State state;

    public PersonMoveEvent(BigDecimal time, State state, Person person) {
        super(time);
        this.state = state;
        this.person = person;
    }

    @Override
    public List<Event> execute() {
        final Location center = state.field.locationOf(person);
        final Set<Location> moore = Fields.moore(state.field, center);

        final Location bestTarget = moore.stream()
                .filter(state.field::isFree)
                .max(Comparator.comparingDouble(state.use::get))
                .orElse(null);


        final List<Event> newEvents = new ArrayList<>();

        if (bestTarget == null) {
            //TODO how long to wait if no move possible?
            final BigDecimal timeForMove = BigDecimal.valueOf(1 / person.getVelocity());
            final BigDecimal nextMove = state.clock.systemTime().add(timeForMove);
            final PersonMoveEvent event = new PersonMoveEvent(nextMove, state, person);
            newEvents.add(event);
            return newEvents;
        }

        if (state.field.isTarget(bestTarget)) {
            state.field.removePerson(person);
            return newEvents;
        }

        state.field.putPerson(person, bestTarget);
        final double distance = Locations.distance(center, bestTarget);
        final BigDecimal timeForMove = BigDecimal.valueOf(distance / person.getVelocity());
        final BigDecimal nextMove = state.clock.systemTime().add(timeForMove);
        final PersonMoveEvent event = new PersonMoveEvent(nextMove, state, person);
        newEvents.add(event);


        return newEvents;
    }
}
