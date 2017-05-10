package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class BeginServeEvent extends BaseEvent {
    private final Distribution dist = new NegativeExponentialDistribution();
    private final Phone phone;

    public BeginServeEvent(BigDecimal timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        if (phone.isOccupied()){
            throw new AssertionError("Phone Taken");
        }
        Person person = (phone.isResidentPhone()) ? state.queue.dequeueVip() : state.queue.dequeue();
        phone.setUser(person);
        makeFinishEvent(state);

        log(state, person);
    }

    private void makeFinishEvent(SimulationState state) {
        final double serveTime = dist.getNextValue(CallShopConfiguration.MEAN_CALL);
        state.serveDistributionLog.log(serveTime);
        final BigDecimal absoluteTime = state.clock.systemTime().add(new BigDecimal(serveTime));
        final FinishServeEvent event = new FinishServeEvent(absoluteTime, phone);
        state.events.add(event);
    }

    private void log(SimulationState state, Person person) {
        state.beginServeLog.log(
                person.getId(),
                person.isResident(),
                getTimeStamp()
        );
    }
}
