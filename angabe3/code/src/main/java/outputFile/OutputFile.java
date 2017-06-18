package outputFile;

import config.Configuration;
import field.Field;
import field.location.Location;
import measurement.Measurement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by dima on 28.05.17.
 * The file in which the simulation will be saved.
 */
public class OutputFile {

    private Output output;
    public OutputFile(Configuration config_, Field field_){
        output = new Output(config_, field_);
    }

    /**
     * Completes the writing of the output file.
     * @param filename
     * @throws JAXBException
     * @throws IOException
     */
    public void save(String filename) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Output.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //m.marshal(output, System.out);
        m.marshal(this.output, new FileWriter(filename));
    }

    public void setMeasurement(Measurement measurement){
        output.setFromX(measurement.getFromX());
        output.setFromY(measurement.getFromY());
        output.setToX(measurement.getToX());
        output.setToY(measurement.getToY());
    }

    public void setDistances(Map<Location, Double> use){
        output.setDistanceMap(use);
    }

    public void addMoveEvent(BigDecimal time, int id, long x, long y){
        output.addMoveEvent(time, id, x, y);
    }

    public void addRemoveEvent(BigDecimal time, int id, long x, long y){
        output.addRemoveEvent(time, id, x, y);
    }

    public void addPawnEvent(BigDecimal time, int id, long x, long y){
        output.addPawnEvent(time, id, x, y);
    }


}

