package visualisation.draw;

import field.location.Location;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import outputFile.Output;
import outputFile.OutputEvent;
import visualisation.SimulatedPerson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static visualisation.GuiConfiguration.*;

/**
 * Created by Arne on 16.06.2017.
 * Class responsible for drawing the canvas.
 */
public class CanvasDrawer {

    private Pane canvas = new Pane();

    /**
     * List of all simulated persons.
     */
    private List<SimulatedPerson> personList = new ArrayList<>();

    /**
     * Size of one cell.
     */
    private int cellsize = 4;

    // Layers of the visualisation
    private Canvas cellLayer;
    private Canvas heatLayer;
    private Canvas objectLayer;
    private Canvas measurementLayer;

    // Scrollbar
    private boolean enableScrollbar = false;
    private ScrollBar scrollBarV = new ScrollBar();
    private ScrollBar scrollBarH = new ScrollBar();
    private double scrollBarSize = scrollBarV.getWidth();

    // Object Variables
    private Stage primaryStage;
    private Stage canvasStage;
    private Stage infoStage;
    private Output input;

    private int step = 0;
    private double distancemin = 0.0;
    private OutputEvent next;

    public CanvasDrawer(Stage primaryStage, Stage canvasStage, Stage infoStage) {
        this.primaryStage = primaryStage;
        this.canvasStage = canvasStage;
        this.infoStage = infoStage;
        canvasStage.setScene(new Scene(canvas, STARTSIZE,5));
    }

    /**
     * Configures the scrollbar.
     */
    public void configureScrollbar(){
        // Vertical ScrollBar
        scrollBarV.setLayoutX(canvasStage.getScene().getWidth() - scrollBarSize);
        scrollBarV.setMax(cellLayer.getHeight() - canvas.getHeight() + scrollBarSize);
        scrollBarV.setOrientation(Orientation.VERTICAL);
        scrollBarV.setPrefHeight(canvasStage.getScene().getHeight() - scrollBarSize + 6);
        scrollBarV.valueProperty().addListener((ov, old_val, new_val) -> {
            cellLayer.setLayoutY(-new_val.doubleValue());
            heatLayer.setLayoutY(-new_val.doubleValue());
            objectLayer.setLayoutY(-new_val.doubleValue());
        });

        // Horizontal ScrollBar
        scrollBarH.setLayoutY(canvasStage.getScene().getHeight()- scrollBarSize + 6);
        scrollBarH.setMax(cellLayer.getWidth() - canvas.getWidth());
        scrollBarH.setOrientation(Orientation.HORIZONTAL);
        scrollBarH.setPrefWidth(canvasStage.getScene().getWidth() - scrollBarSize);
        scrollBarH.toFront();
        scrollBarH.valueProperty().addListener((ov, old_val, new_val) -> {
            cellLayer.setLayoutX(-new_val.doubleValue());
            heatLayer.setLayoutX(-new_val.doubleValue());
            objectLayer.setLayoutX(-new_val.doubleValue());
        });
    }

    /**
     * Creates the canvas for the simulation.
     */
    public void createGrid(){
        createLayers();

        int width = Math.min(cellsize * input.getFieldWidth(), (int)(screenBounds.getWidth()*0.9 - infoStage.getWidth())) + (enableScrollbar?(int) scrollBarSize :0);
        int height = Math.min(cellsize * input.getFieldHeight(),(int)(screenBounds.getHeight()*0.7 - primaryStage.getHeight()))+(enableScrollbar?(int) scrollBarSize :0);

        ObservableList<Node> canvasChildren = canvas.getChildren();
        canvas = new Pane();
        canvas.getChildren().addAll(canvasChildren);
        canvasStage.setScene(new Scene(canvas, width, height));
        canvasStage.setX((screenBounds.getWidth() - infoStage.getWidth() - canvasStage.getWidth())/2);
        canvasStage.show();

        if (enableScrollbar) {
            configureScrollbar();
        }

        canvasStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                configureScrollbar();
            }
        });


        canvasStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                configureScrollbar();
            }
        });


    }

    /**
     * Draws the heatmap which visualizes the value of each location.
     */
    public void drawHeatMap(){
        String heatmap = input.getDistanceMap();
        String[] rows = heatmap.split("\n");
        GraphicsContext gc = heatLayer.getGraphicsContext2D();
        double min = Double.MIN_VALUE;
        for(int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split(" ");
            for (int j = 0; j < row.length; j++) {
                double d = Double.parseDouble(row[j]);
                if (d == WALLVALUE){
                    continue;
                }
                min = Math.min(d, min);
            }
        }
        distancemin = min;
        for(int y = 0; y < input.getFieldHeight(); y++) {
            String[] row = rows[y].split(" ");
            for (int x = 0; x < input.getFieldWidth(); x++) {
                double d = Double.parseDouble(row[x]);
                if (d == WALLVALUE){
                    continue;
                }
                gc.setFill(getHeatColor(d,distancemin));
                gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
            }
        }
    }

    /**
     * Draws objects of the objectLayer, which include target and walls.
     * Persons will be added during the simulation.
     */
    public void drawObjects(){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        String map = input.getFieldmap();
        String[] rows = map.split("\n");
        for(int y = 0; y < input.getFieldHeight(); y++) {
            for (int x = 0; x < input.getFieldWidth(); x++) {
                char c = rows[y].charAt(x);
                if(input.getTargets() == null) continue;
                if ( input.getTargets().contains(Location.of(x, y))) {
                    gc.setFill(GOALCOLOR);
                    gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
                    gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
                } else if (c == ' '){
                    // The reason why the walls are drawn in a runLater block is to prevent a bug,
                    // in which some walls are not drawn when a second input is loaded.
                    final int x2 = x;
                    final int y2 = y;
                    Platform.runLater(() -> {
                        gc.setFill(WALLCOLOR);
                        gc.fillRect(x2* cellsize, y2* cellsize, cellsize, cellsize);
                    });
                }
            }
        }
    }

    /**
     * Sets the simulation to the beginning.
     */
    public void reset(){
        personList.stream().forEach(simulatedPerson -> removePersonFromField(simulatedPerson));
        personList.clear();
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLACK);

        for (Location target: input.getTargets()) {
            gc.fillRect(target.x * cellsize, target.y * cellsize, cellsize, cellsize);
            gc.strokeRect(target.x* cellsize, target.y* cellsize, cellsize, cellsize);
        }

        step = 0;
        proceedToStart();
    }

    /**
     * Handles all Events that occur at the time zero.
     */
    private void proceedToStart(){
        if(input.getEvents() == null)
            return;
        while (input.getEvents().size() > step && input.getEvents().get(step+1).getTime().equals(BigDecimal.ZERO)){
            handleNextEvent();
        }
    }

    /**
     * This methods handles the next event in the eventlist of the input. Persons will be created, moved and removed here.
     */
    public void handleNextEvent(){
        next = input.getEvents().get(step);
        SimulatedPerson person;
        Optional<SimulatedPerson> p;
        switch (next.getType()){
            case "pawn" :
                person = new SimulatedPerson(next.getPersonID(), next.getPositionX(), next.getPositionY());
                if(personList.contains(person)){
                    throw new AssertionError("Pawn Event with an ID that's already taken!");
                }
                personList.add(person);
                addPersonToField(person);
                break;
            case "move" :
                p = personList.stream().filter(simulatedPerson -> simulatedPerson.getId() == next.getPersonID()).findAny();
                if (!p.isPresent()){
                    throw new AssertionError("Person not in personList");
                }
                person = p.get();
                removePersonFromField(person);
                person.setX(next.getPositionX());
                person.setY(next.getPositionY());
                addPersonToField(person);
                break;
            case "remove":
                p = personList.stream().filter(simulatedPerson -> simulatedPerson.getId() == next.getPersonID()).findAny();
                if (!p.isPresent()){
                    throw new AssertionError("Person not in personList");
                }
                person = p.get();
                removePersonFromField(person);
                personList.remove(person);
                break;
            default:
                throw new AssertionError("Invalid Event Type: " + next.getType());
        }

        if (DEBUG) {
            System.out.println(next);
            System.out.println(personList);
        }
        step++;
    }

    /**
     * Sets the size of a cell depending on the screen size and the field bounds.
     */
    public void setCellSize(){
        double x = screenBounds.getWidth()*0.9 - infoStage.getWidth();
        double y = screenBounds.getHeight()*0.7 - primaryStage.getHeight();
        int width = input.getFieldWidth();
        int height = input.getFieldHeight();

        cellsize = (int)Math.min(x/width,(y-MENUSIZE)/height);
        cellsize = Math.min(MAXCELLSIZE, cellsize);
        if (cellsize< MINCELLSIZE){
            enableScrollbar = true;
        }
        cellsize = Math.max(MINCELLSIZE, cellsize);
    }

    /**
     * Draws the cells of the cellLayer.
     */
    public void drawCells(){
        GraphicsContext gc = cellLayer.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        for(int y = 0; y < input.getFieldHeight(); y++) {
            for (int x = 0; x < input.getFieldWidth(); x++) {
                gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
                gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
            }
        }
    }

    /**
     * Helping method for creating the layers.
     */
    public void createLayers(){
        cellLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        heatLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());

        objectLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        measurementLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());

        // Drawing the layers
        drawCells();
        drawHeatMap();
        drawObjects();
        drawMeasurement();

        canvas.getChildren().add(cellLayer);
        canvas.getChildren().add(heatLayer);
        canvas.getChildren().add(objectLayer);
        canvas.getChildren().add(measurementLayer);

        // Add Scrollbars if necessary
        if (enableScrollbar) {
            canvas.getChildren().add(scrollBarV);
            canvas.getChildren().add(scrollBarH);
        }

        // Setting display order
        heatLayer.toBack();
        measurementLayer.toFront();
        objectLayer.toFront();
    }

    /**
     * Remove all Layers from the canvas.
     */
    public void removeLayers(){
        canvas.getChildren().remove(cellLayer);
        canvas.getChildren().remove(heatLayer);
        canvas.getChildren().remove(objectLayer);
        canvas.getChildren().remove(measurementLayer);
    }

    /**
     * Draws the measurement box in the canvas.
     */
    public void drawMeasurement(){
        GraphicsContext gc = measurementLayer.getGraphicsContext2D();
        gc.setStroke(MEASUREMENTCOLOR);
        gc.setLineWidth(3);
        if (DEBUG) {
            System.out.println("Measurement from: " + input.getFromX() + ", " + input.getFromY() + " to: " + input.getToX() + ", " + input.getToY());
        }
        gc.strokeRect(input.getFromX() * cellsize, input.getFromY() * cellsize, (input.getToX() - input.getFromX()) * cellsize, (input.getToY() - input.getFromY()) * cellsize);
    }

    /**
     * Helping method for creating the heatmap.
     * @param d
     * @param min
     * @return the color of the a cell.
     */
    public Color getHeatColor(double d, double min){
        return Color.hsb(((1+d/min) * 360), 1, 1, 1);
    }

    /**
     * Method that adds a person to the field.
     * @param person
     */
    public void addPersonToField(SimulatedPerson person){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.setFill(PERSONCOLOR);
        gc.setStroke(Color.BLACK);
        gc.fillRect(person.getX()* cellsize +1, person.getY()* cellsize +1, cellsize -2, cellsize -2);
    }

    /**
     * Method that removes a person from the field.
     * @param person
     */
    public void removePersonFromField(SimulatedPerson person){
        long x = person.getX();
        long y = person.getY();
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.clearRect(x* cellsize +1,y* cellsize +1, cellsize -2, cellsize -2);
        if(input.getTargets().contains(Location.of((int)x, (int)y))){
            gc.setFill(GOALCOLOR);
            gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
            gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
        }
    }

    public Canvas getCellLayer() {
        return cellLayer;
    }

    public Canvas getHeatLayer() {
        return heatLayer;
    }

    public Canvas getObjectLayer() {
        return objectLayer;
    }

    public Canvas getMeasurementLayer() {
        return measurementLayer;
    }

    public Pane getCanvas() {
        return canvas;
    }

    public void setInput(Output input) {
        this.input = input;
    }

    public int getStep() {
        return step;
    }

    public OutputEvent getNext() {
        return next;
    }

    public double getDistancemin() {
        return distancemin;
    }

    public List<SimulatedPerson> getPersonList() {
        return personList;
    }
}
