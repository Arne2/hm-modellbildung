package cs.hm.edu.muenchen.hm.modellbildung.des.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author peter-mueller
 */
public class EventListTest {

    @Test
    public void nextEvent() throws Exception {
        final EventList list = new EventList();
        if (list.nextEvent() != null) {
            Assert.fail("Next Event on empty list must return null!");
        }
        final SimpleEvent now = new SimpleEvent(0);
        final SimpleEvent later = new SimpleEvent(1);
        list.add(later);
        list.add(now);
        if (!list.nextEvent().equals(now)) {
            Assert.fail("First next event must be the earlier one!");
        }
    }

    @Test
    public void add() throws Exception {
        final EventList list = new EventList();
        final SimpleEvent event = new SimpleEvent(1);
        list.add(event);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNull() {
        final EventList list = new EventList();
        list.add(null);
    }

    private class SimpleEvent extends BaseEvent {

        public SimpleEvent(double timeStamp) {
            super(timeStamp);
        }

        @Override
        public void execute(SimulationState state) {

        }
    }
}