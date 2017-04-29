package cs.hm.edu.muenchen.hm.modellbildung.des.time;

import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;

/**
 * @author peter-mueller
 */
public abstract class BaseEvent implements Event {
    private final double timeStamp;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "timeStamp: " + timeStamp +
                "}";
    }

    public BaseEvent(double timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public double getTimeStamp() {
        return timeStamp;
    }
    @Override
    public int compareTo(Event o) {
        return Double.compare(this.getTimeStamp(), o.getTimeStamp());
    }

}
