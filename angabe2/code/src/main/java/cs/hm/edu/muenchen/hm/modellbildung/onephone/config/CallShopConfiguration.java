package cs.hm.edu.muenchen.hm.modellbildung.onephone.config;

import cs.hm.edu.muenchen.hm.modellbildung.des.log.Log;

/**
 * @author peter-mueller
 */

public class CallShopConfiguration {
    public static final int MEAN_ARRIVAL = 1000;
    public static final int MEAN_CALL = 100;

    public static final Log arrivalLog = new Log("arrival.csv");
    public static final Log serveLog = new Log("serve.csv");
    public static final Log finishLog = new Log("finish.csv");
    public static final Log queueLog = new Log("queue.csv");
}