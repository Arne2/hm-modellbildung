package main;

import config.Configuration;
import field.Field;
import field.FieldImporter;
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
                .maxDuration(100)
                .build();

        FieldImporter fi = new FieldImporter(build);
        final Field field2 = fi.getField();
        OutputFile output = new OutputFile(build, field2);
        final Simulation simulation = new Simulation(field2, build, output);
        simulation.run(BigDecimal.valueOf(build.getMaxDuration()));
        output.save(build.getOutput() + build.getAlgorithm().toString() + ".xml");
    }

}
