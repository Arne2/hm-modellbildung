package visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import outputFile.Output;
import outputFile.OutputEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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

    /**
     * Size of the menu.
     */
    public static final int MENUSIZE = 75;

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
     * List of all simulated persons.
     */
    private List<SimulatedPerson> personList = new ArrayList<>();

    // Helping variables
    private int step = 0;
    private boolean running = false;
    private boolean heatmap = false;
    private boolean enableScrollbar = false;
    private long waitingtime = 0;

    // Panes
    private Pane root = new Pane();
    private Pane canvas = new Pane();

    // Scrollbar
    private ScrollBar sc = new ScrollBar();

    // Labels
    private Label stepLabel;
    private Label timeLabel;

    // Layers of the visualisation
    private Canvas cellLayer;
    private Canvas heatLayer;
    private Canvas objectLayer;

    // Buttons
    private Button proceed;
    private Button reset;
    private Button play;
    private Button changeLayer;

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

    private static final boolean DEBUG = true;

    public static Output input = null;

    public static void main(String[] args) {
        // Reading the xml file
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Output.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            FileReader reader = new FileReader("output.xml");
            input = (Output) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Visualisierung für: " + input.getName());

        setCellSize();

        createLabels();
        createButtons();
        createLayers();

        int width = Math.max(cellsize * input.getFieldWidth(), 500)+(int)sc.getWidth();
        int height = Math.min(Math.max(cellsize * input.getFieldHeight() + MENUSIZE, 500),(int)screenBounds.getHeight()-50);

        // Add all elements to the pane
        addToRoot();

        heatLayer.toBack();
        objectLayer.toFront();

        primaryStage.setScene(new Scene(root, width, height));

        if (enableScrollbar) {
//            configureScrollbar(primaryStage);
        }

        primaryStage.show();
    }

    /**
     * Adds all components to the root pane.
     */
    public void addToRoot(){
        root.getChildren().add(stepLabel);
        root.getChildren().add(timeLabel);
        root.getChildren().add(proceed);
        root.getChildren().add(reset);
        root.getChildren().add(play);
        root.getChildren().add(changeLayer);
        root.getChildren().add(cellLayer);
        root.getChildren().add(heatLayer);
        root.getChildren().add(objectLayer);

//        canvas.getChildren().add(cellLayer);
//        canvas.getChildren().add(heatLayer);
//        canvas.getChildren().add(objectLayer);
//        root.getChildren().add(canvas);
        if (enableScrollbar) {
//            root.getChildren().add(sc);
        }
    }

    /**
     * Configures the scrollbar.
     * @param primaryStage
     */
    private void configureScrollbar(Stage primaryStage){
        sc.setLayoutX(primaryStage.getScene().getWidth()-sc.getWidth());
        sc.setLayoutY(MENUSIZE);
        sc.setMin(0);
        sc.setOrientation(Orientation.VERTICAL);
        sc.setPrefHeight(180);
        sc.setMax(360);
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                canvas.setLayoutY(-new_val.doubleValue());
            }
        });
    }

    /**
     * Sets the size of a cell depending on the screen size and the field bounds.
     */
    private void setCellSize(){
        double x = screenBounds.getWidth();
        double y = screenBounds.getHeight()-50;
        int width = input.getFieldWidth();
        int height = input.getFieldHeight();

        cellsize = (int)Math.min(x/width,(y-MENUSIZE)/height);
        cellsize = Math.min(MAXCELLSIZE, cellsize);
        if (cellsize< MINCELLSIZE){
            enableScrollbar = true;
        }
        cellsize = Math.max(MINCELLSIZE, cellsize);
        System.out.println(cellsize);
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
        //TODO heatmap
        GraphicsContext gc = heatLayer.getGraphicsContext2D();
        LinearGradient lg = new LinearGradient(0, 0, 1, 1, true,
                CycleMethod.REFLECT,
                new Stop(0.0, Color.YELLOW),
                new Stop(1.0, Color.RED));
        gc.setFill(lg);
        gc.setLineWidth(1);
        gc.stroke();
        gc.fillRect(0,0, heatLayer.getWidth(), heatLayer.getHeight());
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
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x* cellsize, y* cellsize, cellsize, cellsize);
                    gc.strokeRect(x* cellsize, y* cellsize, cellsize, cellsize);
                } else if (c == ' '){
                    gc.setFill(Color.BLACK);
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
    }

    /**
     * Helping method for creating the labels.
     */
    public void createLabels(){
        stepLabel = new Label("Current step: " + step);
        stepLabel.setLayoutX(25);
        stepLabel.setLayoutY(14);

        timeLabel = new Label("Current Time: 0");
        timeLabel.setLayoutX(250);
        timeLabel.setLayoutY(14);
    }

    /**
     * Helping method for creating the buttons.
     */
    public void createButtons(){
        proceed = new Button("Next Step");
        proceed.setLayoutX(150);
        proceed.setLayoutY(12);

        proceed.setOnAction(event -> {
            if (step < input.getEvents().size()) {
                handleNextEvent();
            }
            if (step >= input.getEvents().size()){
                stepLabel.setText("Finished");
            }
        });

        reset = new Button("Reset");
        reset.setLayoutX(150);
        reset.setLayoutY(42);

        reset.setOnAction(event -> {
            reset();
        });

        play = new Button("Play");
        play.setLayoutX(25);
        play.setLayoutY(42);

        play.setOnAction((ActionEvent event) -> {
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
                                    Thread.sleep(Math.max(MILLIS,waitingtime));
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

        });

        changeLayer = new Button("Heatmap");
        changeLayer.setLayoutX(250);
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
    }

    /**
     * Helping method for creating the layers.
     */
    public void createLayers(){
        cellLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        cellLayer.setLayoutY(MENUSIZE);

        heatLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        heatLayer.setLayoutY(MENUSIZE);

        objectLayer = new Canvas(cellsize *input.getFieldWidth(), cellsize *input.getFieldHeight());
        objectLayer.setLayoutY(MENUSIZE);

        drawCells();
        drawHeatMap();
        drawObjects();
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

        timeLabel.setText("Current Time: " + next.getTime());
        stepLabel.setText("Current step: " + ++step);
    }

    /**
     * Method that adds a person to the field.
     * @param person
     */
    private void addPersonToField(SimulatedPerson person){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.setFill(Color.ORANGE);
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
