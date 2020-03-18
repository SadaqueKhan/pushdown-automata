package app.view;

import app.listener.StepRunSimulationListener;
import app.model.TransitionModel;
import app.presenter.SimulationController;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class StepRunSimulationView extends BorderPane {

    //Reference to simulation controller
    private final SimulationController simulationController;
    private ListView<TransitionModel> transitionOptionsListView;
    private Button backButton;
    private Button forwardButton;

    public StepRunSimulationView(SimulationController simulationController) {
        this.simulationController = simulationController;
        setUpUIComponents();
        setUpStepUIListeners();
    }

    private void setUpUIComponents() {
        transitionOptionsListView = new ListView<>();
        setTop(transitionOptionsListView);

        backButton = new Button("Back");
        forwardButton = new Button("Forward");
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(backButton, forwardButton);
        setBottom(buttonContainer);
    }

    private void setUpStepUIListeners() {
        StepRunSimulationListener stepRunSimulationListener = new StepRunSimulationListener(simulationController);
        backButton.setOnAction(stepRunSimulationListener);
        forwardButton.setOnAction(stepRunSimulationListener);
    }


    public ListView<TransitionModel> getTransitionOptionsListView() {
        return transitionOptionsListView;
    }
}