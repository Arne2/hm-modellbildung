package person;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by dima on 05.06.17.
 */
public class PersonTest {


    @Test
    public void testStep(){

        Person a = new Person(1.5);
        a.step(new BigDecimal(3), new BigDecimal(2));
        a.step(new BigDecimal(3), new BigDecimal(2));
        a.step(new BigDecimal(3), new BigDecimal(2));

        a.step(new BigDecimal(6), new BigDecimal(4));

        if(a.getVelocity() != 1.5) Assert.fail("velocity assignment error");
        if(a.getMeanVelocity().doubleValue() != 1.5) Assert.fail("velocity calculation error");
        if(a.getDistanceWent().doubleValue() != 15) Assert.fail("distance calculation error");
        if(a.getTimePassed().doubleValue() != 10) Assert.fail("time passed calculation error");
        if(a.getTimeSinceLastMove() != 2) Assert.fail("time since last move assignment error");

    }
}
