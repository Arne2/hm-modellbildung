package person;

import field.location.Location;

/**
 * @author peter-mueller
 */
public class Person {
    private final double velocity;

    public Person(double velocity) {
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }
}
