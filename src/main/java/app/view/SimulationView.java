package app.view;

import app.controller.SimulationController;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class SimulationView extends BorderPane {

    //Reference to simulation controller
    private final SimulationController simulationController;

    //UI components at the top of the scene
    private Text inputTextField;

    //UI components in the center of the scene
    private Accordion simulationsAccordianContainer;

    public SimulationView(SimulationController simulationController) {
        this.simulationController = simulationController;
        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    private void setUpUIComponents() {
        //UI components at the top of the scene
        this.inputTextField = new Text("Successful paths: ");
        this.setTop(inputTextField);

        //UI components in the center of the scene
        this.simulationsAccordianContainer = new Accordion();
        simulationsAccordianContainer.setMinSize(200, 200);
        this.setCenter(simulationsAccordianContainer);
    }

    private void setUpUILayout() {
    }

    private void setUpUIListeners() {
    }

    public void renderSuccessfulSimulationsToView(ArrayList<String> successfulPathList) {

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(successfulPathList);
        HBox hbox = new HBox(listView);
        HBox.setHgrow(listView, Priority.ALWAYS);

        TitledPane titledPane = new TitledPane();
        titledPane.setText("Simulation1");
        titledPane.setContent(hbox);

        simulationsAccordianContainer.getPanes().add(titledPane);
    }
}