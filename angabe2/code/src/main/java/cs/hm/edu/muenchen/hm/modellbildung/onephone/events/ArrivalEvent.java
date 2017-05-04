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
import java.util.Random;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.VIP_PERCENTAGE;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.arrivalLog;
import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.queueLog;


/**
 * @author peter-mueller
 */
public class ArrivalEvent extends BaseEvent {
    private final Distribution dist = new NegativeExponentialDistribution();
    private final Random random = new Random();

    public ArrivalEvent(double timeStamp) {
        super(timeStamp);
    }

    @Override
    public void execute(SimulationState state) {
        for (Phone next : state.phones()){
            if (!next.isOccupied()){
                final BeginServeEvent event = new BeginServeEvent(state.clock().systemTime(), next);
                state.events().add(event);
                break;
            }
        }

        final Person person = new Person(random.nextInt(100) < VIP_PERCENTAGE);
        state.queue().enqueue(person);

        List<String> list = new ArrayList();
        list.add(person.getId()+"");
        list.add(person.isResident()+"");
        list.add(getTimeStamp()+"");
        list.add(state.queue().count()+"");
        arrivalLog.writeLine(list);
        queueLog.writeLine(list);

        final double nextArrivalIn = dist.getNextValue(CallShopConfiguration.MEAN_ARRIVAL);
        final double absoluteTime = state.clock().systemTime() + nextArrivalIn;
        final ArrivalEvent event = new ArrivalEvent(absoluteTime);
        state.events().add(event);
    }
}
