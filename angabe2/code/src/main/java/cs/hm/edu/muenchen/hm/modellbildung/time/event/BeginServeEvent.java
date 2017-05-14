package cs.hm.edu.muenchen.hm.modellbildung.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopCalculation;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopLogs;
import cs.hm.edu.muenchen.hm.modellbildung.distribution.Distribution;
import cs.hm.edu.muenchen.hm.modellbildung.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Person;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Phone;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class BeginServeEvent extends BaseEvent {
    private static final BigDecimal DIVIDE = BigDecimal.ONE.divide(BigDecimal.valueOf(CallShopConfiguration.MEAN_ARRIVAL), 32, BigDecimal.ROUND_HALF_EVEN);
    private final Distribution dist = new NegativeExponentialDistribution();
    private final Phone phone;

    public BeginServeEvent(BigDecimal timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        if (phone.isOccupied()) {
            throw new AssertionError("Phone Taken");
        }
        Person person = (phone.isResidentPhone()) ? state.queue.dequeueVip() : state.queue.dequeue();
        person.setBeginTimeStamp(state.clock.systemTime());
        phone.setUser(person);
        makeFinishEvent(state);

        log(state, person);
    }

    /**
     * Adds the Event to the EventList when the customer will be finished with his call.
     * @param state
     */
    private void makeFinishEvent(SimulationState state) {
        final double serveTime = dist.getNextValue(CallShopConfiguration.MEAN_CALL);
        state.logs.serveDelta.log(serveTime);
        final BigDecimal absoluteTime = state.clock.systemTime().add(new BigDecimal(serveTime));
        final FinishServeEvent event = new FinishServeEvent(absoluteTime, phone);
        state.events.add(event);
    }

    private void log(SimulationState state, Person person) {
        final CallShopCalculation calculation = state.calculation;
        final CallShopLogs logs = state.logs;
        final BigDecimal time = state.clock.systemTime();

        meanTime(person, calculation, logs, time);
        little(calculation, logs, time);
    }

    /**
     * Starts the calculation for the mean time for the evaluation of the simulation´s results.
     * @param person
     * @param calculation
     * @param logs
     * @param time
     */
    private void meanTime(Person person, CallShopCalculation calculation, CallShopLogs logs, BigDecimal time) {
        final BigDecimal diff = person.getBeginTime().subtract(person.getArrivalTime());

        calculation.meanQueueTimeAll.calculate(diff);
        logs.meanQueueTimeAll.log(time, calculation.meanQueueTimeAll.getMean());

        if (person.isResident()) {
            calculation.meanQueueTimeResident.calculate(diff);
            logs.meanQueueTimeResident.log(time, calculation.meanQueueTimeResident.getMean());
        } else {
            calculation.meanQueueTimeNormal.calculate(diff);
            logs.meanQueueTimeNormal.log(time, calculation.meanQueueTimeNormal.getMean());
        }
    }

    /**
     * Starts the calculation to check if the simulation passes the requirements of the little´s law.
     * @param calculation
     * @param logs
     * @param time
     */
    private void little(CallShopCalculation calculation, CallShopLogs logs, BigDecimal time) {
        final BigDecimal meanQueueSizeNormal = calculation.meanQueueSizeNormal.getMean();
        final BigDecimal littleNormal = meanQueueSizeNormal.subtract(
                DIVIDE.multiply(calculation.meanQueueTimeNormal.getMean())
        );
        logs.littleQueueNormal.log(time, littleNormal);

        final BigDecimal meanQueueSizeResident = calculation.meanQueueSizeResident.getMean();
        final BigDecimal littleResident = meanQueueSizeResident.subtract(
                DIVIDE.multiply(calculation.meanQueueTimeResident.getMean())
        );
        logs.littleQueueResident.log(time, littleResident);

        final BigDecimal meanQueueSizeAll = calculation.meanQueueSizeAll.getMean();
        final BigDecimal littleAll = meanQueueSizeAll.subtract(
                DIVIDE.multiply(calculation.meanQueueTimeAll.getMean())
        );
        logs.littleQueueAll.log(time, littleAll);
    }
}
