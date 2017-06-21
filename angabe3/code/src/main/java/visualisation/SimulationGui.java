package visualisation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import outputFile.Output;
import visualisation.draw.CanvasDrawer;
import visualisation.draw.InfoDrawer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

import static visualisation.GuiConfiguration.DEBUG;
import static visualisation.GuiConfiguration.MENUSIZE;
import static visualisation.GuiConfiguration.MILLIS;
import static visualisation.GuiConfiguration.STARTSIZE;
import static visualisation.GuiConfiguration.screenBounds;

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
//    private Pane info = new Pane();

    // Silder
    Slider slider = new Slider();
    Label sliderLabel;

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

    /**
     * Helping class for drawing the info panel.
     */
    InfoDrawer infoDrawer;

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
        infoDrawer.setInput(input);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visualisierung");
        canvasDrawer = new CanvasDrawer(primaryStage, canvasStage, infoStage);
        infoDrawer = new InfoDrawer(canvasStage, infoStage, canvasDrawer);

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
     * Creates a png file of the current canvas.
     * @throws IOException
     * @throws URISyntaxException
     */
    public void saveSnapshot() throws IOException, URISyntaxException {
        String path = inputFile.getParent() + "/snapshot_"+ input.getAlgorithm() + "_" + LocalTime.now().toString().replace(":","_") + ".png";
        File snapshot = new File (path);
        if (!snapshot.exists()){
            Files.createDirectories(Paths.get(inputFile.getParent()));
            Files.createFile(Paths.get(path));
        }
        WritableImage writableImageCanvas = new WritableImage(((int) canvasDrawer.getCanvas().getWidth()), ((int) canvasDrawer.getCanvas().getHeight()));
        canvasDrawer.getCanvas().snapshot(null, writableImageCanvas);

        WritableImage writableImageInfo = new WritableImage(((int) infoDrawer.getCanvas().getWidth()), ((int) infoDrawer.getCanvas().getHeight()));
        infoDrawer.getCanvas().snapshot(null, writableImageInfo);

        RenderedImage renderedImage = joinImages(writableImageCanvas, writableImageInfo);
        ImageIO.write(renderedImage, "png", snapshot);
    }

    /**
     * Helping method for join Info Pane and Map Pane in one Image
     * Joins two Images side by side
     */
    public static BufferedImage joinImages(WritableImage img1_temp,WritableImage img2_temp) {
        BufferedImage img1 = SwingFXUtils.fromFXImage(img1_temp, null);
        BufferedImage img2 = SwingFXUtils.fromFXImage(img2_temp, null);
        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
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
                infoDrawer.updateLabels();
            }
            if (canvasDrawer.getStep() >= input.getEvents().size()){
                infoDrawer.getStepLabel().setText("Finished");
            }
        });

        // Reset button
        reset = new Button("Reset");
        reset.setLayoutX(75);
        reset.setLayoutY(42);

        reset.setOnAction(event -> {
            canvasDrawer.reset();
            infoDrawer.getStepLabel().setText("Current step: " + canvasDrawer.getStep());
            infoDrawer.getTimeLabel().setText("Current Time: 0");
            infoDrawer.getPersonLabel().setText("People in the Simulation: 0");
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
        infoDrawer.createInfo();
        canvasDrawer.reset();
        infoDrawer.getStepLabel().setText("Current step: " + canvasDrawer.getStep());
        infoDrawer.getTimeLabel().setText("Current Time: 0");
        infoDrawer.getPersonLabel().setText("People in the Simulation: 0");

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
                                while (canvasDrawer.getStep() < input.getEvents().size()){
                                    canvasDrawer.handleNextEvent();
                                    infoDrawer.updateLabels();
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
                                infoDrawer.getStepLabel().setText("Finished");
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
