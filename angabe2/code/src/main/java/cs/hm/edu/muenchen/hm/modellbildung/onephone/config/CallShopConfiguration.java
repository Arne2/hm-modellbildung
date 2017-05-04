package cs.hm.edu.muenchen.hm.modellbildung.onephone.config;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

/**
 * @author peter-mueller
 */

public class CallShopConfiguration {
    public static int MEAN_ARRIVAL = 1000;
    public static int MEAN_CALL = 100;
    public static int VIP_PERCENTAGE = 10;
    public static int CONFIGURATION = 1;

    public static Log arrivalLog;
    public static Log serveLog;
    public static Log finishLog;
    public static Log queueLog;
}