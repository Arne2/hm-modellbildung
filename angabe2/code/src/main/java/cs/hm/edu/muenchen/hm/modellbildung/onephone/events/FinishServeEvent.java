package cs.hm.edu.muenchen.hm.modellbildung.onephone.events;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.BaseEvent;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.CallShopCalculation;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.CallShopLogs;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.config.CallShopConfiguration;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class FinishServeEvent extends BaseEvent {
    private static final BigDecimal DIVIDE = BigDecimal.ONE.divide(BigDecimal.valueOf(CallShopConfiguration.MEAN_ARRIVAL), 32, BigDecimal.ROUND_HALF_EVEN);

    private final Phone phone;

    public FinishServeEvent(BigDecimal timeStamp, Phone phone) {
        super(timeStamp);
        this.phone = phone;
    }

    @Override
    public void execute(SimulationState state) {
        Person person = phone.removeUser();

        person.setFinishTimeStamp(state.clock.systemTime());

        if (!state.queue.isEmpty()){
            final BeginServeEvent event = new BeginServeEvent(state.clock.systemTime(), phone);
            state.events.add(event);
        }
        log(state, person);
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
        final BigDecimal diff = person.getFinishTime().subtract(person.getArrivalTime());

        calculation.meanSystemTimeAll.calculate(diff);
        logs.meanSystemTimeAll.log(time, calculation.meanSystemTimeAll.getMean());

        if (person.isResident()) {
            calculation.meanSystemTimeResident.calculate(diff);
            logs.meanSystemTimeResident.log(time, calculation.meanSystemTimeResident.getMean());
        } else {
            calculation.meanSystemTimeNormal.calculate(diff);
            logs.meanSystemTimeNormal.log(time, calculation.meanSystemTimeNormal.getMean());
        }
    }

    /**
     * Starts the calculation to check if the simulation passes the requirements of the little´s law.
     * @param calculation
     * @param logs
     * @param time
     */
    private void little(CallShopCalculation calculation, CallShopLogs logs, BigDecimal time) {
        final BigDecimal meanSystemSizeNormal = calculation.meanSystemSizeNormal.getMean();
        final BigDecimal littleNormal = meanSystemSizeNormal.subtract(
                DIVIDE.multiply(calculation.meanSystemTimeNormal.getMean())
        );
        logs.littleSystemNormal.log(time, littleNormal);

        final BigDecimal meanSystemSizeResident = calculation.meanSystemSizeResident.getMean();
        final BigDecimal littleResident = meanSystemSizeResident.subtract(
                DIVIDE.multiply(calculation.meanSystemTimeResident.getMean())
        );
        logs.littleSystemResident.log(time, littleResident);

        final BigDecimal meanSystemSizeAll = calculation.meanSystemSizeAll.getMean();
        final BigDecimal littleAll = meanSystemSizeAll.subtract(
                DIVIDE.multiply(calculation.meanSystemTimeAll.getMean())
        );
        logs.littleSystemAll.log(time, littleAll);
    }
}
