package main;

import config.Configuration;
import field.RotatingField;
import field.FieldImporter;
import field.location.Location;
import outputFile.OutputFile;

import java.math.BigDecimal;

/**
 * @author DD00033863
 */
public class Automat {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        final Configuration build = new Configuration.Builder(args)
                .withVelocityDistribution(Long.valueOf("seed", 36))
                .maxDuration(1000)
                .build();

        FieldImporter fi = new FieldImporter(build);
        final RotatingField field2 = fi.getField();
        field2.addMeasurePoint(Location.of(0,-1));
        field2.addMeasurePoint(Location.of(field2.getWidth(),field2.getHeight()+1));
        OutputFile output = new OutputFile(build, field2);
        final Simulation simulation = new Simulation(field2, build, output);

        field2.enableMod();
        simulation.run(BigDecimal.valueOf(build.getMaxDuration()));

        output.save(build.getOutput() + build.getAlgorithm().toString() + ".xml");
    }

}
