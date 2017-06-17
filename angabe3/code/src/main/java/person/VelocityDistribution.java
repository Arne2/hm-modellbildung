package person;

import java.util.Random;

/**
 * @author peter-mueller
 */
public class VelocityDistribution {

    private final double deviation;
    private final double velocity;
    private final Random random;

    public VelocityDistribution(double deviation, double velocity, long seed) {
        this.deviation = deviation;
        this.velocity = velocity;
        this.random = new Random(seed);
    }

    public double nextVelocity() {
        return random.nextGaussian() * deviation + velocity;
    }

}
