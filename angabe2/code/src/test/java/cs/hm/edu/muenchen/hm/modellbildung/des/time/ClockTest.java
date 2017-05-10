package cs.hm.edu.muenchen.hm.modellbildung.des.time;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class ClockTest {

    @Test
    public void advance() throws Exception {
        final Clock clock = new Clock();
        clock.advance(new BigDecimal(1));
        if (!clock.systemTime().equals(new BigDecimal(1))) {
            Assert.fail("Failed to advance clock time!");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void advanceNegativeValue() {
        final Clock clock = new Clock();
        clock.advance(new BigDecimal(-1));
    }

    @Test
    public void advanceTo() throws Exception {
        final Clock clock = new Clock();
        clock.advanceTo(new BigDecimal(2));
        if (!clock.systemTime().equals(new BigDecimal(2))) {
            Assert.fail("Failed to advance to time two!");
        }
        clock.advanceTo(new BigDecimal(3));
        if (!clock.systemTime().equals(new BigDecimal(3))) {
            Assert.fail("Failed to advance to time three!");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void advanceToBack() {
        final Clock clock = new Clock();
        clock.advanceTo(new BigDecimal(2));
        clock.advanceTo(new BigDecimal(0));
    }

    @Test
    public void systemTime() throws Exception {
        final Clock clock = new Clock();
        if (!clock.systemTime().equals(new BigDecimal(0))) {
            Assert.fail("System time is not zero!");
        }
        clock.advance(new BigDecimal(1));
        if (!clock.systemTime().equals(new BigDecimal(1))) {
            Assert.fail("System time is not one!");
        }
        clock.advanceTo(new BigDecimal(2));
        if (!clock.systemTime().equals(new BigDecimal(2))) {
            Assert.fail("System time is not two!");
        }
    }
}