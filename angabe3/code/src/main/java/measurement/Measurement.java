package measurement;

import config.Configuration;
import field.Field;
import field.RotatingField;
import field.location.Location;
import outputFile.XYLog;
import person.Person;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by user on 14.06.17.
 */
public class Measurement implements AutoCloseable {

    // Parameters of the Measurement field.
    private final int fromX;
    private final int fromY;
    private final int toX;
    private final int toY;
    private final double measureFieldSize;

    private final Configuration config;

    // Log files
    private final XYLog fundamental;
    private final XYLog timeVeloctiy;
    private final XYLog timeDensity;
    private final XYLog timeFlow;

    public Measurement(Set<Location> measurementPoints, Configuration config) throws IOException {
        this.config = config;

        Path fundamentalLogFile = new File(config.getOutput() + "./fundamental.csv").toPath();
        fundamental = new XYLog(fundamentalLogFile, "density", "velocity");

        Path timeVelocityLogFile = new File(config.getOutput() + "./timeVelocity.csv").toPath();
        timeVeloctiy = new XYLog(timeVelocityLogFile, "time", "velocity");

        Path timeDensityLogFile = new File(config.getOutput() + "./timeDensity.csv").toPath();
        timeDensity = new XYLog(timeDensityLogFile, "time", "density");


        Path timeFlowFile = new File(config.getOutput() + "./timeFlow.csv").toPath();
        timeFlow = new XYLog(timeFlowFile, "time", "density");

        if (measurementPoints.size() == 0) {
            fromX = 0;
            fromY = 0;
            toY = 0;
            toX = 0;
        } else {
            toX = measurementPoints.stream().max(Comparator.comparingInt((Location loc) -> loc.getX())).get().getX();
            toY = measurementPoints.stream().max(Comparator.comparingInt((Location loc) -> loc.getY())).get().getY();
            fromX = measurementPoints.stream().min(Comparator.comparingInt((Location loc) -> loc.getX())).get().getX();
            fromY = measurementPoints.stream().min(Comparator.comparingInt((Location loc) -> loc.getY())).get().getY();
        }
        measureFieldSize = ((toX - fromX - 1) * config.getCellSize()) * ((toY - fromY - 1) * config.getCellSize());
    }

    /**
     * Calculates and logs the information for later purposes.
     *
     * @param field
     * @param time
     */
    public void measure(Field field, BigDecimal time) {
        BigDecimal sum_v = new BigDecimal(0);

        Map<Location, Person> personsToMeasure = new HashMap<>();
        for (Map.Entry<Person, Location> person : field.getPersons().entrySet()) {

            if (person.getValue().getX() > toX ||
                    person.getValue().getX() < fromX ||
                    person.getValue().getY() > toY ||
                    person.getValue().getY() < fromY) {
                continue;
            }
            personsToMeasure.put(person.getValue(), person.getKey());
            final BigDecimal slidingAverageVelocity = person.getKey().getSlidingAverageVelocity();
            sum_v = sum_v.add(slidingAverageVelocity);
        }
        if (personsToMeasure.size() == 0) {
            fundamental.log(new BigDecimal(0), new BigDecimal(0));
            return;
        }
        BigDecimal meanv = sum_v.divide(new BigDecimal(personsToMeasure.size()), 32, BigDecimal.ROUND_HALF_EVEN);

        BigDecimal density = new BigDecimal(personsToMeasure.size() / measureFieldSize);

        fundamental.log(density, meanv);
        timeVeloctiy.log(time, meanv);
        timeDensity.log(time, density);
        if (field instanceof RotatingField) {
            final long l = ((RotatingField) field).clearSteppedOverLine();
            timeFlow.log(time, BigDecimal.valueOf(l));
        }
    }


    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }

    @Override
    public void close() throws Exception {
        fundamental.close();
        timeDensity.close();
        timeVeloctiy.close();
        timeFlow.close();
    }
}
