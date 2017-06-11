package visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import outputFile.Output;
import outputFile.OutputEvent;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Arne on 30.05.2017.
 */
public class SimulationGui extends Application {
    /**
     * Waiting time for the running simulation.
     */
    public static final int MILLIS = 5;

    // Default Sizes of Panels
    public static final int MENUSIZE = 75;
    public static final int STARTSIZE = 400;
    public static final int INFOSIZE = 250;

    // Colors
    public static final Color GOALCOLOR = Color.GREEN;
    public static final Color WALLCOLOR = Color.BLACK;
    public static final Color PERSONCOLOR = Color.PINK;

    /**
     * Value of a wall in the distance map.
     */
    public static final double WALLVALUE = -1.7976931348623157E308;

    /**
     * Minimal size of a cell.
     */
    private final int MINCELLSIZE = 4;

    /**
     * Maximal size of a cell.
     */
    private final int MAXCELLSIZE = 30;

    /**
     * Size of one cell.
     */
    private int cellsize = 4;

    /**
     * Defines how fast the simulation will be run.
     */
    private double speed = 1.0;
    /**
     * List of all simulated persons.
     */
    private List<SimulatedPerson> personList = new ArrayList<>();

    // Helping variables
    private int step = 0;
    private boolean running = false;
    private boolean heatmap = false;
    private boolean enableScrollbar = false;
    private long waitingtime = 0;

    // Stages
    Stage primaryStage;
    Stage canvasStage = new Stage();
    Stage infoStage = new Stage();

    // Panes
    private Pane menu = new Pane();
    private Pane canvas = new Pane();
    private Pane info = new Pane();

    // Scrollbar
    private ScrollBar scrollBarV = new ScrollBar();
    private ScrollBar scrollBarH = new ScrollBar();
    private double scrollBarSize = scrollBarV.getWidth();

    // Silder
    Slider slider = new Slider();
    Label sliderLabel;

    // Labels
    private Label stepLabel;
    private Label timeLabel;
    private Label algorithmLabel;
    private Label personLabel;
    private Label simulationTimeLabel;

    // Caption
    private Label cellSizeInfo;
    private Label infoPerson;
    private Label infoDestination;
    private Label infoWall;
    private Canvas captionColors;

    // Layers of the visualisation
    private Canvas cellLayer;
    private Canvas heatLayer;
    private Canvas objectLayer;

    // Buttons
    private Button proceed;
    private Button reset;
    private Button play;
    private Button changeLayer;
    private Button loadXML;

    /**
     * For accessing the screen info.
     */
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    private static final boolean DEBUG = false;

    public static Output input = null;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads a simulation from a file.
     * @param file
     * @throws JAXBException
     * @throws IOException
     */
    private void loadInput(File file) throws JAXBException, IOException{
        JAXBContext jaxbContext = JAXBContext.newInstance(Output.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        FileReader reader = new FileReader(file);
        input = (Output) unmarshaller.unmarshal(reader);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visualisierung");

        createButtons();
        createSlider();

        primaryStage.setScene(new Scene(menu, STARTSIZE, MENUSIZE));

        primaryStage.setOnCloseRequest(event -> {
            running = false;
            canvasStage.close();
            infoStage.close();
        });

        primaryStage.setY(screenBounds.getHeight()*0.2);
        primaryStage.setX((screenBounds.getWidth()-STARTSIZE)/2);
        primaryStage.show();

        canvasStage.setScene(new Scene(canvas, STARTSIZE,5));
        canvasStage.setX(primaryStage.getX());
        canvasStage.setY(primaryStage.getY() + primaryStage.getHeight());
        canvasStage.show();

        infoStage.setScene(new Scene(info, INFOSIZE, canvasStage.getScene().getHeight()));
        infoStage.setX(canvasStage.getX()+canvasStage.getWidth());
        infoStage.setY(canvasStage.getY());
        infoStage.show();
    }

    /**
     * Creates the canvas for the simulation.
     */
    private void createGrid(){
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
    }

    /**
     * Configures the slider.
     */
    private void createSlider(){
        slider.setMin(1);
        slider.setMax(19);
        slider.setValue(10);
        slider.setLayoutX(150);
        slider.setLayoutY(14);
        slider.setMaxWidth(100);
        slider.valueProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue.intValue() < 10){
                speed = newValue.intValue()/10.0;
            } else {
                speed = newValue.intValue() - 9;
            }
            sliderLabel.setText("Speed: " + speed + "x");
        });

        sliderLabel = new Label("Speed: 1x");
        sliderLabel.setLayoutX(275);
        sliderLabel.setLayoutY(14);

        menu.getChildren().add(slider);
        menu.getChildren().add(sliderLabel);
    }

    /**
     * Configures the scrollbar.
     */
    private void configureScrollbar(){
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
     * Sets the size of a cell depending on the screen size and the field bounds.
     */
    private void setCellSize(){
        double x = screenBounds.getWidth() - info.getWidth();
        double y = screenBounds.getHeight() - 50;
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
     * Helping method for drawing Canvas -- currently not used.
     * @param canvas
     * @param x
     * @param y
     */
    private void moveCanvas(Canvas canvas, int x, int y) {
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    /**
     * Draws the cells of the cellLayer.
     */
    public void drawCells(){
        GraphicsContext gc = cellLayer.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for(int y = 0; y < input.getFieldHeight(); y++) {
            for (int x = 0; x < input.getFieldWidth(); x++) {
                gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
                gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
            }
        }
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
        for(int y = 0; y < input.getFieldHeight(); y++) {
            String[] row = rows[y].split(" ");
            for (int x = 0; x < input.getFieldWidth(); x++) {
                double d = Double.parseDouble(row[x]);
                if (d == WALLVALUE){
                    continue;
                }
                int val = (int)((1-d/min)*255);
                gc.setFill(Color.rgb(val,val,255-val));
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
                if (x == input.getTargetX() && y == input.getTargetY()) {
                    gc.setFill(GOALCOLOR);
                    gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
                    gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
                } else if (c == ' '){
                    gc.setFill(WALLCOLOR);
                    gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
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
        gc.fillRect(input.getTargetX()* cellsize, input.getTargetY()* cellsize, cellsize, cellsize);
        gc.strokeRect(input.getTargetX()* cellsize, input.getTargetY()* cellsize, cellsize, cellsize);
        step = 0;
        stepLabel.setText("Current step: " + step);
        timeLabel.setText("Current Time: 0");
        personLabel.setText("People in the Simulation: 0");
    }

    /**
     * Helping method for creating the info panel.
     */
    private void createInfo(){
        info = new Pane();
        double height = Math.max(canvas.getHeight(), INFOSIZE);
        createLabels();
        createCaption(height);
        infoStage.setScene(new Scene(info, INFOSIZE, height));
        infoStage.setX(canvasStage.getX() + canvasStage.getWidth());
        infoStage.show();
    }

    /**
     * Helping method for creating the caption for the info panel.
     * @param height of the infoStage
     */
    private void createCaption(double height){
        cellSizeInfo = new Label("Cellsize: " + input.getCellsize());
        cellSizeInfo.setLayoutX(10);
        cellSizeInfo.setLayoutY(height - 76);

        infoPerson = new Label("Person");
        infoPerson.setLayoutX(25);
        infoPerson.setLayoutY(height - 58);

        infoDestination = new Label("Destination");
        infoDestination.setLayoutX(25);
        infoDestination.setLayoutY(height - 40);

        infoWall = new Label("Wall/Obstacle");
        infoWall.setLayoutX(25);
        infoWall.setLayoutY(height - 22);

        captionColors = new Canvas(10,46);
        captionColors.setLayoutX(10);
        captionColors.setLayoutY(height - 55);
        GraphicsContext gc = captionColors.getGraphicsContext2D();

        gc.setFill(PERSONCOLOR);
        gc.fillRect(0, 0, 10, 10);

        gc.setFill(GOALCOLOR);
        gc.fillRect(0, 18, 10, 10);

        gc.setFill(WALLCOLOR);
        gc.fillRect(0, 36, 10, 10);

        info.getChildren().add(cellSizeInfo);
        info.getChildren().add(infoPerson);
        info.getChildren().add(infoDestination);
        info.getChildren().add(infoWall);
        info.getChildren().add(captionColors);
    }

    /**
     * Helping method for creating the labels.
     */
    public void createLabels(){
        algorithmLabel = new Label("Algorithm type: " + input.getAlgorithm());
        algorithmLabel.setLayoutX(10);
        algorithmLabel.setLayoutY(14);

        stepLabel = new Label("Current step: " + step);
        stepLabel.setLayoutX(10);
        stepLabel.setLayoutY(32);

        timeLabel = new Label("Current Time: 0");
        timeLabel.setLayoutX(10);
        timeLabel.setLayoutY(50);

        simulationTimeLabel = new Label("Simulationtime: " + input.getEvents().stream().map(outputEvent -> outputEvent.getTime()).max(BigDecimal::compareTo).get());
        simulationTimeLabel.setLayoutX(10);
        simulationTimeLabel.setLayoutY(68);

        personLabel = new Label("People in the Simulation: 0");
        personLabel.setLayoutX(10);
        personLabel.setLayoutY(86);

        info.getChildren().add(algorithmLabel);
        info.getChildren().add(stepLabel);
        info.getChildren().add(timeLabel);
        info.getChildren().add(simulationTimeLabel);
        info.getChildren().add(personLabel);
    }

    /**
     * Helping method for creating the buttons.
     */
    public void createButtons(){
        // Proceed button
        proceed = new Button("Next Step");
        proceed.setLayoutX(25);
        proceed.setLayoutY(12);

        proceed.setOnAction(event -> {
            if (step < input.getEvents().size()) {
                handleNextEvent();
            }
            if (step >= input.getEvents().size()){
                stepLabel.setText("Finished");
            }
        });

        // Reset button
        reset = new Button("Reset");
        reset.setLayoutX(75);
        reset.setLayoutY(42);

        reset.setOnAction(event -> {
            reset();
        });

        // Play button
        play = new Button("Play");
        play.setLayoutX(25);
        play.setLayoutY(42);

        play.setOnAction((ActionEvent event) -> {
            play();
        });

        // Change layer button
        changeLayer = new Button("Heatmap");
        changeLayer.setLayoutX(150);
        changeLayer.setLayoutY(42);

        changeLayer.setOnAction(event -> {
            if (heatmap){
                heatmap = false;
                changeLayer.setText("Heatmap");
                heatLayer.toBack();
            } else {
                heatmap = true;
                changeLayer.setText("Cellmap");
                cellLayer.toBack();
            }
        });

        // Load simulation button
        loadXML = new Button("Load Simulation");
        loadXML.setLayoutX(250);
        loadXML.setLayoutY(42);
        loadXML.setOnAction(event -> {
            loadXmlMethod();
        });

        // Add the buttons to the pane
        menu.getChildren().add(proceed);
        menu.getChildren().add(reset);
        menu.getChildren().add(play);
        menu.getChildren().add(changeLayer);
        menu.getChildren().add(loadXML);

        // At the beginning there is no simulation selected. Therefore all buttons will be disabled.
        play.setDisable(true);
        proceed.setDisable(true);
        reset.setDisable(true);
        slider.setDisable(true);
        changeLayer.setDisable(true);
    }

    /**
     * Method of the load xml button. Reads the simulation out of a xml file. Closes the opened simulation.
     */
    private void loadXmlMethod(){
        // Stopps the running of a simulation.
        running = false;
        play.setText("Play");
        proceed.setDisable(false);

        // Chooser for the file to load
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);

        int result = chooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION){
            return;
        }
        File file = chooser.getSelectedFile();
        try {
            loadInput(file);
        } catch (JAXBException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incompatible data format");
            alert.setHeaderText(null);
            alert.setContentText("The read xml does not contain the structure needed to be processed!");
            alert.showAndWait();
            return;
        } catch (IOException e){
            e.printStackTrace();
        }
        // Resets and initializes the simulation
        canvas.getChildren().remove(cellLayer);
        canvas.getChildren().remove(heatLayer);
        canvas.getChildren().remove(objectLayer);

        play.setDisable(false);
        proceed.setDisable(false);
        reset.setDisable(false);
        slider.setDisable(false);
        changeLayer.setDisable(false);

        setCellSize();
        createGrid();
        createInfo();
        reset();

        if(heatmap){
            cellLayer.toBack();
        } else {
            heatLayer.toBack();
        }
        primaryStage.setTitle("Visualisierung für: " + input.getName());
    }

    /**
     * The method of the play button. Pressed again the running of the simulation will be stopped.
     */
    private void play(){
        if (running){
            running = false;
            play.setText("Play");
        } else {
            play.setText("Pause");
            running = true;
            proceed.setDisable(true);

            Task task = new Task<Void>() {
                @Override
                public Void call() {
                    while (running && step < input.getEvents().size()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                handleNextEvent();
                                if (step+1 < input.getEvents().size()) {
                                    BigDecimal a = input.getEvents().get(step).getTime();
                                    BigDecimal b = input.getEvents().get(step -1).getTime();
                                    waitingtime = a.subtract(b).multiply(new BigDecimal(1000)).longValue();
                                } else {
                                    waitingtime = 0;
                                }
                            }
                        });
                        if (DEBUG) {
                            System.out.println(waitingtime);
                        }
                        try {
                            Thread.sleep(Math.max(MILLIS,(long)(waitingtime/speed)));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    running = false;
                    proceed.setDisable(false);
                    if (step >= input.getEvents().size()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stepLabel.setText("Finished");
                                play.setText("Play");
                            }
                        });
                    }
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    /**
     * Helping method for creating the layers.
     */
    public void createLayers(){
        cellLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        heatLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        objectLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());

        // Drawing the layers
        drawCells();
        drawHeatMap();
        drawObjects();

        canvas.getChildren().add(cellLayer);
        canvas.getChildren().add(heatLayer);
        canvas.getChildren().add(objectLayer);

        // Add Scrollbars if necessary
        if (enableScrollbar) {
            canvas.getChildren().add(scrollBarV);
            canvas.getChildren().add(scrollBarH);
        }

        // Setting display order
        heatLayer.toBack();
        objectLayer.toFront();
    }

    /**
     * This methods handles the next event in the eventlist of the input. Persons will be created, moved and removed here.
     */
    public void handleNextEvent(){
        OutputEvent next = input.getEvents().get(step);
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
                personLabel.setText("People in the Simulation: " + personList.size());
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
                personLabel.setText("People in the Simulation: " + personList.size());
                break;
            default:
                throw new AssertionError("Invalid Event Type: " + next.getType());
        }

        if (DEBUG) {
            System.out.println(next);
            System.out.println(personList);
        }

        timeLabel.setText("Current Time: " + next.getTime());
        stepLabel.setText("Current step: " + ++step);
    }

    /**
     * Method that adds a person to the field.
     * @param person
     */
    private void addPersonToField(SimulatedPerson person){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.setFill(PERSONCOLOR);
        gc.setStroke(Color.BLACK);
        gc.fillRect(person.getX()* cellsize +1, person.getY()* cellsize +1, cellsize -2, cellsize -2);
    }

    /**
     * Method that removes a person from the field.
     * @param person
     */
    private void removePersonFromField(SimulatedPerson person){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.clearRect(person.getX()* cellsize +1,person.getY()* cellsize +1, cellsize -2, cellsize -2);
    }
}
