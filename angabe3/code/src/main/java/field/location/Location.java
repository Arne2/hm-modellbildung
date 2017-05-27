package field.location;

/**
 * @author peter-mueller
 */
public class Location implements Comparable<Location>{
    public final int x;
    public final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Location of(int x, int y) {
        return new Location(x,y);
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

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

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
