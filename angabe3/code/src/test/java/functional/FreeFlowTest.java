package functional;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.location.Locations;
import field.view.StringView;
import main.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

/*
* @author peter-mueller
*/

public class FreeFlowTest {
    private static final double VELOCITY = 1.48;
    private static final int CELL_SIZE = 20;
    private final Configuration conf = new Configuration.Builder(new String [] {""})
            .cellSize(CELL_SIZE)
            .velocity(() -> VELOCITY)
            .build();

    @Test
    public void testHorizontal() throws Exception {
        final Field field = StringView.parseStringMap("0000000000X\n",CELL_SIZE);
        Calculation calculation = new Calculation(field).invoke();

        if (calculation.getError() > 0.0) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail(message);
        }
    }

    @Test
    public void testOnTarget() throws Exception {
        final Field field = StringView.parseStringMap("X\n",CELL_SIZE);
        Calculation calculation = new Calculation(field).invoke();

        //If person on Target, then the velocity is 0
        //Thus the difference between expected
        //and actual velocity is VELOCITY

        if (calculation.getError() > VELOCITY) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail(message);
        }
    }

    @Test
    public void testVertical() throws Exception {
        final Field field = StringView.parseStringMap(
                "0\n" +
                        "0\n" +
                        "0\n" +
                        "0\n" +
                        "X\n", CELL_SIZE
        );
        Calculation calculation = new Calculation(field).invoke();

        if (calculation.getError() > 0.0) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail("Person did not move with the right speed! " + message);
        }
    }

    @Test
    public void testDiagonal() throws Exception {
        final Field field = StringView.parseStringMap(
                "000\n" +
                        "000\n" +
                        "00X\n", CELL_SIZE
        );
        Calculation calculation = new Calculation(field).invoke();

        if (calculation.getError() > 0.0) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail("Person did not move with the right speed! " + message);
        }
    }

    @Test
    public void testSlope() throws Exception {
        final Field field = StringView.parseStringMap(
                "0000\n" +
                        "0000\n" +
                        "000X\n", CELL_SIZE
        );
        Calculation calculation = new Calculation(field).invoke();

        if (calculation.getError() > 0.06) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail("Person did not move with the right speed! " + message);
        }
    }
    @Test
    public void testSlope2() throws Exception {
        final Field field = StringView.parseStringMap(
                        "00000\n" +
                        "00000\n" +
                        "0000X\n", CELL_SIZE
        );
        Calculation calculation = new Calculation(field).invoke();

        if (calculation.getError() > 0.21) {
            final String message = String.format(
                    "Expected velocity: %s, but got %s! (error of %s)",
                    VELOCITY, calculation.getActualVelocity(), calculation.getError()
            );
            Assert.fail("Person did not move with the right speed! " + message);
        }
    }

    private class Calculation {
        private final Field field;
        private double actualVelocity;
        private double error;

        public Calculation(Field field) {
            this.field = field;
        }

        public double getActualVelocity() {
            return actualVelocity;
        }

        public double getError() {
            return error;
        }

        public Calculation invoke() throws Exception {
            final Simulation simulation = new Simulation(field, conf, null);
            final Location start = Location.of(0, 0);
            simulation.spawnPerson(start);
            simulation.run(BigDecimal.valueOf(1000));

            //final BigDecimal duration = simulation.getClock().systemTime();
            //final double length = Locations.distance(start, simulation.field.getTarget());
            //actualVelocity = length / duration.doubleValue();
            actualVelocity = simulation.getPersons().get(0).getMeanVelocity().doubleValue();
            error = Math.abs(actualVelocity - VELOCITY);
            return this;
        }
    }
}
