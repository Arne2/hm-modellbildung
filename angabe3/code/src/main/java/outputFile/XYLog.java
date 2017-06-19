package outputFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * Created by dima on 12.06.17.
 * This is the logging class for the measurement.
 */
public class XYLog implements AutoCloseable {

    private final Log log;

    public XYLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("x",
                "y");
    }

    public XYLog(final Path file, String xName, String yName) throws IOException {
        this.log = new Log(file);
        log.log(xName,
                yName);
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