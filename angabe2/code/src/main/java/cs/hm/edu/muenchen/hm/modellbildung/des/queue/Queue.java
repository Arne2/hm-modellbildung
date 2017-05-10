package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;

/**
 * @author peter-mueller
 */
public interface Queue {
    void enqueue(Person person);
    Person dequeue();
    Person dequeueVip();

    long count();
    boolean isEmpty();
}
