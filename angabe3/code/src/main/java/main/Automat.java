package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.view.StringView;
import jdk.nashorn.internal.runtime.ECMAException;
import outputFile.OutputFile;

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
        final Field field = StringView.parseStringMap(
                "X 000         \n" +
                "0 00000  0  0 \n" +
                "0 000 00  00 0\n" +
                "000 0 0 0 00 0\n" +
                "0 0 0 00 00 00\n");

        final Configuration build = new Configuration.Builder(args)
                .build();
        final Simulation simulation = new Simulation(field, build);
        OutputFile output = new OutputFile(build, field);
        simulation.spawnPerson(Location.of(13,4));
        simulation.spawnPerson(Location.of(13,3));

        System.out.println(StringView.personMap(simulation.field));
        System.out.println(StringView.useMap(simulation.field, simulation.use));
        simulation.run(BigDecimal.valueOf(200000));
        try {
            output.save("output.xml");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
