package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;

import java.util.ArrayList;
import java.util.List;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.finishLog;

/**
 * @author peter-mueller
 */
public class FinishServeEvent extends BaseEvent {
    private final Phone phone;

    public FinishServeEvent(double timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        Person person = phone.removeUser();

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(person.isResident()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        finishLog.writeLine(list);

        if (!state.queue().isEmpty()){
            final BeginServeEvent event = new BeginServeEvent(state.clock().systemTime(), phone);
            state.events().add(event);
        }
    }
}
