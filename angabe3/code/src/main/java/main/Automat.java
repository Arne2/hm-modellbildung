package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.view.StringView;

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
        final Field field = StringView.parseStringMap("X 000\n" +
                "0 000\n" +
                "0 000\n" +
                "000 0\n" +
                "0 0 0\n");

        final Configuration build = new Configuration.Builder(args)
                .build();
        final Simulation simulation = new Simulation(field, build);

        simulation.spawnPerson(Location.of(4,4));
        simulation.spawnPerson(Location.of(4,3));

        System.out.println(StringView.personMap(simulation.field));
        System.out.println(StringView.useMap(simulation.field, simulation.use));
        simulation.run(BigDecimal.valueOf(100000));
    }

}
