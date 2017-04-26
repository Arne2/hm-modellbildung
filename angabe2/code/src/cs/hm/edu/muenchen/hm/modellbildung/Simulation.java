package cs.hm.edu.muenchen.hm.modellbildung;

import cs.hm.edu.muenchen.hm.modellbildung.distribution.Distribution;

/**
 * @author peter-mueller
 */
public interface Simulation {
    public void run(Distribution arrival, Distribution service, long duration);
}
