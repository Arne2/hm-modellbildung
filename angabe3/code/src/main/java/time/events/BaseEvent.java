package time.events;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public abstract class BaseEvent implements Event {
    private final BigDecimal time;

    public BaseEvent(BigDecimal time) {
        this.time = time;
    }

    @Override
    public BigDecimal getTime() {
        return time;
    }

    @Override
    public int compareTo(Event o) {
        return getTime().compareTo(o.getTime());
    }

}
