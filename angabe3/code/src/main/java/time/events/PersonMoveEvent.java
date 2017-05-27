package time.events;

import field.Fields;
import field.location.Location;
import main.State;
import person.Person;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Set;

/**
 * @author peter-mueller
 */
public class PersonMoveEvent extends BaseEvent {

    private final Person person;

    public PersonMoveEvent(BigDecimal time, State state, Person person) {
        super(time, state);
        this.person = person;
    }

    @Override
    public void execute() {
        final Location center = person.getLocation();
        final Set<Location> moore = Fields.moore(getState().field, center);

        final Location bestTarget = moore.stream()
                .filter(getState().field::isFree)
                .min(Comparator.comparingDouble(getState().use::get))
                .orElse(null);

        person.moveTo(bestTarget);

    }
}
