package app.view;

import app.controller.SimulationController;
import app.listener.SimulationListener;
import app.model.ConfigurationModel;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SimulationView extends BorderPane {

    //Reference to simulation controller
    private final SimulationController simulationController;

    //UI components at the top of the scene
    private Text simulationStatsTextField;

    //UI components in the center of the scene
    private ListView<ConfigurationModel> transitionsTakenlistView;


    public SimulationView(SimulationController simulationController) {
        this.simulationController = simulationController;
        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        //UI components at the top of the scene
        this.simulationStatsTextField = new Text();
        hBox.getChildren().add(simulationStatsTextField);
        setTop(hBox);

        //UI components in the center of the scene
        this.transitionsTakenlistView = new ListView<>();
        setCenter(transitionsTakenlistView);
    }


    private void setUpUIListeners() {
        SimulationListener simulationListener = new SimulationListener(simulationController);
        transitionsTakenlistView.setOnMouseReleased(simulationListener);
    }

    public Text getSimulationStatsTextField() {
        return simulationStatsTextField;
    }

    public ListView<ConfigurationModel> getTransitionsTakenlistView() {
        return transitionsTakenlistView;
    }

}