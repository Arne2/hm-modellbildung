package cs.hm.edu.muenchen.hm.modellbildung.queue;

import cs.hm.edu.muenchen.hm.modellbildung.domain.Person;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the queue a Person has to enter before being able to use a Phone.
 * Created by Arne on 27.04.2017.
 */
public class ListQueue implements Queue {

    private final List<Person> list = new LinkedList<>();

    public void enqueue(Person person) {
        list.add(person);
    }

    @Override
    public List<Person> getList() {
        return list;
    }

    public Person dequeue() {
        if (list.isEmpty()) {
            return null;
        }
        return list.remove(0);
    }

    @Override
    public Person dequeueVip() {
        if (list.isEmpty()) {
            return null;
        }
        Optional<Person> person = list.stream().filter(Person::isResident).findFirst();
        if (person.isPresent()) {
            list.remove(person.get());
            return person.get();
        }
        return dequeue();
    }

    public long countNormal() {
        return list.stream().filter(p -> !p.isResident()).count();
    }

    public long countResident() {
        return list.stream().filter(Person::isResident).count();
    }

    public long countAll() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
