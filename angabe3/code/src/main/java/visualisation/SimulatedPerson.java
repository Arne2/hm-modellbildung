package visualisation;

/**
 * Created by Arne on 30.05.2017.
 */
public class SimulatedPerson {

    private final int id;
    private long x;
    private long y;

    public SimulatedPerson(int id, long x, long y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "SimulatedPerson{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
