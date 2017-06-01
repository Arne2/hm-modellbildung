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
                        "000000X000000\n" +
                        "0000000000000\n" +
                        "000       000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000  000  000\n" +
                        "0000000000000\n" +
                        "0000000000000");

        final Configuration build = new Configuration.Builder(args)
                .build();
        final Simulation simulation = new Simulation(field, build);
        OutputFile output = new OutputFile(build, field);
        simulation.spawnPerson(Location.of(4,3));
        simulation.spawnPerson(Location.of(5,3));
        simulation.spawnPerson(Location.of(6,3));
        simulation.spawnPerson(Location.of(7,3));
        simulation.spawnPerson(Location.of(8,3));

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
