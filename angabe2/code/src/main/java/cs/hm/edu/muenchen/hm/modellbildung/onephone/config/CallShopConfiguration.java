package cs.hm.edu.muenchen.hm.modellbildung.onephone.config;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains the configuration for the simulation
 * @author peter-mueller
 */
public class CallShopConfiguration {
    public static long SKIP_N = 0;
    public static int MEAN_ARRIVAL = 1000;
    public static int MEAN_CALL = 100;
    public static BigDecimal DURATION = new BigDecimal(100000000);
    public static int VIP_PERCENTAGE = 10;
    public static int CONFIGURATION = 1;
    public static String OUTPATH = "../data/";

    /**
     * Sets the constants of the Configuration at the begin of the program.
     *
     * @param args array of strings
     */
    public static void loadConfiguration(String[] args) {
        List<String> arguments = Arrays.asList(args);
        int index = arguments.indexOf("-a");
        if (index >= 0 && arguments.size() > index) {
            String s = arguments.get(index + 1);
            MEAN_ARRIVAL = Integer.parseInt(s);
        }
        index = arguments.indexOf("-c");
        if (index >= 0 && arguments.size() > index) {
            String s = arguments.get(index + 1);
            MEAN_CALL = Integer.parseInt(s);
        }
        index = arguments.indexOf("-d");
        if (index >= 0 && arguments.size() > index) {
            String s = arguments.get(index + 1);
            DURATION = new BigDecimal(Integer.parseInt(s));
        }
        index = arguments.indexOf("-vip");
        if (index >= 0 && arguments.size() > index) {
            String s = arguments.get(index + 1);
            VIP_PERCENTAGE = Integer.parseInt(s);
        }
        index = arguments.indexOf("-conf");
        if (index >= 0 && arguments.size() > index) {
            String s = arguments.get(index + 1);
            CONFIGURATION = Integer.parseInt(s);
        }
        index = arguments.indexOf("-o");
        if (index >= 0 && arguments.size() > index) {
            OUTPATH = arguments.get(index + 1);
        }
        index = arguments.indexOf("-skip");
        if (index >= 0 && arguments.size() > index) {
            final String s = arguments.get(index + 1);
            SKIP_N = Long.parseLong(s);
        }
    }

    /**
     * Writes the program parameters into a file for documentation.
     * @param folder
     */
    public static void logConfiguration(Path folder) {
        if (!folder.toFile().exists()) {
            final Path parent = folder.getParent();
            parent.toFile().mkdirs();
        }
        try {
            Writer fw = Files.newBufferedWriter(folder);
            fw.write("Programmstart mit folgenden Parametern: \n Mittlere Ankunftszeit: " +
                    MEAN_ARRIVAL + "\n Mittlere Telefonierzeit: " + MEAN_CALL + "\n Gesamtdauer: " + DURATION +
                    "\n Wahrscheinlichkeit auf Dorfbewohner: " + VIP_PERCENTAGE + "\n Konfiguration: " + CONFIGURATION);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}