package cs.hm.edu.muenchen.hm.modellbildung.des.time;

/**
 * @author peter-mueller
 */
public class Clock {
    private double time = 0;

    @Override
    public String toString() {
        return "{" +
                "time: " + time +
                '}';
    }

    public void advance(double time) {
        if (time < 0) {
            throw new IllegalArgumentException("Time must not be negative!");
        }
        this.time += time;
    }
    public void advanceTo(double time) {
        if (time < this.time) {
            throw new IllegalArgumentException("Time must be greater than the current system time!");
        }
        this.time = time;
    }
    public double systemTime() {
        return time;
    }
}
