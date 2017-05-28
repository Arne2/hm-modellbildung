package person;

import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class Person {
    private final double velocity;
    private double timeSinceLastMove;
    private BigDecimal distanceWent;
    private BigDecimal timePassed;
    private BigDecimal meanVelocity;
    public Person(double velocity) {
        this.velocity = velocity;
        this.timeSinceLastMove = 0;
        this.distanceWent = new BigDecimal(0);
        this.timePassed = new BigDecimal(0);
        this.meanVelocity = new BigDecimal(0);
    }

    public double getVelocity() {
        return velocity;
    }

    public double getTimeSinceLastMove(){
        return this.timeSinceLastMove;
    }

    public BigDecimal getMeanVelocity(){
        return meanVelocity;
    }

    public BigDecimal getDistanceWent(){
        return this.distanceWent;
    }

    public BigDecimal getTimePassed(){
        return this.timePassed;
    }


    public void step(BigDecimal distance, BigDecimal time){
        this.distanceWent = this.distanceWent.add(distance);
        this.timePassed = timePassed.add(time);
        if(timePassed.equals(new BigDecimal(0)) == false){
            meanVelocity = distanceWent.divide(timePassed, 32, BigDecimal.ROUND_HALF_EVEN);
        }

    }


}
