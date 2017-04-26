package cs.hm.edu.muenchen.hm.modellbildung.time;

/**
 * @author peter-mueller
 */
public class Event implements Comparable<Event> {
    private final int timeStamp;

    public Event(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTimeStamp() {
        return timeStamp;
    }
    @Override
    public int compareTo(Event o) {
        return Float.compare(o.getTimeStamp(), this.getTimeStamp());
    }

}
