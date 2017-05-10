package cs.hm.edu.muenchen.hm.modellbildung.onephone.config;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */

public class CallShopConfiguration {
    public static int MEAN_ARRIVAL = 1000;
    public static int MEAN_CALL = 100;
    public static BigDecimal DURATION = new BigDecimal(100000000);
    public static int VIP_PERCENTAGE = 10;
    public static int CONFIGURATION = 1;
    public static String OUTPATH = "../data/";
}