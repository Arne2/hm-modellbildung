package main;

import config.Configuration;
import field.Field;
import field.FieldImporter;
import field.location.Location;
import field.view.StringView;
import jdk.nashorn.internal.runtime.ECMAException;
import outputFile.OutputFile;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

/**
 * @author DD00033863
 */
public class Automat {

    /**
     * @param args
     */
    public static void main(String[] args) {
/*        final Field field = StringView.parseStringMap(
                        "000000X000000\n" +
                        "0000000000000\n" +
                        "000       000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000  000  000\n" +
                        "0000000000000\n" +
                        "0000000000000");*/



        final Configuration build = new Configuration.Builder(args)
                .maxDuration(4)
                .build();

        FieldImporter fi = new FieldImporter(build);
        final Field field2 = fi.getField();
        OutputFile output = new OutputFile(build, field2);
        final Simulation simulation = new Simulation(field2, build, output);
        simulation.run(BigDecimal.valueOf(build.getMaxDuration()));
        try {
            output.save(build.getOutput());
        }catch (JAXBException e){
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

}
