package cs.hm.edu.muenchen.hm.modellbildung.distribution;

/**
 * @author peter-mueller
 */
public interface Distribution {
    double getNextValue(double mean);
}
