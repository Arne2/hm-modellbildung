package cs.hm.edu.muenchen.hm.modellbildung.calculation;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class MeanCalculator {
    private BigDecimal sum = BigDecimal.ZERO;
    private long n = 0;

    private long skip;

    public MeanCalculator() {
        this(0);
    }
    public MeanCalculator(long skip) {
        this.skip = skip;
    }


    public void calculate(BigDecimal value) {
        if (skip-- > 0) {
            return;
        }
        sum = sum.add(value);
        n++;
    }

    public BigDecimal getMean() {
        if (n == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(n), 32 ,BigDecimal.ROUND_HALF_EVEN);
    }

}
