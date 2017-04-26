package cs.hm.edu.muenchen.hm.modellbildung.time;

/**
 * @author peter-mueller
 */
public class Clock {
    private double time = 0;

    public void advance(double time) {
        this.time += time;
    }
    public double getSystemTime() {
        return time;
    }
}
