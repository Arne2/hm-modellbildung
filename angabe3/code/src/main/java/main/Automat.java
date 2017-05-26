/**
 * 
 */
package main;

import config.Configuration;
import field.Field;
import field.location.Location;
import person.Person;
import time.events.PersonMoveEvent;

import java.math.BigDecimal;

/**
 * @author DD00033863
 *
 */
public class Automat {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Field field = new Field(
				Configuration.INSTANCE.fieldSizeX,
				Configuration.INSTANCE.fieldSizeY);

		field.at(Location.of(1,0)).occupy();
		field.at(Location.of(1,1)).occupy();
		field.at(Location.of(1,2)).occupy();
		field.at(Location.of(1,3)).occupy();
		field.at(Location.of(1,4)).occupy();
		final State state = new State(field);

		final Person person = new Person(Location.of(50, 50), 14.0);
		while (!person.getLocation().equals(Location.of(0,0))) {
			new PersonMoveEvent(BigDecimal.ONE, state, person).execute();
			System.out.println(person);
		}
	}

}
