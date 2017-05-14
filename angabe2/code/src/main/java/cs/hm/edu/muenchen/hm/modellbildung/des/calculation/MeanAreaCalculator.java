package cs.hm.edu.muenchen.hm.modellbildung.des.calculation;


import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class MeanAreaCalculator {

    private BigDecimal sumArea = BigDecimal.ZERO;
    private BigDecimal lastTime = BigDecimal.ZERO;

    public BigDecimal getMean() {
        return mean;
    }

    private BigDecimal mean = BigDecimal.ZERO;

    private long skip;

    public MeanAreaCalculator() {
        this(0);
    }

    public MeanAreaCalculator(long skip) {
        this.skip = skip;
    }

    public void calculate(BigDecimal height, BigDecimal current) {
        if (skip-- > 0) {
            return;
        }
        if (current.equals(BigDecimal.ZERO)) {
            return;
        }
        final BigDecimal delta = current.subtract(lastTime);
        final BigDecimal area = delta.multiply(height);
        sumArea = sumArea.add(area);
        mean = sumArea.divide(current, 32, BigDecimal.ROUND_HALF_EVEN);

        lastTime = current;
    }
}
