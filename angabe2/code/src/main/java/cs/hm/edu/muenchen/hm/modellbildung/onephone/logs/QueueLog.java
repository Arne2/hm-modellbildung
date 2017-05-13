package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class QueueLog implements AutoCloseable {
    private final Log log;

    public QueueLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("time", "size", "room");
    }

    public void log(BigDecimal timeStamp, long size, long roomSize) {
        final String[] strings = {
                timeStamp.toString(),
                String.valueOf(size),
                String.valueOf(roomSize)
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
