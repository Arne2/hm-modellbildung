package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class FinishServeEvent extends BaseEvent {
    private final Phone phone;

    public FinishServeEvent(BigDecimal timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        Person person = phone.removeUser();

        if (!state.queue.isEmpty()){
            final BeginServeEvent event = new BeginServeEvent(state.clock.systemTime(), phone);
            state.events.add(event);
        }

        log(state, person);
    }

    private void log(SimulationState state, Person person) {
        state.finishServeLog.log(
                person.getId(),
                person.isResident(),
                state.clock.systemTime()
        );
    }
}
