package visualisation.draw;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import outputFile.Output;

import java.math.BigDecimal;

import static visualisation.GuiConfiguration.GOALCOLOR;
import static visualisation.GuiConfiguration.INFOSIZE;
import static visualisation.GuiConfiguration.PERSONCOLOR;
import static visualisation.GuiConfiguration.WALLCOLOR;

/**
 * Created by Arne on 16.06.2017.
 */
public class InfoDrawer {

    private Pane info = new Pane();

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

    // Object Variables
    private Stage canvasStage;
    private Stage infoStage;
    private CanvasDrawer canvasDrawer;
    private Output input;

    public InfoDrawer(Stage canvasStage, Stage infoStage, CanvasDrawer canvasDrawer) {
        this.canvasStage = canvasStage;
        this.infoStage = infoStage;
        this.canvasDrawer = canvasDrawer;
        infoStage.setScene(new Scene(info, INFOSIZE, canvasStage.getScene().getHeight()));
    }

    /**
     * Helping method for creating the info panel.
     */
    public void createInfo(){
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

    public void updateLabels(){
        personLabel.setText("People in the Simulation: " + canvasDrawer.getPersonList().size());
        timeLabel.setText("Current Time: " + canvasDrawer.getNext().getTime());
        stepLabel.setText("Current step: " + canvasDrawer.getStep());
        if (canvasDrawer.getStep() >= input.getEvents().size()){
            stepLabel.setText("Finished");
        }
    }

    public void setInput(Output input) {
        this.input = input;
    }

    public Label getStepLabel() {
        return stepLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getPersonLabel() {
        return personLabel;
    }
}
