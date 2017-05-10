package cs.hm.edu.muenchen.hm.modellbildung.des.log;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author peter-mueller
 */
public class Log implements AutoCloseable {
    private static final String DEFAULT_SEPARATOR = ",";

    private final Writer writer;

    public Log(Path file) throws IOException {
        createFolders(file);
        this.writer = Files.newBufferedWriter(file);
    }

    private void createFolders(Path path) throws IOException {
        if (!path.toFile().exists()) {
            final Path parent = path.getParent();
            parent.toFile().mkdirs();
        }
    }

    public void log(String... strings) {
        final String line = String.join(DEFAULT_SEPARATOR, strings);
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
