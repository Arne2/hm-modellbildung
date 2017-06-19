package outputFile;

import config.Configuration;
import field.Field;
import field.location.Location;
import field.view.StringView;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arne on 30.05.2017.
 * Represents the simulation in the xml format for post visualization.
 */
@XmlRootElement
public class Output{
    // Information of the Simulation
    private String distanceMap;
    private final Configuration config;
    private Field field;
    private String fieldmap;
    private String name = "output";
    private List<OutputEvent> events;
    private Set<Location> targets;
    private int fieldWidth;
    private int fieldHeight;
    private String cellsize = "40cm";
    private String algorithm;
    private double freeFlowVelocity;
    private double deviation;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

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
        targets = field_.getTargets();
        algorithm = config_.getAlgorithm().toString();
        freeFlowVelocity = config_.getVelocity();
        deviation = config_.getDeviation();
        cellsize = config_.getCellSize()*100 + "cm";
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


    public Set<Location> getTargets(){
        return targets;
    }

    public void setTargets(Set<Location> targets_){
        targets = targets_;
    }


    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCellsize() {
        return cellsize;
    }

    public void setCellsize(String cellsize) {
        this.cellsize = cellsize;
    }

    public double getFreeFlowVelocity() {
        return freeFlowVelocity;
    }

    public void setFreeFlowVelocity(double freeFlowVelocity) {
        this.freeFlowVelocity = freeFlowVelocity;
    }

    public double getDeviation() {
        return deviation;
    }

    public void setDeviation(double deviation) {
        this.deviation = deviation;
    }

    public int getFromX() {
        return fromX;
    }

    public void setFromX(int fromX) {
        this.fromX = fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public void setFromY(int fromY) {
        this.fromY = fromY;
    }

    public int getToX() {
        return toX;
    }

    public void setToX(int toX) {
        this.toX = toX;
    }

    public int getToY() {
        return toY;
    }

    public void setToY(int toY) {
        this.toY = toY;
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
