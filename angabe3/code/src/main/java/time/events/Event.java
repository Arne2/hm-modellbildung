package time.events;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author peter-mueller
 */
public interface Event extends Comparable<Event> {
    List<Event> execute();
    BigDecimal getTime();
}
