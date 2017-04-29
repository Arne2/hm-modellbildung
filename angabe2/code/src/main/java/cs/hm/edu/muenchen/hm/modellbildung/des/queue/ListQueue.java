package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.onephone.data.Person;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Arne on 27.04.2017.
 */
public class ListQueue implements Queue {

    private final List<Person> list = new LinkedList<>();

    public void enqueue(Person person) {
        list.add(person);
    }

    public Person dequeue() {
        if(list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }
    public long count() {
        return list.size();
    }
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
