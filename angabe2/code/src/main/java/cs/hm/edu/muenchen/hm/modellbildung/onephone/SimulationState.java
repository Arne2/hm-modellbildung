package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.Clock;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.EventList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
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
