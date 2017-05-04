package cs.hm.edu.muenchen.hm.modellbildung.des.queue;

import cs.hm.edu.muenchen.hm.modellbildung.des.data.Person;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Person dequeueVip() {
        if(list.isEmpty()) {
            return null;
        }
        Optional<Person> person = list.stream().filter(Person::isResident).findFirst();
//        for (Person person : list){
//            if (person.isResident()){
//                list.remove(person);
//                return person;
//            }
//        }
//        return list.remove(0);

        if (person.isPresent()) {
            list.remove(person.get());
            return person.get();
        } else {
            return list.remove(0);
        }

    }

    public long count() {
        return list.size();
    }
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
