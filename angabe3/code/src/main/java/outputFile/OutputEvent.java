package outputFile;

import java.math.BigDecimal;

/**
 * Created by dima on 28.05.17.
 */
public class OutputEvent{
    private String type;
    private BigDecimal time;
    private int personID;
    private long positionX;
    private long positionY;

    OutputEvent (String type_, BigDecimal time_, int personID_, long x_, long y_){
        setType(type_);
        setTime(time_);
        setPersonID(personID_);
        setPositionX(x_);
        setPositionY(y_);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTime() {
        return time;
    }

    public void setTime(BigDecimal time) {
        this.time = time;
    }

    public int getPersonID() {
        return personID;
    }

    public void setPersonID(int personID) {
        this.personID = personID;
    }

    public long getPositionX() {
        return positionX;
    }

    public void setPositionX(long positionX) {
        this.positionX = positionX;
    }

    public long getPositionY() {
        return positionY;
    }

    public void setPositionY(long positionY) {
        this.positionY = positionY;
    }
}