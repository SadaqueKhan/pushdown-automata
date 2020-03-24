package app.view;
import app.listener.*;
import app.model.*;
import app.presenter.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * View blueprint for a tape scene.
 * </p>
 */
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