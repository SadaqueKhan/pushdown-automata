package app.view;

import app.listener.StepRunSimulationListener;
import app.model.TransitionModel;
import app.presenter.SimulationPresenter;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class StepRunSimulationStage extends BorderPane {

    //Reference to simulation controller
    private final SimulationPresenter simulationPresenter;
    private ListView<TransitionModel> transitionOptionsListView;
    private Button backButton;
    private Button forwardButton;

    public StepRunSimulationStage(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
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
        StepRunSimulationListener stepRunSimulationListener = new StepRunSimulationListener(simulationPresenter);
        backButton.setOnAction(stepRunSimulationListener);
        forwardButton.setOnAction(stepRunSimulationListener);
    }


    public ListView<TransitionModel> getTransitionOptionsListView() {
        return transitionOptionsListView;
    }
}