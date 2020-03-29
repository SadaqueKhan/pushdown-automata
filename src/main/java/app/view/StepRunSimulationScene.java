package app.view;
import app.listener.StepRunSimulationListener;
import app.model.TransitionModel;
import app.presenter.SimulationStagePresenter;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.HiddenSidesPane;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for a step run simulation scene.
 * </p>
 */
public class StepRunSimulationScene extends BorderPane {
    //Reference to simulation presenter.
    private final SimulationStagePresenter simulationStagePresenter;
    //Reference to UI components for view.
    private ListView<TransitionModel> transitionOptionsListView;
    private Button backwardButton;
    private Button forwardButton;
    private Text currentConfigTextField;
    private VBox historyVBox;
    /**
     * \
     * Constructor of the step run simulation scene, used to instantiate an instance of the view.
     * @param simulationStagePresenter a reference to the views presenter.
     */
    public StepRunSimulationScene(SimulationStagePresenter simulationStagePresenter) {
        this.simulationStagePresenter = simulationStagePresenter;
        setUpUIComponents();
        setUpStepUIListeners();
    }
    /**
     * Sets up the UI components of the view.
     */
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
        ScrollPane historyVBoxScrollPane = new ScrollPane();
        historyVBoxScrollPane.pannableProperty().set(true);
        historyVBoxScrollPane.fitToWidthProperty().set(true);
        historyVBoxScrollPane.fitToHeightProperty().set(true);
        historyVBoxScrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.historyVBox = new VBox();
        String cssLayout = "-fx-border-color: black;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid;\n" +
                "-fx-background-color: white;\n";
        historyVBox.setStyle(cssLayout);
        historyVBoxScrollPane.setContent(historyVBox);
        HiddenSidesPane pane = new HiddenSidesPane();
        pane.setContent(transitionOptionsListView);
        pane.setRight(historyVBoxScrollPane);
        setCenter(pane);
        //Set up components for the bottom of the stage
        backwardButton = new Button("<<< Backward");
        forwardButton = new Button("Forward >>>");
        backwardButton.setMinWidth(275);
        forwardButton.setMinWidth(275);
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().addAll(backwardButton, forwardButton);
        buttonContainer.setAlignment(Pos.CENTER);
        setBottom(buttonContainer);
    }
    /**
     * Sets up the listeners for UI components of the view.
     */
    private void setUpStepUIListeners() {
        StepRunSimulationListener stepRunSimulationListener = new StepRunSimulationListener(simulationStagePresenter);
        backwardButton.setOnAction(stepRunSimulationListener);
        forwardButton.setOnAction(stepRunSimulationListener);
    }
    // Getters for UI components of the view.
    public ListView<TransitionModel> getTransitionOptionsListView() {
        return transitionOptionsListView;
    }
    public Text getCurrentConfigTextField() {
        return currentConfigTextField;
    }
    public VBox getHistoryVBox() {
        return historyVBox;
    }
}