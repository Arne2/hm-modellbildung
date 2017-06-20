package field;

import field.location.Location;
import person.Person;

/**
 * @author peter-mueller
 */
public class RotatingField extends Field {
    public RotatingField(double cellSize) {
        super(cellSize);
    }


    public long clearSteppedOverLine() {
        final long steppedOver = this.steppedOver;
        this.steppedOver = 0;
        return steppedOver;
    }
    private long steppedOver = 0;

    @Override
    public void putPerson(Person person, Location location)
    {
        final double cellsInOneMeter = 1 / getCellSize();
        if (location.getX() == 10) {
            steppedOver += 1 / cellsInOneMeter;
        }
        super.putPerson(person, modLocation(location));
    }

    @Override
    public boolean isFree(Location location) {
        if (location.getX() < 0) {
            return false;
        }
        return super.isFree(modLocation(location));
    }

    @Override
    public boolean has(Location location) {
        return super.has(modLocation(location));
    }

    private int width = -1;

    @Override
    public int getWidth() {
        if (width == -1) {
            width = super.getWidth();
        }
        return width;
    }

    private boolean enabled = false;

    public void enableMod() {
        this.enabled = true;
    }

    @Override
    public boolean isTarget(Location target) {
        return false;
    }

    private Location modLocation(Location location) {
        if (!enabled) {
            return location;
        }

        if (location.getX() >= getWidth() - 1) {
            final int width = getWidth();
            location = location.withX(location.getX() - width + 1);
        } else if (location.getX() < 0) {
            location = location.withX(getWidth()  + location.getX());
        }
        return location;
    }
}
