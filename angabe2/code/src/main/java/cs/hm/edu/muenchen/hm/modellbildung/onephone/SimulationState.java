package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.ListQueue;
import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.Clock;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.EventList;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.CalculationLog;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.DistributionLog;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.EventLog;
import cs.hm.edu.muenchen.hm.modellbildung.onephone.logs.QueueLog;

import java.io.IOException;
import java.nio.file.Path;
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

    public final EventLog arrivalLog;
    public final EventLog beginServeLog;
    public final EventLog finishServeLog;
    public final QueueLog queueLog;
    public final DistributionLog arrivalDistributionLog;
    public final DistributionLog serveDistributionLog;
    public final CalculationLog calculationLog;
    SimulationState(Path folder) throws IOException {
        arrivalLog = new EventLog(folder.resolve("arrival.csv"));
        beginServeLog = new EventLog(folder.resolve("begin.csv"));
        finishServeLog = new EventLog(folder.resolve("finish.csv"));
        queueLog = new QueueLog(folder.resolve("queue.csv"));
        arrivalDistributionLog= new DistributionLog(folder.resolve("arrival_distribution.csv"));
        serveDistributionLog= new DistributionLog(folder.resolve("serve_distribution.csv"));
        calculationLog = new CalculationLog(folder.resolve("calculation.csv"));

    }

    @Override
    public void close() throws IOException {
        arrivalLog.close();
        beginServeLog.close();
        finishServeLog.close();
        queueLog.close();
    }
}
