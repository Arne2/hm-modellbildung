package time.events;

import field.Fields;
import field.cell.Cell;
import field.location.Location;
import main.State;
import person.Person;

import java.math.BigDecimal;
import java.util.Arrays;
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
        final Set<Cell> moore = Fields.moore(getState().field, center);

        final Location bestTarget = moore.stream()
                .filter(c -> !c.isOccupied())
                .map(Cell::location)
                .min(Comparator.comparingDouble(getState().use::get))
                .orElse(null);

        person.moveTo(bestTarget);

    }
}
