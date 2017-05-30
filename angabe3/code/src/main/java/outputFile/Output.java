package outputFile;

import config.Configuration;
import field.Field;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arne on 30.05.2017.
 */
@XmlRootElement
public class Output{
    private final Configuration config;
    private Field field;
    private String name = "output";
    private List<OutputEvent> events;
    private int fieldWidth;
    private int fieldHeight;

    // Default Constructor necessary for JAXB
    public Output(){
        config = null;
    }

    public Output(Configuration config_, Field field_){
        this.config = config_;
        this.field = field_;
        events = new ArrayList<>();
        fieldWidth = field.getWidth();
        fieldHeight = field.getHeight();
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public void setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public void setEvents(List<OutputEvent> events){
        this.events = events;
    }

    public Configuration getConfig(){
        return config;
    }

    public Field getField(){
        return field;
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

    @Override
    public String toString() {
        return "Output{" +
                "name='" + name + '\'' +
                ", events=" + events +
                ", fieldWidth=" + fieldWidth +
                ", fieldHeight=" + fieldHeight +
                '}';
    }
}
