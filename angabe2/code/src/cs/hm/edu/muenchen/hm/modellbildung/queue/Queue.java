package cs.hm.edu.muenchen.hm.modellbildung.queue;

import cs.hm.edu.muenchen.hm.modellbildung.data.Person;

/**
 * @author peter-mueller
 */
public interface Queue {
    public void enqueue(Person person);
    public Person dequeue();

    public long count();
}
