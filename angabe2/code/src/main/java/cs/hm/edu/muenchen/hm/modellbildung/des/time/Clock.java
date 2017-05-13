package cs.hm.edu.muenchen.hm.modellbildung.des.time;

import java.math.BigDecimal;

/**
 * This class administrates the representation of the time in this simulation.
 * @author peter-mueller
 */
public class Clock {
    private BigDecimal time = new BigDecimal(0);

    /**
     * Advances the clock relative to the future.
     * @param time
     */
    public void advance(BigDecimal time) {
        if (time.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Time must not be negative!");
        }
        this.time = this.time.add(time);
    }

    /**
     * Advances the clock absolute to the future.
     * @param time
     */
    public void advanceTo(BigDecimal time) {
        if (this.time.compareTo(time) > 0) {
            throw new IllegalArgumentException("Time must be greater than the current system time!");
        }
        this.time = time;
    }
    public BigDecimal systemTime() {
        return time;
    }
}
