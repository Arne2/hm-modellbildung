package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.time.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.events.BeginServeEvent;

/**
 * @author peter-mueller
 */
public class FinishServeEvent extends BaseEvent {
    public FinishServeEvent(double timeStamp) {
        super(timeStamp);
    }

    @Override
    public void execute(SimulationState state) {
        if (!state.phone().isOccupied()) {
            throw new AssertionError("Phone must have been in use!");
        }
        state.phone().removeUser();

        if (!state.queue().isEmpty()){
            final BeginServeEvent event = new BeginServeEvent(state.clock().systemTime());
            state.events().add(event);
        }
    }
}
