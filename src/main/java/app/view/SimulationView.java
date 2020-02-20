package app.view;

import app.controller.SimulationController;
import app.listener.SimulationListener;
import app.model.TransitionModel;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
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

    }

    private void setUpUIComponents() {
        //UI components at the top of the scene
        this.inputTextField = new Text();
        this.setTop(inputTextField);

        //UI components in the center of the scene
        this.simulationsAccordianContainer = new Accordion();
        simulationsAccordianContainer.setMinSize(200, 200);
        this.setCenter(simulationsAccordianContainer);
    }

    public void renderSuccessfulSimulationsToView(HashMap<Integer, ArrayList<TransitionModel>> transitionsTakenList) {

        if (transitionsTakenList.isEmpty()) {
            this.inputTextField.setText("No successful paths found.");
        } else {
            this.inputTextField.setText("Successful paths");
            for (Map.Entry<Integer, ArrayList<TransitionModel>> entry : transitionsTakenList.entrySet()) {
                ListView<TransitionModel> transitionsTakenlistView = new ListView<>();
                transitionsTakenlistView.getItems().addAll(entry.getValue());
                transitionsTakenlistView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                transitionsTakenlistView.setOnMouseReleased(new SimulationListener(simulationController));

                TitledPane titledPane = new TitledPane();
                titledPane.setText(entry.getKey().toString());
                titledPane.setContent(transitionsTakenlistView);


                simulationsAccordianContainer.getPanes().add(titledPane);
            }
        }
    }


    private void setUpUILayout() {
    }


}