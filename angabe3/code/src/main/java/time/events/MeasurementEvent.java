package time.events;

import main.Simulation;
import person.Person;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by user on 14.06.17.
 */
public class MeasurementEvent extends BaseEvent{
    private final Simulation simulation;

    public MeasurementEvent(BigDecimal time, Simulation simulation) {
        super(time);
        this.simulation = simulation;
    }

    @Override
    public List<Event> execute() {
        final List<Event> newEvents = new ArrayList<>();
        System.out.printf("measure at %s\n", simulation.getClock().systemTime());
        simulation.getMeasurement().measure(simulation.field, getTime());
        removeNPersons(30);

        final BigDecimal nextMove = getTime().add(new BigDecimal(1));
        final MeasurementEvent event = new MeasurementEvent(nextMove, simulation);
        newEvents.add(event);
        return newEvents;
    }

    private void removeNPersons(int amount) {
        final Set<Person> collect = simulation.field.getPersons().keySet().stream()
                .limit(amount)
                .collect(Collectors.toSet());
        collect.forEach(simulation.field::removePerson);
    }
}
