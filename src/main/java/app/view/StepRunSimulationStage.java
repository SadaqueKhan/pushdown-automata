package app.view;

import app.listener.StepRunSimulationListener;
import app.model.TransitionModel;
import app.presenter.SimulationPresenter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StepRunSimulationStage extends BorderPane {

    //Reference to simulation controller
    private final SimulationPresenter simulationPresenter;
    private ListView<TransitionModel> transitionOptionsListView;
    private Button backButton;
    private Button forwardButton;
    private Text currentConfigTextField;

    public StepRunSimulationStage(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
        setUpUIComponents();
        setUpStepUIListeners();
    }

    private void setUpUIComponents() {

        //Set up components for the top of the stage
        this.currentConfigTextField = new Text();
        currentConfigTextField.setTextAlignment(TextAlignment.CENTER);
        HBox currentConfigTextFieldHBoxContainer = new HBox();
        currentConfigTextFieldHBoxContainer.getChildren().add(currentConfigTextField);
        currentConfigTextFieldHBoxContainer.setAlignment(Pos.CENTER);
        setTop(currentConfigTextFieldHBoxContainer);

        //Set up components for the center of the stage
        this.transitionOptionsListView = new ListView<>();
        setCenter(transitionOptionsListView);

        //Set up components for the bottom of the stage
        backButton = new Button("<<< Back");
        forwardButton = new Button("Forward >>>");

        backButton.setMinWidth(275);
        forwardButton.setMinWidth(275);
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(backButton, forwardButton);
        buttonContainer.setAlignment(Pos.CENTER);
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

    public Text getCurrentConfigTextField() {
        return currentConfigTextField;
    }
}