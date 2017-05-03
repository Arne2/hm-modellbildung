package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;

/**
 * @author peter-mueller
 */
public interface Queue {
    void enqueue(Person person);
    Person dequeue();

    long count();
    boolean isEmpty();
}
