package cs.hm.edu.muenchen.hm.modellbildung.log;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Logging class for csv files with one column
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
