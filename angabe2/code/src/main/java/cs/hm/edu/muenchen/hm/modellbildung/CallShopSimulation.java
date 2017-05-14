package cs.hm.edu.muenchen.hm.modellbildung;

import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopCalculation;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopLogs;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.time.event.ArrivalEvent;
import cs.hm.edu.muenchen.hm.modellbildung.time.event.Event;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The main class.
 * @author peter-mueller
 */
public class CallShopSimulation {

    private final SimulationState state;

    public CallShopSimulation(SimulationState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null!");
        }
        this.state = state;
    }

    /**
     * Starts the simulation.
     */
    private void run() {
        init();
        while (state.clock.systemTime().compareTo(CallShopConfiguration.DURATION) < 0) {
            final Event event = state.events.nextEvent();
            log(event);
            state.clock.advanceTo(event.getTimeStamp());
            event.execute(state);
        }
    }

    /**
     * Begins the logging for a certain event.
     * @param event
     */
    private void log(Event event) {
        final CallShopLogs logs = state.logs;
        final BigDecimal time = state.clock.systemTime();

        logs.queueSizeNormal.log(time, BigDecimal.valueOf(state.queue.countNormal()));
        logs.queueSizeResident.log(time, BigDecimal.valueOf(state.queue.countResident()));
        logs.queueSizeAll.log(time, BigDecimal.valueOf(state.queue.countAll()));

        calculate(event);
        final CallShopCalculation calculation = state.calculation;

        logs.meanQueueSizeNormal.log(time, calculation.meanQueueSizeNormal.getMean());
        logs.meanQueueSizeResident.log(time, calculation.meanQueueSizeResident.getMean());
        logs.meanQueueSizeAll.log(time, calculation.meanQueueSizeAll.getMean());

        logs.meanPhoneSizeNormal.log(time, calculation.meanPhoneSizeNormal.getMean());
        logs.meanPhoneSizeResident.log(time, calculation.meanPhoneSizeResident.getMean());
        logs.meanPhoneSizeAll.log(time, calculation.meanPhoneSizeAll.getMean());

        logs.meanSystemSizeNormal.log(time, calculation.meanSystemSizeNormal.getMean());
        logs.meanSystemSizeResident.log(time, calculation.meanSystemSizeResident.getMean());
        logs.meanSystemSizeAll.log(time, calculation.meanSystemSizeAll.getMean());
    }

    /**
     * Begins the calculation for a certain event.
     * @param event
     */
    private void calculate(Event event) {
        final BigDecimal queueSizeNormal = BigDecimal.valueOf(state.queue.countNormal());
        final BigDecimal queueSizeResident = BigDecimal.valueOf(state.queue.countResident());
        final BigDecimal queueSizeAll = BigDecimal.valueOf(state.queue.countAll());

        state.calculation.meanQueueSizeNormal.calculate(queueSizeNormal, event.getTimeStamp());
        state.calculation.meanQueueSizeResident.calculate(queueSizeResident, event.getTimeStamp());
        state.calculation.meanQueueSizeAll.calculate(queueSizeAll, event.getTimeStamp());

        final BigDecimal phoneSizeNormal = BigDecimal.valueOf(state.phones.stream().map(Phone::getUser).filter(p -> p != null && !p.isResident()).count());
        final BigDecimal phoneSizeResident = BigDecimal.valueOf(state.phones.stream().map(Phone::getUser).filter(p -> p != null && p.isResident()).count());
        final BigDecimal phoneSizeAll = BigDecimal.valueOf(state.phones.stream().map(Phone::getUser).filter(Objects::nonNull).count());

        state.calculation.meanPhoneSizeNormal.calculate(phoneSizeNormal, event.getTimeStamp());
        state.calculation.meanPhoneSizeResident.calculate(phoneSizeResident, event.getTimeStamp());
        state.calculation.meanPhoneSizeAll.calculate(phoneSizeAll, event.getTimeStamp());

        final BigDecimal systemSizeNormal = queueSizeNormal.add(phoneSizeNormal);
        final BigDecimal systemSizeResident = queueSizeResident.add(phoneSizeResident);
        final BigDecimal systemSizeAll = queueSizeAll.add(phoneSizeAll);

        state.calculation.meanSystemSizeNormal.calculate(systemSizeNormal, event.getTimeStamp());
        state.calculation.meanPhoneSizeResident.calculate(systemSizeResident, event.getTimeStamp());
        state.calculation.meanSystemSizeAll.calculate(systemSizeAll, event.getTimeStamp());
    }

    /**
     * Initial method for the simulation.
     */
    private void init() {
        state.phones.add(new Phone(CallShopConfiguration.CONFIGURATION > 1));
        if (CallShopConfiguration.CONFIGURATION == 3) {
            state.phones.add(new Phone(false));
        }
        Event event = new ArrivalEvent(state.clock.systemTime());
        state.events.add(event);
    }

    public static void main(String[] args) {

        CallShopConfiguration.loadConfiguration(args);
        final Path folder = Paths.get(CallShopConfiguration.OUTPATH);
        CallShopConfiguration.logConfiguration(folder.resolve("starting_configuration.txt"));


        try (final CallShopLogs logs = new CallShopLogs(folder);
             final SimulationState state = new SimulationState(logs)) {

            final CallShopSimulation callShopSimulation = new CallShopSimulation(state);
            callShopSimulation.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




