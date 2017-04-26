package cs.hm.edu.muenchen.hm.modellbildung.time;

import java.util.LinkedList;
import java.util.List;

/**
 * @author peter-mueller
 */
public class Simulator {
    private final List<Event> eventList = new LinkedList<>();

    public void doUntil(double deadTime) {
        final Clock clock = new Clock();
        while ( clock.getSystemTime() < deadTime) {
            nextEvent();
        }
    }

    public Event nextEvent() {
        return eventList.remove(0);
    }

    public void add(Event event) {
        eventList.add(event);
    }
}
