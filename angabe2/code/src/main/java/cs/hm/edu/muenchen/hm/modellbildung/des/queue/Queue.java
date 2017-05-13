package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;

import java.util.List;

/**
 * @author peter-mueller
 */
public interface Queue {
    /**
     * Adds a new Person to the queue
     * @param person
     */
    void enqueue(Person person);

    /**
     * @return the first Person in the queue
     */
    Person dequeue();

    /**
     * Returns the first vip/resident. If there is none the first non-vip will be returned.
     * @return the next person in line
     */
    Person dequeueVip();
    List<Person> getList();

    /**
     * @return the amount of non-residents in the queue
     */
    long countNormal();

    /**
     * @return the amount of residents in the queue
     */
    long countResident();

    /**
     * @return the amount of Persons in the queue
     */
    long countAll();
    boolean isEmpty();
}
