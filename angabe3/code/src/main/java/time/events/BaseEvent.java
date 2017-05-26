package time.events;

import main.State;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public abstract class BaseEvent implements Event {
    private final BigDecimal time;
    private final State state;
    public BaseEvent(BigDecimal time, State state) {
        this.time = time;
        this.state = state;
    }

    @Override
    public BigDecimal getTime() {
        return time;
    }

    @Override
    public int compareTo(Event o) {
        return getTime().compareTo(o.getTime());
    }

    public State getState() {
        return state;
    }
}
