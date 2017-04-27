package cs.hm.edu.muenchen.hm.modellbildung.time;

/**
 * @author peter-mueller
 */
public class Event implements Comparable<Event> {
    private final EventType type;
    private final double timeStamp;

    public Event(EventType type, double timeStamp) {
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public EventType getType() {
        return type;
    }

    public double getTimeStamp() {
        return timeStamp;
    }
    @Override
    public int compareTo(Event o) {
        return Double.compare(this.getTimeStamp(), o.getTimeStamp());
    }

}
