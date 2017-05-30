package visualisation;

import outputFile.Output;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Arne on 30.05.2017.
 */
public class SimulationGui {
    public static void main(String[] args) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Output.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            FileReader reader = new FileReader("output.xml");
            Output output = (Output) unmarshaller.unmarshal(reader);
            System.out.println(output);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
