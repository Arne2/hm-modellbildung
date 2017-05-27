package functional;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.location.Locations;
import field.view.StringView;
import main.Simulation;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class FreeFlowTest {
    private static final double VELOCITY = 1.0;
    private final Configuration conf = new Configuration.Builder()
            .cellSize(1)
            .velocity(VELOCITY)
            .build();

    @Test
    public void testHorizontal() {
        final Field field = StringView.parseStringMap("0000000000X\n");
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
    public void testOnTarget() {
        final Field field = StringView.parseStringMap("X\n");
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
    public void testVertical() {
        final Field field = StringView.parseStringMap(
                "0\n" +
                        "0\n" +
                        "0\n" +
                        "0\n" +
                        "X\n"
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
    public void testDiagonal() {
        final Field field = StringView.parseStringMap(
                "000\n" +
                        "000\n" +
                        "00X\n"
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
    public void testSlope() {
        final Field field = StringView.parseStringMap(
                "0000\n" +
                        "0000\n" +
                        "000X\n"
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
    public void testSlope2() {
        final Field field = StringView.parseStringMap(
                "00000\n" +
                        "00000\n" +
                        "0000X\n"
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
        private Field field;
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

        public Calculation invoke() {
            final Simulation simulation = new Simulation(field, conf);
            final Location start = Location.of(0, 0);
            simulation.spawnPerson(start);
            simulation.run(BigDecimal.valueOf(1000));

            final BigDecimal duration = simulation.clock.systemTime();
            final double length = Locations.distance(start, simulation.field.getTarget());
            actualVelocity = length / duration.doubleValue();
            error = Math.abs(actualVelocity - VELOCITY);
            return this;
        }
    }
}
