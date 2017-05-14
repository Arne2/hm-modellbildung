package cs.hm.edu.muenchen.hm.modellbildung.distribution;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author peter-mueller
 */
public class NegativeExponentialDistributionTest {
    private static final int SEED = 13;
    private static final int AMOUNT = 100000;
    private final Distribution dist = new NegativeExponentialDistribution(SEED);

    @Test
    public void testRandomSeed() {
        final Distribution dist = new NegativeExponentialDistribution();
        dist.getNextValue(1);
    }

    @Test
    public void getNextValue() throws Exception {
        for (int mean = -10; mean < 10; mean++) {
            double sum = 0;

            for (int i = 0; i < AMOUNT; i++) {
                sum += dist.getNextValue(mean);
            }

            final double meanOfDist = sum / AMOUNT;
            final double error = mean - meanOfDist;

            if (Math.abs(error) > 0.1) {
                Assert.fail("mean was " + meanOfDist + "but should be around " + mean);
            }
        }
    }
}