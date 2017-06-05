package field;

import field.location.Location;
import field.location.Locations;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by dima on 05.06.17.
 */
public class LocationTest {
    @Test
    public void equalsTest(){
        Location loc_a = Location.of(1 , 2);
        Location loc_b = Location.of(1 , 2);
        Location loc_c = Location.of(1, 3);
        Location loc_d = Location.of(3,1);
        Location loc_e = Location.of(5,4);

        if(!(loc_a).equals(loc_a)) Assert.fail("location equals error");
        if(!loc_a.equals(loc_b))Assert.fail("location equals error");
        if(loc_a.equals(loc_c))Assert.fail("location equals error");
        if(loc_a.equals(loc_d))Assert.fail("location equals error");
        if(loc_a.equals(loc_e))Assert.fail("location equals error");
    }

    @Test
    public void compareTo(){

        Location loc_a = Location.of(1 , 2);
        Location loc_b = Location.of(1 , 2);
        Location loc_c = Location.of(1, 3);
        Location loc_d = Location.of(3,1);

        if(loc_a.compareTo(loc_a) !=0 ) Assert.fail("location compareTo error");
        if(loc_a.compareTo(loc_b) !=0 ) Assert.fail("location compareTo error");
        if(loc_a.compareTo(loc_c) == 0 ) Assert.fail("location compareTo error");
        if(loc_a.compareTo(loc_d) == 0 ) Assert.fail("location compareTo error");

    }

    @Test
    public void testRelative(){
        Location a = Location.of(1, 2);
        Location b = Location.of(1,1);
        Location c = a.relative(b);
        if(c.x != 2 || c.y != 3) Assert.fail("location relative error");

        c = c.relative(Location.of(-2, -3));
        if(c.x != 0 || c.y != 0)Assert.fail("location relative error");
    }

    @Test
    public void testHashCode(){
        Location a = Location.of(1,5);
        if(a.hashCode() != 36) Assert.fail("Wrong HashCode Calculation");

    }

    @Test
    public void testDistance(){
        Location a = Location.of(1,5);

        Location b = Location.of(1,5);
        if(Locations.distance(a,b) != 0) Assert.fail("location distance calculation error");

        Location c = Location.of(1,10);
        if(Locations.distance(a,c) != 5) Assert.fail("location distance calculation error");

        Location d = Location.of(10,5);
        if(Locations.distance(a,d) != 9) Assert.fail("location distance calculation error");

        Location e = Location.of(6,10);
        if(Math.abs(Locations.distance(a,e) - 7.071) > 0.01) Assert.fail("location distance calculation error");

    }


}
