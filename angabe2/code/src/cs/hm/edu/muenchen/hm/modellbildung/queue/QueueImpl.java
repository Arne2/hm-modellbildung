package cs.hm.edu.muenchen.hm.modellbildung.queue;

import cs.hm.edu.muenchen.hm.modellbildung.data.Person;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Arne on 27.04.2017.
 */
public class QueueImpl implements Queue{

    private List<Person> list = new LinkedList<>();

    @Override
    public void enqueue(Person person) {
        list.add(person);
    }

    @Override
    public Person dequeue() {
        if(list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    @Override
    public long count() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
