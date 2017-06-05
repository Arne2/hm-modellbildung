package functional;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.view.StringView;
import main.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by dima on 01.06.17.
 */
public class XChickenTest {
    private static final int CELL_SIZE = 50;
    private static final double VELOCITY = 1.48;
    private static final double DEVIATION = 0.114;
    private final Configuration conf = new Configuration.Builder(new String [] {""})
            .cellSize(CELL_SIZE)
            .velocity(VELOCITY)
            .deviation(DEVIATION)
            .build();

    @Test
    public void testChickenTest(){
        Field field = StringView.parseStringMap(
                        "000000X000000\n" +
                        "0000000000000\n" +
                        "000       000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000 00000 000\n" +
                        "000    0  000\n" +
                        "0000000000000\n" +
                        "0000000000000");
        final Simulation simulation = new Simulation(field, conf, null);
        simulation.spawnPerson(new Location(6,3));
        simulation.run(new BigDecimal(100000));

        System.out.println(simulation.getPersons().toArray().length);
        if(simulation.field.numberOfPersons() != 0){
            System.out.println(StringView.useMap(field, simulation.getUse()));
            Assert.fail("chicken test failed: still " +
                    simulation.getPersons().toArray().length + " persons in field");
        }
    }
}
