package app.controller;

import app.model.Configuration;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.view.SimulationView;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SimulationController {
    private final MainStageController mainStageController;
    private final MachineModel machineModel;
    private final SimulationView simulationView;

    public SimulationController(MainStageController mainStageController, MachineModel machineModel, String inputWord) {
        this.mainStageController = mainStageController;
        this.machineModel = machineModel;
        this.simulationView = new SimulationView(this);
        generateSimulation(machineModel, inputWord);

        //Create a new scene to render simulation
        Scene scene = new Scene(simulationView, 500, 500);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Simulation");
        stage.setScene(scene);
        stage.show();

        mainStageController.setSimulationProgressBar(false);
    }

    private void generateSimulation(MachineModel machineModel, String inputWord) {

        SimulationModel simulationModel = new SimulationModel(machineModel, inputWord);


        if (machineModel.isAcceptanceByFinalState() && machineModel.findFinalStateModel() == null) {

        } else {
            int flag = simulationModel.run();

            if (flag == 200) {
                loadConfigurationsOntoSimulationView(simulationModel);
            }
        }
    }

    private void loadConfigurationsOntoSimulationView(SimulationModel simulationModel) {
        ListView<Configuration> simulationListView = simulationView.getTransitionsTakenlistView();

        ArrayList<Configuration> configurationPath = simulationModel.getConfigurationPath();

        simulationListView.getItems().addAll(configurationPath);
    }


}
