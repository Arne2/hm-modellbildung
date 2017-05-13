package cs.hm.edu.muenchen.hm.modellbildung.des.time.event;

import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.Event;

import java.math.BigDecimal;

/**
 * The base class for every Event
 * @author peter-mueller
 */
public abstract class BaseEvent implements Event {
    private final BigDecimal timeStamp;

    public BaseEvent(BigDecimal timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public BigDecimal getTimeStamp() {
        return timeStamp;
    }
    @Override
    public int compareTo(Event o) {
        return getTimeStamp().compareTo(o.getTimeStamp());
    }

}
