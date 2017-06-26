package outputFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * Created by user on 20.06.17.
 */
public class XYZLog  implements AutoCloseable{
    private final Log log;

    public XYZLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log("x",
                "y",
                "z");
    }

    public XYZLog(final Path file, String xName, String yName,String zName) throws IOException {
        this.log = new Log(file);
        log.log(xName,
                yName,
                zName);
    }

    public void log(BigDecimal x, BigDecimal y, BigDecimal z) {
        final String[] strings = {
                x.toString(),
                y.toString(),
                z.toString()
        };
        log.log(strings);
    }


    @Override
    public void close() throws Exception {
        log.close();
    }
}
