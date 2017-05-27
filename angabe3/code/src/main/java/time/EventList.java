package time;

import time.events.Event;

import java.util.Collection;
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

    public boolean hasNext() {
        return !eventList.isEmpty();
    }

    /**
     * Adds an Event to the list.
     *
     * @param event
     */
    public void add(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event must not be null!");
        }
        eventList.add(event);
    }

    public void addAll(Collection<Event> events) {
        if (events == null) {
            throw new IllegalArgumentException("Events must not be null!");
        }
        eventList.addAll(events);
    }
}
