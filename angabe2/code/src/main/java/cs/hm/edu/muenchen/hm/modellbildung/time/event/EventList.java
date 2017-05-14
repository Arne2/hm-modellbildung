package cs.hm.edu.muenchen.hm.modellbildung.time.event;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author peter-mueller
 */
public class EventList {
    private final List<Event> eventList = new LinkedList<>();

    /**
     * @return the next element in chronological order.
     */
    public Event nextEvent() {
        if (eventList.isEmpty()) {
            return null;
        }
        Collections.sort(eventList);
        return eventList.remove(0);
    }

    /**
     * Adds an Event to the list.
     * @param event
     */
    public void add(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null!");
        }
        eventList.add(event);
    }
}
