package main;

import config.Configuration;
import field.Field;
import field.FieldImporter;
import field.location.Location;
import field.view.StringView;
import jdk.nashorn.internal.runtime.ECMAException;
import outputFile.OutputFile;
import person.Person;
import person.VelocityDistribution;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
        final Field field2 = fi.getField();
        OutputFile output = new OutputFile(build, field2);
        final Simulation simulation = new Simulation(field2, build, output);

        simulation.run(BigDecimal.valueOf(build.getMaxDuration()));

        output.save(build.getOutput() + build.getAlgorithm().toString() + ".xml");
    }

}
