package person;

import javax.management.OperationsException;
import java.math.BigDecimal;

/**
 * @author peter-mueller
 */
public class Person {
    private static int UNIQUE_ID = 0;
    private final int id = ++UNIQUE_ID;

    private final double velocity;
    private double timeSinceLastMove;
    private BigDecimal distanceWent;
    private BigDecimal timePassed;
    private BigDecimal meanVelocity;

    //calculate the sliding mean velocity of 1 Values
    private BigDecimal slidingVelocity[] = new BigDecimal[1];
    private int slidingCounter = 0;

    public Person(double velocity) {
        this.velocity = velocity;
        this.timeSinceLastMove = 0;
        this.distanceWent = new BigDecimal(0);
        this.timePassed = new BigDecimal(0);
        this.meanVelocity = new BigDecimal(0);

        //initialize sliding Velocity to zero
        //At Begin of Simulation persons do not move
        for(int i = 0; i < slidingVelocity.length ; i++)
        {
            slidingVelocity[i] = new BigDecimal(0);
        }
    }

    public int getId(){
        return id;
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

    public BigDecimal getSlidingAverageVelocity(){
        BigDecimal slidingAverage = new BigDecimal(0);
        for (BigDecimal velocity: slidingVelocity ) {
            slidingAverage = slidingAverage.add(velocity);
        }
        return slidingAverage.divide(new BigDecimal(slidingVelocity.length), 32, BigDecimal.ROUND_HALF_EVEN);
    }

    public void step(BigDecimal distance, BigDecimal timeForMove){
        if(timeForMove.doubleValue() != 0) {
            //add new Velocity to Array
            slidingVelocity[(slidingCounter++)&0b0] = distance.divide(timeForMove, 32, BigDecimal.ROUND_HALF_EVEN);
        }

        this.distanceWent = this.distanceWent.add(distance);
        this.timePassed = timePassed.add(timeForMove);
        if(timePassed.equals(new BigDecimal(0)) == false){
            meanVelocity = distanceWent.divide(timePassed, 32, BigDecimal.ROUND_HALF_EVEN);
        }

    }


}
