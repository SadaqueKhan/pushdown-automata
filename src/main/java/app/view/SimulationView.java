package app.view;

import app.controller.SimulationController;
import app.model.TransitionModel;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        this.inputTextField = new Text("Successful paths ");
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

    public void renderSuccessfulSimulationsToView(HashMap<Integer, ArrayList<TransitionModel>> transitionMapping) {
        for (Map.Entry<Integer, ArrayList<TransitionModel>> entry : transitionMapping.entrySet()) {
            ListView<TransitionModel> listView = new ListView<>();
            listView.getItems().addAll(entry.getValue());
            HBox hbox = new HBox(listView);
            HBox.setHgrow(listView, Priority.ALWAYS);

            TitledPane titledPane = new TitledPane();
            titledPane.setText(entry.getKey().toString());
            titledPane.setContent(hbox);

            simulationsAccordianContainer.getPanes().add(titledPane);
        }

        this.inputTextField.setText("No successful paths found.");
    }
}