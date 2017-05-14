package cs.hm.edu.muenchen.hm.modellbildung.domain;

import java.math.BigDecimal;

/**
 * This class represents a Customer of the Callshop.
 * @author peter-mueller
 */
public class Person {
    private static int UNIQUE_ID = 0;
    private final int id = ++UNIQUE_ID;

    private BigDecimal ArrivalTime = new BigDecimal(-1);
    private BigDecimal BeginTime = new BigDecimal(-1);
    private BigDecimal FinishTime = new BigDecimal(-1);


    private final boolean resident;

    public Person() {
        this(false);
    }
    public Person(boolean resident) {
        this.resident = resident;
    }

    public int getId() {
        return id;
    }

    public boolean isResident() {
        return resident;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setArrivalTimeStamp(BigDecimal time){
        ArrivalTime = time;
    }
    public void setBeginTimeStamp(BigDecimal time){
        BeginTime = time;
    }
    public void setFinishTimeStamp(BigDecimal time){
        FinishTime = time;
    }

    public BigDecimal getArrivalTime(){
        return ArrivalTime;
    }
    public BigDecimal getBeginTime(){
        return BeginTime;
    }
    public BigDecimal getFinishTime(){
        return FinishTime;
    }

}
