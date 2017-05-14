package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class XLog implements AutoCloseable {
    private final Log log;

    public XLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("x");
    }

    public void log(double x) {
        final String[] strings = {
                String.valueOf(x)
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
