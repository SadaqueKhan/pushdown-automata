package app.view;
import app.listener.QuickRunSimulationListener;
import app.model.ConfigurationModel;
import app.presenter.SimulationStagePresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.SegmentedButton;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for a quick run simulation scene.
 * </p>
 */
public class QuickRunSimulationScene extends BorderPane {
    //Reference to simulation presenter.
    private final SimulationStagePresenter simulationStagePresenter;
    //Reference to UI components for this view.
    private ListView<ConfigurationModel> algorithmlistView;
    private ToggleButton toggleAlgorithmButton;
    private ToggleButton togglePathButton;
    private VBox containerForCenterNodes;
    private VBox pathsVBox;
    private ScrollPane pathsScrollPane;
    private Label simulationStatsLabel;
    /**
     * Constructor of the quick run simulation scene, used to instantiate an instance of this view.
     * @param simulationStagePresenter
     */
    public QuickRunSimulationScene(SimulationStagePresenter simulationStagePresenter) {
        this.simulationStagePresenter = simulationStagePresenter;
        setUpUIComponents();
        setUpUIListeners();
    }
    /**
     * Sets up the UI components of the view.
     */
    private void setUpUIComponents() {
        this.toggleAlgorithmButton = new ToggleButton("Algorithm");
        this.togglePathButton = new ToggleButton("Paths");
        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleAlgorithmButton, togglePathButton);
        toggleAlgorithmButton.setSelected(true);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.getChildren().addAll(segmentedButton);
        containerForTopNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForTopNodes.setSpacing(5);
        containerForTopNodes.setAlignment(Pos.TOP_CENTER);
        setTop(containerForTopNodes);
        this.containerForCenterNodes = new VBox();
        containerForCenterNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForCenterNodes.setSpacing(5);
        containerForCenterNodes.setAlignment(Pos.TOP_CENTER);
        this.algorithmlistView = new ListView<>();
        containerForCenterNodes.getChildren().add(algorithmlistView);
        Accordion pathAccordion = new Accordion();
        pathsVBox = new VBox(pathAccordion);
        pathsScrollPane = new ScrollPane();
        pathsScrollPane.setContent(pathsVBox);
        pathsScrollPane.setFitToWidth(true);
        HiddenSidesPane pane = new HiddenSidesPane();
        pane.setContent(containerForCenterNodes);
        simulationStatsLabel = new Label();
        simulationStatsLabel.setWrapText(true);
        simulationStatsLabel.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setRight(simulationStatsLabel);
        setCenter(pane);
    }
    /**
     * Sets up the listeners for UI components of the view.
     */
    private void setUpUIListeners() {
        //Create listener for UI component for this view.
        QuickRunSimulationListener quickRunSimulationListener = new QuickRunSimulationListener(simulationStagePresenter);
        //Link listener to events on UI components for this view.
        toggleAlgorithmButton.setOnAction(quickRunSimulationListener);
        togglePathButton.setOnAction(quickRunSimulationListener);
        algorithmlistView.setOnMouseReleased(quickRunSimulationListener);
    }
    // Getters for UI components of the view.
    public Label getSimulationStatsLabel() {
        return simulationStatsLabel;
    }
    public ListView<ConfigurationModel> getAlgorithmlistView() {
        return algorithmlistView;
    }
    public VBox getPathsVBox() {
        return pathsVBox;
    }
    public VBox getContainerForCenterNodes() {
        return containerForCenterNodes;
    }
    public ScrollPane getPathsScrollPane() {
        return pathsScrollPane;
    }
}