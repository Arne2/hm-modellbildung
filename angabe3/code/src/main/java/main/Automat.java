/**
 *
 */
package main;

import field.Field;
import field.location.Location;
import field.view.StringView;
import person.Person;
import time.events.Event;
import time.events.PersonMoveEvent;

import java.util.List;

/**
 * @author DD00033863
 */
public class Automat {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final Field field = StringView.parseStringMap("X 000\n" +
                "0 000\n" +
                "0 000\n" +
                "000 0\n" +
                "0 0 0\n");

        final State state = new State(field);

        final Person person = new Person(14.0);
        final Person person2 = new Person(14.0);
        state.field.putPerson(person, Location.of(4, 4));
        state.field.putPerson(person2, Location.of(4, 3));

        state.events.add(new PersonMoveEvent(state.clock.systemTime(), state, person));
        state.events.add(new PersonMoveEvent(state.clock.systemTime(), state, person2));

        System.out.println(StringView.personMap(state.field));
        System.out.println(StringView.useMap(state.field, state.use));
        while (state.events.hasNext()) {
            final Event event = state.events.nextEvent();
            state.clock.advanceTo(event.getTime());
            final List<Event> newEvents = event.execute();
            state.events.addAll(newEvents);
            System.out.println(state.clock.systemTime());
            System.out.println(StringView.personMap(state.field));
        }
    }

}
