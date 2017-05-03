package cs.hm.edu.muenchen.hm.modellbildung.des.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;

/**
 * @author peter-mueller
 */
public interface Event extends Comparable<Event> {
    void execute(SimulationState state);
    double getTimeStamp();
}
