package cs.hm.edu.muenchen.hm.modellbildung.des.time.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author peter-mueller
 */
public class EventList {
    @Override
    public String toString() {
        return eventList.toString();
    }

    private final List<Event> eventList = new LinkedList<>();
    public Event nextEvent() {
        Collections.sort(eventList);
        return eventList.remove(0);
    }

    public void add(Event event) {
        eventList.add(event);
    }
}
