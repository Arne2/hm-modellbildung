package time.events;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public interface Event extends Comparable<Event> {
    void execute();
    BigDecimal getTime();
}
