package time.events;

import main.Simulation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

        simulation.getMeasurement().measure(simulation.field, getTime());
        final BigDecimal nextMove = getTime().add(new BigDecimal(0.5));
        final MeasurementEvent event = new MeasurementEvent(nextMove, simulation);
        newEvents.add(event);
        return newEvents;
    }
}
