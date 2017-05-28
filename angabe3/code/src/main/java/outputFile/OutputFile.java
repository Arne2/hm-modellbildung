package outputFile;

import config.Configuration;
import field.Field;
import time.events.Event;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dima on 28.05.17.
 */

public class OutputFile {

    private static Output output = new Output();
    public OutputFile(Configuration config_, Field field_){
        output = new Output(config_, field_);
    }

    public void save(String filename)throws Exception{
        JAXBContext context = JAXBContext.newInstance(Output.class);

        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //m.marshal(output, System.out);
        m.marshal(this.output, new FileWriter(filename));
    }

    public static void addMoveEvent(BigDecimal time, int id, long x, long y){
        output.addMoveEvent(time, id, x, y);
    }

    public static void addRemoveEvent(BigDecimal time, int id, long x, long y){
        output.addRemoveEvent(time, id, x, y);
    }

    public static void addPawnEvent(BigDecimal time, int id, long x, long y){
        output.addPawnEvent(time, id, x, y);
    }


}
@XmlRootElement
class Output{
    //private final Configuration config;
    //private Field field;
    private String name = "output";
    private List<OutputEvent> events;

    Output(){
        //this.config = null;
        //this.field = null;
        events = new ArrayList<OutputEvent>();
    }
    Output(Configuration config_, Field field_){
        //this.config = config_;
        //this.field = field_;
        events = new ArrayList<OutputEvent>();
    }


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEvents(List<OutputEvent> events){
        this.events = events;
    }

    public Configuration getConfig(){
        //return config;
        return null;
    }

    public Field getField(){
        //return field;
        return null;
    }

    public List<OutputEvent> getEvents(){
        return events;
    }

    public void addMoveEvent(BigDecimal time, int id, long x, long y){
        events.add(new OutputEvent("move", time, id, x, y));
    }

    public void addRemoveEvent(BigDecimal time, int id, long x, long y){
        events.add(new OutputEvent("remove", time, id, x, y));
    }

    public void addPawnEvent(BigDecimal time, int id, long x, long y){
        events.add(new OutputEvent("pawn", time, id, x, y));
    }


}
