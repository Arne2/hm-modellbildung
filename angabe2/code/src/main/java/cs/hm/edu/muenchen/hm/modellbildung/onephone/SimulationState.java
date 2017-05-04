package cs.hm.edu.muenchen.hm.modellbildung.onephone;

import cs.hm.edu.muenchen.hm.modellbildung.des.queue.Queue;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.Clock;
import cs.hm.edu.muenchen.hm.modellbildung.des.time.event.EventList;
import cs.hm.edu.muenchen.hm.modellbildung.des.data.Phone;

import java.util.List;

/**
 * @author peter-mueller
 */
public interface SimulationState {
    List<Phone> phones();
    Queue queue();
    Clock clock();
    EventList events();
}
