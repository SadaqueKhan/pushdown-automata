package app.view;

import app.controller.SimulationController;
import app.listener.SimulationListener;
import app.model.ConfigurationModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SegmentedButton;

public class SimulationView extends BorderPane {

    //Reference to simulation controller
    private final SimulationController simulationController;

    //UI components at the top of the scene
    private Text simulationStatsTextField;

    //UI components in the center of the scene
    private ListView<ConfigurationModel> algorithmlistView;
    private ToggleButton toggleAlgorithmButton;
    private ToggleButton togglePathButton;
    private VBox containerForCenterNodes;
    private VBox pathsVBox;


    public SimulationView(SimulationController simulationController) {
        this.simulationController = simulationController;
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

        HBox statsBarHBoxContainer = new HBox();
        statsBarHBoxContainer.setAlignment(Pos.CENTER);
        //UI components at the top of the scene
        this.simulationStatsTextField = new Text();
        statsBarHBoxContainer.getChildren().add(simulationStatsTextField);

        VBox containerForTopNodes = new VBox();
        containerForTopNodes.getChildren().addAll(segmentedButton, statsBarHBoxContainer);

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

        setCenter(containerForCenterNodes);
    }

    private void setUpUIListeners() {
        SimulationListener simulationListener = new SimulationListener(simulationController);
        toggleAlgorithmButton.setOnAction(simulationListener);
        togglePathButton.setOnAction(simulationListener);
        algorithmlistView.setOnMouseReleased(simulationListener);
    }

    public Text getSimulationStatsTextField() {
        return simulationStatsTextField;
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
}