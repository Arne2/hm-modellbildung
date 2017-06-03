package field.location;

/**
 * Provides a simple value object for a location.
 *
 * @author peter-mueller
 */
public class Location implements Comparable<Location> {
    /** x offset from the top left corner to the right. */
    public final int x;
    /** y offset from the top left corner to the bottom. */
    public final int y;

    /**
     * Create a new Location with the given offsets.
     *
     * @param x horizontal offset rightwards
     * @param y vertical offset downwards
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new Location based on two integers as offset.
     *
     * @param x horizontal offset rightwards
     * @param y vertical offset downwards
     * @return a Location
     */
    public static Location of(int x, int y) {
        return new Location(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return x == location.x && y == location.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * Combine two locations. They are added together.
     *
     * @param other the other location to add on this one.
     * @return A new location that describes the sum.
     */
    public Location relative(Location other) {
        return new Location(
                this.x + other.x,
                this.y + other.y
        );
    }

    @Override
    public int compareTo(Location o) {
        final int result = Integer.compare(this.y, o.y);
        if (result == 0) {
            return Integer.compare(this.x, o.x);
        }
        return result;
    }
}
