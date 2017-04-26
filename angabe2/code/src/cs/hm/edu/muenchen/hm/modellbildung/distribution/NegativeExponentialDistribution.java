package cs.hm.edu.muenchen.hm.modellbildung.distribution;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author peter-mueller
 */
public class NegativeExponentialDistribution implements Distribution {
    private final Random random = new Random();

    @Override
    public double getNextValue(double mean) {
        mean *= -1;
        return Math.log(1- random.nextDouble()) * mean;
    }
}
