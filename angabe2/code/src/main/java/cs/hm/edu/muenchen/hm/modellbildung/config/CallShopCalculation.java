package cs.hm.edu.muenchen.hm.modellbildung.config;

import cs.hm.edu.muenchen.hm.modellbildung.config.CallShopConfiguration;
import cs.hm.edu.muenchen.hm.modellbildung.calculation.MeanAreaCalculator;
import cs.hm.edu.muenchen.hm.modellbildung.calculation.MeanCalculator;

/**
 * This class contains the configuration for the calculation.
 * @author peter-mueller
 */
public class CallShopCalculation {
    public final MeanAreaCalculator meanQueueSizeNormal = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanQueueSizeResident = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanQueueSizeAll = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);

    public final MeanAreaCalculator meanPhoneSizeNormal = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanPhoneSizeAll = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanPhoneSizeResident = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);

    public final MeanAreaCalculator meanSystemSizeNormal = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanSystemSizeResident = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);
    public final MeanAreaCalculator meanSystemSizeAll = new MeanAreaCalculator(CallShopConfiguration.SKIP_N);

    public final MeanCalculator meanQueueTimeNormal = new MeanCalculator(CallShopConfiguration.SKIP_N);
    public final MeanCalculator meanQueueTimeResident = new MeanCalculator(CallShopConfiguration.SKIP_N);
    public final MeanCalculator meanQueueTimeAll = new MeanCalculator(CallShopConfiguration.SKIP_N);

    public final MeanCalculator meanSystemTimeNormal = new MeanCalculator(CallShopConfiguration.SKIP_N);
    public final MeanCalculator meanSystemTimeResident = new MeanCalculator(CallShopConfiguration.SKIP_N);
    public final MeanCalculator meanSystemTimeAll = new MeanCalculator(CallShopConfiguration.SKIP_N);
}
