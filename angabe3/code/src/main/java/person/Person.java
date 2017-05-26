package person;

import field.location.Location;

/**
 * @author peter-mueller
 */
public class Person {
    private final double velocity;
    private Location location;

    public Person(Location location, double velocity) {
        this.location = location;
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return "Person{" +
                "location=" + location +
                ", velocity=" + velocity +
                '}';
    }

    public Location getLocation() {
        return location;
    }

    public void moveTo(Location location) {
        this.location = location;
    }
}
