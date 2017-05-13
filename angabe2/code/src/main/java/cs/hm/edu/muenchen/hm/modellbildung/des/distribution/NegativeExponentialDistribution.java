package cs.hm.edu.muenchen.hm.modellbildung.des.distribution;

import java.util.Random;

/**
 * This class is responsible for creating random numbers which are negative exponential distributed.
 * @author peter-mueller
 */
public class NegativeExponentialDistribution implements Distribution {
    private final Random random;

    public NegativeExponentialDistribution(int seed) {
        random = new Random(seed);
    }

    public NegativeExponentialDistribution() {
        random = new Random();
    }

    public double getNextValue(double mean) {
        mean *= -1;
        return Math.log(1- random.nextDouble()) * mean;
    }
}
