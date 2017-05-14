package cs.hm.edu.muenchen.hm.modellbildung.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.SimulationState;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public interface Event extends Comparable<Event> {
    /**
     * Executes the event´s logic.
     * @param state
     */
    void execute(SimulationState state);
    BigDecimal getTimeStamp();
}
