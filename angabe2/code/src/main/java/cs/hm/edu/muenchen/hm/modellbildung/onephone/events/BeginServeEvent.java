package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;

import java.util.ArrayList;
import java.util.List;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.serveLog;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.queueLog;

/**
 * @author peter-mueller
 */
public class BeginServeEvent extends BaseEvent {
    private final Distribution dist = new NegativeExponentialDistribution();
    private final Phone phone;

    public BeginServeEvent(double timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        Person person = (phone.isResidentPhone()) ? state.queue().dequeueVip() : state.queue().dequeue();
        phone.setUser(person);

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(person.isResident()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        serveLog.writeLine(list);
        queueLog.writeLine(list);

        final double serveTime = dist.getNextValue(CallShopConfiguration.MEAN_CALL);
        final double absoluteTime = state.clock().systemTime() + serveTime;
        final FinishServeEvent event = new FinishServeEvent(absoluteTime, phone);
        state.events().add(event);
    }
}
