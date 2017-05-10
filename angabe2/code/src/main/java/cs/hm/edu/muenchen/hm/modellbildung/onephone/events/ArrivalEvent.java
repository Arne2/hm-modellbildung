package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;

import java.math.BigDecimal;
import java.util.Random;

import static cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration.VIP_PERCENTAGE;


/**
 * @author peter-mueller
 */
public class ArrivalEvent extends BaseEvent {
    private final Distribution dist = new NegativeExponentialDistribution();
    private final Random random = new Random();

    public ArrivalEvent(BigDecimal timeStamp) {
        super(timeStamp);
    }

    @Override
    public void execute(SimulationState state) {
        findFreePhones(state);

        final Person person = new Person(random.nextInt(100) < VIP_PERCENTAGE);
        state.queue.enqueue(person);
        makeNextArrival(state);

        log(state, person);
    }

    private void makeNextArrival(SimulationState state) {
        final double nextArrivalIn = dist.getNextValue(CallShopConfiguration.MEAN_ARRIVAL);
        state.arrivalDistributionLog.log(nextArrivalIn);
        final BigDecimal absoluteTime = state.clock.systemTime().add(new BigDecimal(nextArrivalIn));
        final ArrivalEvent event = new ArrivalEvent(absoluteTime);
        state.events.add(event);
    }

    private void log(SimulationState state, Person person) {
        state.arrivalLog.log(
                person.getId(),
                person.isResident(),
                state.clock.systemTime()
        );
    }

    private void findFreePhones(SimulationState state) {
        for (Phone next : state.phones) {
            if (!next.isOccupied()) {
                final BeginServeEvent event = new BeginServeEvent(state.clock.systemTime(), next);
                state.events.add(event);
                break;
            }
        }
    }
}
