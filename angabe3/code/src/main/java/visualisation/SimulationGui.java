package visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import outputFile.Output;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static visualisation.GuiConfiguration.*;

/**
 * Created by Arne on 30.05.2017.
 */
public class SimulationGui extends Application {

    /**
     * Defines how fast the simulation will be run.
     */
    private double speed = 1.0;

    // Helping variables
    private boolean running = false;
    private boolean heatmap = false;
    private long waitingtime = 0;
    private File inputFile ;

    // Stages
    Stage primaryStage;
    Stage canvasStage = new Stage();
    Stage infoStage = new Stage();

    // Panes
    private Pane menu = new Pane();
    private Pane info = new Pane();

    // Silder
    Slider slider = new Slider();
    Label sliderLabel;

    // Labels
    private Label stepLabel;
    private Label timeLabel;
    private Label algorithmLabel;
    private Label velocityLabel;
    private Label deviationLabel;
    private Label personLabel;
    private Label simulationTimeLabel;

    // Caption
    private Label cellSizeInfo;
    private Label infoPerson;
    private Label infoDestination;
    private Label infoWall;
    private Label heatmapLabel;
    private Label heatmapLabel1;
    private Label heatmapLabel2;
    private Label heatmapLabel3;
    private Canvas captionColors;
    private Canvas captionHeatMap;

    // Buttons
    private Button proceed;
    private Button reset;
    private Button play;
    private Button changeLayer;
    private Button loadXML;
    private Button snapshot;

    /**
     * Helping class for drawing the canvas.
     */
    CanvasDrawer canvasDrawer;

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
        inputFile = file;
        JAXBContext jaxbContext = JAXBContext.newInstance(Output.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        FileReader reader = new FileReader(file);
        input = (Output) unmarshaller.unmarshal(reader);
        canvasDrawer.setInput(input);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visualisierung");
        canvasDrawer = new CanvasDrawer(primaryStage, canvasStage, infoStage);

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

//        canvasStage.setScene(new Scene(canvas, STARTSIZE,5));
        canvasStage.setX(primaryStage.getX());
        canvasStage.setY(primaryStage.getY() + primaryStage.getHeight());
        canvasStage.show();

        infoStage.setScene(new Scene(info, INFOSIZE, canvasStage.getScene().getHeight()));
        infoStage.setX(canvasStage.getX()+canvasStage.getWidth());
        infoStage.setY(canvasStage.getY());
        infoStage.show();
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
     * Creates a png file of the current canvas.
     * @throws IOException
     * @throws URISyntaxException
     */
    public void saveSnapshot() throws IOException, URISyntaxException {
        String path = inputFile.getParent() + "/snapshot_" + LocalTime.now().toString().replace(":","_") + ".png";
        File snapshot = new File (path);
        if (!snapshot.exists()){
            Files.createDirectories(Paths.get(inputFile.getParent()));
            Files.createFile(Paths.get(path));
        }
        WritableImage writableImage = new WritableImage(((int) canvasDrawer.getCanvas().getWidth()), ((int) canvasDrawer.getCanvas().getHeight()));
        canvasDrawer.getCanvas().snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(renderedImage, "png", snapshot);
    }

    /**
     * Helping method for creating the info panel.
     */
    private void createInfo(){
        info = new Pane();
        double height = Math.max(canvasDrawer.getCanvas().getHeight(), INFOSIZE);
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
        // Caption Heatmap
        heatmapLabel = new Label("Heatmap Colors:");
        heatmapLabel.setLayoutX(10);
        heatmapLabel.setLayoutY(height - 135);

        heatmapLabel1 = new Label("0.0m");
        heatmapLabel1.setLayoutX(28);
        heatmapLabel1.setLayoutY(height - 117);

        heatmapLabel2 = new Label(getDistanceString(-canvasDrawer.getDistancemin()/2));
        heatmapLabel2.setLayoutX(28);
        heatmapLabel2.setLayoutY(height - 68);

        heatmapLabel3 = new Label(getDistanceString(-canvasDrawer.getDistancemin()));
        heatmapLabel3.setLayoutX(28);
        heatmapLabel3.setLayoutY(height - 19);

        captionHeatMap = new Canvas(10, 100);
        captionHeatMap.setLayoutX(10);
        captionHeatMap.setLayoutY(height - 109);
        GraphicsContext gc2 = captionHeatMap.getGraphicsContext2D();

        for (int i = 0 ; i < 100; i++){
            gc2.setFill(canvasDrawer.getHeatColor(i,100));
            gc2.fillRect(0, i, 10, 1);
        }

        // Caption Cellmap
        // Offset depending on the Heatmap caption width
        double offset = heatmapLabel3.getLayoutX() + heatmapLabel3.getText().length()*5;

        cellSizeInfo = new Label("Cellsize: " + input.getCellsize());
        cellSizeInfo.setLayoutX(offset + 25);
        cellSizeInfo.setLayoutY(height - 76);

        infoPerson = new Label("Person");
        infoPerson.setLayoutX(offset + 40);
        infoPerson.setLayoutY(height - 58);

        infoDestination = new Label("Destination");
        infoDestination.setLayoutX(offset + 40);
        infoDestination.setLayoutY(height - 40);

        infoWall = new Label("Wall/Obstacle");
        infoWall.setLayoutX(offset + 40);
        infoWall.setLayoutY(height - 22);

        captionColors = new Canvas(10,46);
        captionColors.setLayoutX(offset + 25);
        captionColors.setLayoutY(height - 55);
        GraphicsContext gc = captionColors.getGraphicsContext2D();

        gc.setFill(PERSONCOLOR);
        gc.fillRect(0, 0, 10, 10);

        gc.setFill(GOALCOLOR);
        gc.fillRect(0, 18, 10, 10);

        gc.setFill(WALLCOLOR);
        gc.fillRect(0, 36, 10, 10);

        // Add Nodes to info panel.
        info.getChildren().add(cellSizeInfo);
        info.getChildren().add(infoPerson);
        info.getChildren().add(infoDestination);
        info.getChildren().add(infoWall);
        info.getChildren().add(captionColors);
        info.getChildren().add(heatmapLabel);
        info.getChildren().add(heatmapLabel1);
        info.getChildren().add(heatmapLabel2);
        info.getChildren().add(heatmapLabel3);
        info.getChildren().add(captionHeatMap);
    }

    /**
     * Helping method for creating the content of the heatmap labels.
     * @param distance
     * @return
     */
    private String getDistanceString(double distance){
        String result = (distance+"");
        result = result.substring(0,result.indexOf(".")+3);
        result += "m";
        return result;
    }

    /**
     * Helping method for creating the labels.
     */
    public void createLabels(){
        algorithmLabel = new Label("Algorithm type: " + input.getAlgorithm());
        algorithmLabel.setLayoutX(10);
        algorithmLabel.setLayoutY(14);

        velocityLabel = new Label("Free Flow Velocity: " + input.getFreeFlowVelocity());
        velocityLabel.setLayoutX(10);
        velocityLabel.setLayoutY(32);

        deviationLabel = new Label("Deviation: " + input.getDeviation());
        deviationLabel.setLayoutX(10);
        deviationLabel.setLayoutY(50);

        stepLabel = new Label("Current step: " + canvasDrawer.getStep());
        stepLabel.setLayoutX(10);
        stepLabel.setLayoutY(68);

        timeLabel = new Label("Current Time: 0");
        timeLabel.setLayoutX(10);
        timeLabel.setLayoutY(86);

        simulationTimeLabel = new Label("Simulationtime: " + input.getEvents().stream().map(outputEvent -> outputEvent.getTime()).max(BigDecimal::compareTo).get());
        simulationTimeLabel.setLayoutX(10);
        simulationTimeLabel.setLayoutY(104);

        personLabel = new Label("People in the Simulation: 0");
        personLabel.setLayoutX(10);
        personLabel.setLayoutY(122);

        info.getChildren().add(algorithmLabel);
        info.getChildren().add(velocityLabel);
        info.getChildren().add(deviationLabel);
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
            if (canvasDrawer.getStep() < input.getEvents().size()) {
                canvasDrawer.handleNextEvent();
                personLabel.setText("People in the Simulation: " + canvasDrawer.getPersonList().size());
                timeLabel.setText("Current Time: " + canvasDrawer.getNext().getTime());
                stepLabel.setText("Current step: " + canvasDrawer.getStep());
            }
            if (canvasDrawer.getStep() >= input.getEvents().size()){
                stepLabel.setText("Finished");
            }
        });

        // Reset button
        reset = new Button("Reset");
        reset.setLayoutX(75);
        reset.setLayoutY(42);

        reset.setOnAction(event -> {
            canvasDrawer.reset();
            stepLabel.setText("Current step: " + canvasDrawer.getStep());
            timeLabel.setText("Current Time: 0");
            personLabel.setText("People in the Simulation: 0");
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
                canvasDrawer.getHeatLayer().toBack();
            } else {
                heatmap = true;
                changeLayer.setText("Cellmap");
                canvasDrawer.getCellLayer().toBack();
            }
        });

        // Load simulation button
        loadXML = new Button("Load Simulation");
        loadXML.setLayoutX(350);
        loadXML.setLayoutY(42);
        loadXML.setOnAction(event -> {
            loadXmlMethod();
        });


        // Load snapshot button
        snapshot = new Button("snapshot");
        snapshot.setLayoutX(250);
        snapshot.setLayoutY(42);
        snapshot.setOnAction(event -> {
            try {
                saveSnapshot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add the buttons to the pane
        menu.getChildren().add(proceed);
        menu.getChildren().add(reset);
        menu.getChildren().add(play);
        menu.getChildren().add(changeLayer);
        menu.getChildren().add(loadXML);
        menu.getChildren().add(snapshot);

        // At the beginning there is no simulation selected. Therefore all buttons will be disabled.
        play.setDisable(true);
        proceed.setDisable(true);
        reset.setDisable(true);
        slider.setDisable(true);
        changeLayer.setDisable(true);
        snapshot.setDisable(true);
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
        canvasDrawer.removeLayers();

        play.setDisable(false);
        proceed.setDisable(false);
        reset.setDisable(false);
        slider.setDisable(false);
        changeLayer.setDisable(false);
        snapshot.setDisable(false);
        canvasDrawer.setCellSize();
        canvasDrawer.createGrid();
        createInfo();
        canvasDrawer.reset();
        stepLabel.setText("Current step: " + canvasDrawer.getStep());
        timeLabel.setText("Current Time: 0");
        personLabel.setText("People in the Simulation: 0");

        if(heatmap){
            canvasDrawer.getCellLayer().toBack();
        } else {
            canvasDrawer.getHeatLayer().toBack();
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
                    while (running && canvasDrawer.getStep() < input.getEvents().size()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                while (true){
                                    canvasDrawer.handleNextEvent();
                                    if (canvasDrawer.getStep() + 1 < input.getEvents().size()) {
                                        BigDecimal a = input.getEvents().get(canvasDrawer.getStep()).getTime();
                                        BigDecimal b = input.getEvents().get(canvasDrawer.getStep() - 1).getTime();
                                        waitingtime = a.subtract(b).multiply(new BigDecimal(1000)).longValue();
                                        if (waitingtime > 0){
                                            break;
                                        }
                                        if (DEBUG){
                                            System.out.println("Next Event at the same time!");
                                        }
                                    } else {
                                        waitingtime = 0;
                                        break;
                                    }
                                }
                            }
                        });
                        if (DEBUG) {
                            System.out.println("Waiting Time for the next Event: " + waitingtime);
                        }
                        try {
                            Thread.sleep(Math.max(MILLIS, (long) (waitingtime / speed)));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    running = false;
                    proceed.setDisable(false);
                    if (canvasDrawer.getStep() >= input.getEvents().size()) {
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
}
