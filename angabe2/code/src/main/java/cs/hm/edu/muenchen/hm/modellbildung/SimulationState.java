package cs.hm.edu.muenchen.hm.modellbildung;

import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopCalculation;
import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopLogs;
import cs.hm.edu.muenchen.hm.modellbildung.domain.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.time.Clock;
import cs.hm.edu.muenchen.hm.modellbildung.time.event.EventList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the current state of the simulation.
 * It contains information about the clock, the queue, the phones, the upcoming events and the logs.
 * @author peter-mueller
 */
public class SimulationState implements AutoCloseable {
    public final Clock clock = new Clock();
    public final Queue queue = new ListQueue();
    public final List<Phone> phones = new ArrayList<>();
    public final EventList events = new EventList();
    public final CallShopLogs logs;

    public final CallShopCalculation calculation = new CallShopCalculation();

    SimulationState(CallShopLogs logs) throws IOException {
        if (logs == null ) {
            throw new IllegalArgumentException("Logs cannot be null!");
        }
        this.logs = logs;
    }

    @Override
    public void close() throws IOException {
        logs.close();
    }
}
