package outputFile;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.view.StringView;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arne on 30.05.2017.
 */
@XmlRootElement
public class Output{
    private String distanceMap;
    private final Configuration config;
    private Field field;
    private String fieldmap;
    private String name = "output";
    private List<OutputEvent> events;
    private int fieldWidth;
    private int fieldHeight;
    private int targetX;
    private int targetY;
    private String algorithm;

    // Default Constructor necessary for JAXB
    public Output(){
        config = null;
    }

    public Output(Configuration config_, Field field_){
        events = new ArrayList<>();
        this.config = config_;
        this.field = field_;
        this.fieldmap = StringView.toStringMap(field);
        fieldWidth = field.getWidth();
        fieldHeight = field.getHeight();
        targetX = field_.getTarget().x;
        targetY = field_.getTarget().y;
        algorithm = config_.getAlgorithm().toString();
    }

    public void setDistanceMap(Map<Location, Double> use_){
        distanceMap = StringView.toStringDistanceMap(use_);
    }

    public void setDistanceMap(String distancemap){
        distanceMap = distancemap;
    }

    public String getDistanceMap(){
        return distanceMap;
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

    public String getFieldmap() {
        return fieldmap;
    }

    public void setFieldmap(String fieldmap) {
        this.fieldmap = fieldmap;
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

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
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
