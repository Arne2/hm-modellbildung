package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class CalculationLog implements AutoCloseable{
    private final Log log;

    public CalculationLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("time",
                "event",
                "queueSize",
                "systemSize",
                "meanQueueSize",
                "meanSystemSize",
                "meanWaitingTime",
                "meanCallingTime",
                "meanSystemTime");
    }

    public void log(BigDecimal time,
                    String event,
                    BigDecimal queueSize,
                    BigDecimal systemSize,
                    BigDecimal meanQueueSize,
                    BigDecimal meanSystemSize,
                    BigDecimal meanWaitingTime,
                    BigDecimal meanCallingTime,
                    BigDecimal meanSystemTime
                    ) {
        final String[] strings = {
                time.toString(),
                event,
                queueSize.toString(),
                systemSize.toString(),
                meanQueueSize.toString(),
                meanSystemSize.toString(),
                meanWaitingTime.toString(),
                meanCallingTime.toString(),
                meanSystemTime.toString()
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
