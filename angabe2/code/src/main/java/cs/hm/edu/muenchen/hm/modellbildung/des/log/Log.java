package cs.hm.edu.muenchen.hm.modellbildung.des.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peter-mueller
 */
public class Log {

    private FileWriter fw;

    public Log(String filename){
        File file = new File(filename);
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            List<String> fields = new ArrayList<>();
            fields.add("Person");
            fields.add("Timestamp");
            fields.add("Queuelength");
            CSVUtils.writeLine(fw, fields);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(List<String> values){
        try {
            CSVUtils.writeLine(fw, values);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        fw.flush();
        fw.close();
    }
}
