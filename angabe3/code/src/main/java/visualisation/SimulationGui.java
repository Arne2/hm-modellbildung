package visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
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
     * Size of one cell.
     */
    public static final int CELLSIZE = 2;

    /**
     * List of all simulated persons.
     */
    private List<SimulatedPerson> personList = new ArrayList<>();

    // Helping variables
    private int step = 0;
    private boolean running = false;
    private boolean heatmap = false;
    private long waitingtime = 0;

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

        createLabels();
        createButtons();
        createLayers();

        // Add all elements to the pane
        Pane root = new Pane();
        root.getChildren().add(stepLabel);
        root.getChildren().add(timeLabel);
        root.getChildren().add(proceed);
        root.getChildren().add(reset);
        root.getChildren().add(play);
        root.getChildren().add(changeLayer);
        root.getChildren().add(cellLayer);
        root.getChildren().add(heatLayer);
        root.getChildren().add(objectLayer);

        heatLayer.toBack();
        objectLayer.toFront();

        primaryStage.setScene(new Scene(root, CELLSIZE *input.getFieldWidth(), CELLSIZE*input.getFieldHeight() + 75));
        primaryStage.show();
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
                gc.fillRect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
                gc.strokeRect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
            }
        }
    }

    /**
     * Draws the heatmap which visualizes the value of each location.
     */
    public void drawHeatMap(){
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
                    gc.fillRect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
                    gc.strokeRect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
                } else if (c == ' '){
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x*CELLSIZE, y*CELLSIZE, CELLSIZE, CELLSIZE);
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
        gc.fillRect(input.getTargetX()*CELLSIZE, input.getTargetY()*CELLSIZE, CELLSIZE, CELLSIZE);
        gc.strokeRect(input.getTargetX()*CELLSIZE, input.getTargetY()*CELLSIZE, CELLSIZE, CELLSIZE);
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
        cellLayer = new Canvas(CELLSIZE*input.getFieldWidth(), CELLSIZE*input.getFieldHeight());
        cellLayer.setLayoutY(75);

        heatLayer = new Canvas(CELLSIZE*input.getFieldWidth(), CELLSIZE*input.getFieldHeight());
        heatLayer.setLayoutY(75);

        objectLayer = new Canvas(CELLSIZE*input.getFieldWidth(), CELLSIZE*input.getFieldHeight());
        objectLayer.setLayoutY(75);

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
        gc.fillRect(person.getX()*CELLSIZE+1, person.getY()*CELLSIZE+1, CELLSIZE-2, CELLSIZE-2);
    }

    /**
     * Method that removes a person from the field.
     * @param person
     */
    private void removePersonFromField(SimulatedPerson person){
        GraphicsContext gc = objectLayer.getGraphicsContext2D();
        gc.clearRect(person.getX()*CELLSIZE+1,person.getY()*CELLSIZE+1, CELLSIZE-2, CELLSIZE-2);
    }
}
