package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;

import java.util.ArrayList;
import java.util.List;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.arrivalLog;


/**
 * @author peter-mueller
 */
public class ArrivalEvent extends BaseEvent {
    private final Distribution dist = new NegativeExponentialDistribution();

    public ArrivalEvent(double timeStamp) {
        super(timeStamp);
    }

    @Override
    public void execute(SimulationState state) {
        if (!state.phone().isOccupied()) {
            final BeginServeEvent event = new BeginServeEvent(state.clock().systemTime());
            state.events().add(event);
        }

        final Person person = new Person(true);
        state.queue().enqueue(person);

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        arrivalLog.writeLine(list);

        final double nextArrivalIn = dist.getNextValue(CallShopConfiguration.MEAN_ARRIVAL);
        final double absoluteTime = state.clock().systemTime() + nextArrivalIn;
        final ArrivalEvent event = new ArrivalEvent(absoluteTime);
        state.events().add(event);
    }
}
