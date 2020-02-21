package app.view;

import app.controller.SimulationController;
import app.listener.SimulationListener;
import app.model.Configuration;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class SimulationView extends BorderPane {

    //Reference to simulation controller
    private final SimulationController simulationController;

    //UI components at the top of the scene
    private Text inputTextField;

    //UI components in the center of the scene
    private ListView<Configuration> transitionsTakenlistView;


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
        this.transitionsTakenlistView = new ListView<>();
        this.setCenter(transitionsTakenlistView);
    }


    private void setUpUILayout() {
        SimulationListener simulationListener = new SimulationListener(simulationController);
        transitionsTakenlistView.setOnMouseReleased(simulationListener);
    }


    public ListView<Configuration> getTransitionsTakenlistView() {
        return transitionsTakenlistView;
    }
}