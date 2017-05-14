package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class XYLog implements AutoCloseable {

    private final Log log;

    public XYLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("x",
                "y");
    }

    public void log(BigDecimal x, BigDecimal y) {
        final String[] strings = {
                x.toString(),
                y.toString()
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
