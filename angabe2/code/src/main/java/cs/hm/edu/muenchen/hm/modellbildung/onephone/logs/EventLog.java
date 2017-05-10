package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class EventLog implements AutoCloseable{
    private final Log log;

    public EventLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("id", "resident", "time");
    }

    public void log(int id, boolean resident, BigDecimal time) {
        final String[] strings = {
                String.valueOf(id),
                String.valueOf(resident),
                time.toString()
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
