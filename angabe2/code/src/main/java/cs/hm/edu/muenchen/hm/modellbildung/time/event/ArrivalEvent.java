package cs.hm.edu.muenchen.hm.modellbildung.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Person;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.distribution.NegativeExponentialDistribution;

import java.math.BigDecimal;
import java.util.Random;


/**
 * Represents an event when a customer arrives.
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

        final Person person = new Person(random.nextInt(100) < CallShopConfiguration.VIP_PERCENTAGE);
        person.setArrivalTimeStamp(state.clock.systemTime());
        state.queue.enqueue(person);
        makeNextArrival(state);
    }

    /**
     * Loads the next Event for a customer to come into the EventList.
     * @param state
     */
    private void makeNextArrival(SimulationState state) {
        final double nextArrivalIn = dist.getNextValue(CallShopConfiguration.MEAN_ARRIVAL);
        state.logs.arrivalDelta.log(nextArrivalIn);
        final BigDecimal absoluteTime = state.clock.systemTime().add(new BigDecimal(nextArrivalIn));
        final ArrivalEvent event = new ArrivalEvent(absoluteTime);
        state.events.add(event);
    }

    /**
     * Tries to find a phone that´s currently not used. If there is at least one, a BeginEvent will be added to the EventList.
     * @param state
     */
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
