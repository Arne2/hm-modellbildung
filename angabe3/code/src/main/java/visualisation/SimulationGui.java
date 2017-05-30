package visualisation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Simulation;
import outputFile.Output;
import outputFile.OutputEvent;
import person.Person;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Arne on 30.05.2017.
 */
public class SimulationGui extends Application {
    private String path = System.getProperty("user.dir");
    private List<SimulatedPerson> personList = new ArrayList<>();
    private int step = 0;
    private Label stepLabel;
    private Label timeLabel;
    private GridPane gridPane;
    public static Output input = null;
    public static void main(String[] args) {
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
        gridPane = createGridPane();
        gridPane.setLayoutY(50);
        stepLabel = new Label(step+"");
        stepLabel.setLayoutX(50);
        stepLabel.setLayoutY(14);

        //TODO: timeLabel

        Button proceed = new Button("Next Step");
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

        Pane root = new Pane();
        root.getChildren().add(stepLabel);
        root.getChildren().add(proceed);
        root.getChildren().add(gridPane);
        primaryStage.setScene(new Scene(root, 30*input.getFieldWidth(), 30*input.getFieldHeight() + 50));
        primaryStage.show();
    }

    public GridPane createGridPane(){
        //TODO import field
        GridPane pane = new GridPane();
        for (int x = 0; x < input.getFieldWidth(); x++){
            for(int y = 0; y < input.getFieldHeight(); y++) {
                if(x == 0 && y == 0) {
                    pane.add(new ImageView(new Image("file:images/target.png")),x,y);
                } else {
                    pane.add(new ImageView(new Image("file:images/free.png")),x,y);
                }
            }
        }
        pane.setStyle("-fx-grid-lines-visible: true");
        return pane;
    }

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
                addPersonToGrid(person);
                break;
            case "move" :
                 p = personList.stream().filter(simulatedPerson -> simulatedPerson.getId() == next.getPersonID()).findAny();
                if (!p.isPresent()){
                    throw new AssertionError("Person not in personList");
                }
                person = p.get();
                removePersonFromGrid(person);
                person.setX(next.getPositionX());
                person.setY(next.getPositionY());
                addPersonToGrid(person);
                break;
            case "remove":
                p = personList.stream().filter(simulatedPerson -> simulatedPerson.getId() == next.getPersonID()).findAny();
                if (!p.isPresent()){
                    throw new AssertionError("Person not in personList");
                }
                person = p.get();
                removePersonFromGrid(person);
                personList.remove(person);
                break;
            default:
                throw new AssertionError("Invalid Event Type: " + next.getType());
        }
        System.out.println(next);
        System.out.println(personList);

        //TODO timeLabel
        stepLabel.setText(++step + "");
    }

    private void addPersonToGrid(SimulatedPerson person){
        gridPane.getChildren().remove(person.getX()*input.getFieldHeight() + person.getY());
        gridPane.add(new ImageView(new Image("file:images/person.png")),Math.toIntExact(person.getX()),Math.toIntExact(person.getY()));
    }

    private void removePersonFromGrid(SimulatedPerson person){
        gridPane.getChildren().remove(person.getX()*input.getFieldHeight() + person.getY());
        gridPane.add(new ImageView(new Image("file:images/free.png")),Math.toIntExact(person.getX()),Math.toIntExact(person.getY()));
    }
}
