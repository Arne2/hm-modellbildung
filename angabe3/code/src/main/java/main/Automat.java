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
                .build();

        FieldImporter fi = new FieldImporter(build);
        final Field field2 = fi.getField();
        OutputFile output = new OutputFile(build, field2);
        final Simulation simulation = new Simulation(field2, build, output);
/*        simulation.spawnPerson(Location.of(4,3));
        simulation.spawnPerson(Location.of(5,3));
        simulation.spawnPerson(Location.of(6,3));
        simulation.spawnPerson(Location.of(7,3));
        simulation.spawnPerson(Location.of(8,3));*/

        //System.out.println(StringView.personMap(simulation.field));
        //System.out.println(StringView.useMap(simulation.field, simulation.getUse()));
        simulation.run(BigDecimal.valueOf(50000));
        try {
            output.save("output.xml");
        }catch (JAXBException e){
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

}
