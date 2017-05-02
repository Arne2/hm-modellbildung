package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;
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

    public BeginServeEvent(double timeStamp) {
        super(timeStamp);
    }

    @Override
    public void execute(SimulationState state) {
        if (state.phone().isOccupied()) {
            throw new AssertionError("Phone must not be occupied on begin serve!");
        }
        final Person person = state.queue().dequeue();
        state.phone().setUser(person);

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        serveLog.writeLine(list);
        queueLog.writeLine(list);

        final double serveTime = dist.getNextValue(CallShopConfiguration.MEAN_CALL);
        final double absoluteTime = state.clock().systemTime() + serveTime;
        final FinishServeEvent event = new FinishServeEvent(absoluteTime);
        state.events().add(event);
    }
}
