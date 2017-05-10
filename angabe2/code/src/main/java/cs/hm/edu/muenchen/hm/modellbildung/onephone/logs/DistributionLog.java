package cs.hm.edu.muenchen.hm.modellbildung.onephone.logs;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class DistributionLog implements AutoCloseable{
    private final Log log;

    public DistributionLog(final Path file) throws IOException {
        this.log = new Log(file);
        log.log( "time");
    }

    public void log(double time) {
        System.out.println(time);
        final String[] strings = {
                String.valueOf(time),
        };
        log.log(strings);
    }

    @Override
    public void close() throws IOException {
        log.close();
    }
}
