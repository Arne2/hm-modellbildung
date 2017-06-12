package use;

import field.Field;
import field.location.Location;
import field.use.FastMarching;
import field.view.StringView;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 01.06.17.
 */
public class FastMarchingTest {

    public static final int CELL_SIZE = 20;

    @Test
    public void testFastMarching(){
        Field field = StringView.parseStringMap("" +
                "000\n" +
                "0X0\n" +
                "000\n", CELL_SIZE);
        Map<Location, Double> result = new HashMap<>();
        result.put(new Location(0,0), -1.71);
        result.put(new Location(0,1), -1.0);
        result.put(new Location(0,2), -1.71);
        result.put(new Location(1,0), -1.0);
        result.put(new Location(1,1), -0.0);
        result.put(new Location(1,2), -1.0);
        result.put(new Location(2,0), -1.71);
        result.put(new Location(2,1), -1.0);
        result.put(new Location(2,2), -1.71);
        Map<Location, Double> calculation = FastMarching.use(field);

        for (Location e: calculation.keySet()) {
            if(result.containsKey(e)){
                if(Math.abs(result.get(e) - calculation.get(e)) > 0.01){
                    Assert.fail("wrong fast marching Calculation\n" +
                            "Location: (X:" + e.x+ "|Y:" + e.y + ")\n" +
                            "Diff: " + Math.abs(result.get(e) - calculation.get(e)));
                }
            }
        }
    }

}
