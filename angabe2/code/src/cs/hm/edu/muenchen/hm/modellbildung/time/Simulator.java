package cs.hm.edu.muenchen.hm.modellbildung.time;

import cs.hm.edu.muenchen.hm.modellbildung.data.Person;
import cs.hm.edu.muenchen.hm.modellbildung.distribution.NegativeExponentialDistribution;
import cs.hm.edu.muenchen.hm.modellbildung.processes.Phone;
import cs.hm.edu.muenchen.hm.modellbildung.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.queue.QueueImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * @author peter-mueller
 */
public class Simulator {
    public static final int MEANARRIVAL = 1000;
    public static final int MEANCALL = 100;
    private final List<Event> eventList = new LinkedList<>();
    private final Clock clock = new Clock();
    private final Queue queue = new QueueImpl();
    private final NegativeExponentialDistribution random = new NegativeExponentialDistribution();
    private final Phone p1 = new Phone();
    private int id = 0;

    public void doUntil(double deadTime) {
        //first Event
        System.out.println("Start");
        Event ev = new Event(EventType.ARRIVAL, clock.getSystemTime() + random.getNextValue(MEANARRIVAL));
        addEvent(ev);
        while ( clock.getSystemTime() < deadTime) {
            System.out.println("Clocktime: " + clock.getSystemTime());
            eventList.sort(Event::compareTo);
            nextEvent();
        }
    }

    public Event nextEvent() {

        Event event = eventList.remove(0);
        System.out.println("Queueleingh " + queue.count() + " Event: " + event.getTimeStamp() + " - " + event.getType());
        clock.advance(event.getTimeStamp() - clock.getSystemTime());
        //stream().mapToDouble(Event::getTimeStamp).min();
        switch (event.getType()) {
            case ARRIVAL:
                queue.enqueue(new Person(id++,true));
                if (!p1.isOccupied()){
                    addEvent(new Event(EventType.BEGIN,event.getTimeStamp()));
                }
                addEvent(new Event(EventType.ARRIVAL, event.getTimeStamp() + random.getNextValue(MEANARRIVAL)));
                break;
            case BEGIN:
                p1.setOccupied(true);
                queue.dequeue();
                addEvent(new Event(EventType.FINISH, event.getTimeStamp() + random.getNextValue(MEANCALL)));
                break;
            case FINISH:
                p1.setOccupied(false);
                if (!queue.isEmpty()){
                    addEvent(new Event(EventType.BEGIN,event.getTimeStamp()));
                }
                break;
        }
        return event;
    }

    public void addEvent(Event event) {

        eventList.add(event);
    }
}
