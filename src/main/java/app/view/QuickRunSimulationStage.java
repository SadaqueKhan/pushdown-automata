package app.view;

import app.listener.QuickRunSimulationListener;
import app.model.ConfigurationModel;
import app.presenter.SimulationPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.SegmentedButton;

public class QuickRunSimulationStage extends BorderPane {

    //Reference to simulation controller
    private final SimulationPresenter simulationPresenter;

    // UI components
    private ListView<ConfigurationModel> algorithmlistView;
    private ToggleButton toggleAlgorithmButton;
    private ToggleButton togglePathButton;
    private VBox containerForCenterNodes;
    private VBox pathsVBox;
    private ScrollPane pathsScrollPane;
    private Label simulationStatsLabel;


    public QuickRunSimulationStage(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIComponents() {
        //Setup center GUI elements
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

        Accordion pathAccordian = new Accordion();
        pathsVBox = new VBox(pathAccordian);

        pathsScrollPane = new ScrollPane();
        pathsScrollPane.setContent(pathsVBox);
        pathsScrollPane.setFitToWidth(true);

        //---
        HiddenSidesPane pane = new HiddenSidesPane();
        pane.setContent(containerForCenterNodes);
        simulationStatsLabel = new Label();
        simulationStatsLabel.setWrapText(true);
        simulationStatsLabel.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setRight(simulationStatsLabel);

        setCenter(pane);
    }

    private void setUpUIListeners() {
        QuickRunSimulationListener quickRunSimulationListener = new QuickRunSimulationListener(simulationPresenter);
        toggleAlgorithmButton.setOnAction(quickRunSimulationListener);
        togglePathButton.setOnAction(quickRunSimulationListener);
        algorithmlistView.setOnMouseReleased(quickRunSimulationListener);
    }

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