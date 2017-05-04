package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;

import java.util.ArrayList;
import java.util.List;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.finishLog;

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
        Person person = state.phone().removeUser();

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        finishLog.writeLine(list);

        if (!state.queue().isEmpty()){
            final BeginServeEvent event = new BeginServeEvent(state.clock().systemTime());
            state.events().add(event);
        }
    }
}
