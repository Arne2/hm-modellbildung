package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;

import java.util.List;

/**
 * @author peter-mueller
 */
public interface Queue {
    void enqueue(Person person);
    Person dequeue();
    Person dequeueVip();
    List<Person> getList();
    long count();
    boolean isEmpty();
}
