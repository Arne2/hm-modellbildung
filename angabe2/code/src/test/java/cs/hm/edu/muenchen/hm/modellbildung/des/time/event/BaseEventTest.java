package cs.hm.edu.muenchen.hm.modellbildung.des.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.SimulationState;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author peter-mueller
 */
public class BaseEventTest {

    private class SimpleEvent extends BaseEvent {
        public SimpleEvent(double timeStamp) {
            super(timeStamp);
        }

        @Override
        public void execute(SimulationState state) {
        }
    }

    @Test
    public void getTimeStamp() throws Exception {
        final double stamp = 1;
        final BaseEvent event = new SimpleEvent(stamp);

        if (event.getTimeStamp() != stamp) {
            Assert.fail("TimeStamp does not match!");
        }
    }

    @Test
    public void compareTo() throws Exception {
        final double now = 1;
        final double later = 1;
        final BaseEvent nowEvent = new SimpleEvent(now);
        final BaseEvent laterEvent = new SimpleEvent(later);

        if (nowEvent.compareTo(laterEvent) > 0) {
            Assert.fail("Now should be smaller compared to later!");
        }
    }
}