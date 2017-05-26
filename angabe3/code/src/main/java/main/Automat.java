/**
 *
 */
package main;

import config.Configuration;
import field.Field;
import field.Fields;
import field.location.Location;
import person.Person;
import time.events.PersonMoveEvent;

import java.math.BigDecimal;

/**
 * @author DD00033863
 */
public class Automat {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final Field field = Fields.parseStringMap("0 000\n" +
                "0 000\n" +
                "0 000\n" +
                "0 0 0\n" +
                "000 0\n");
        final State state = new State(field);

        final Person person = new Person(Location.of(4, 4), 14.0);
        while (!person.getLocation().equals(Location.of(0, 0))) {
            new PersonMoveEvent(BigDecimal.ONE, state, person).execute();
            System.out.println(person);
        }
    }

}
