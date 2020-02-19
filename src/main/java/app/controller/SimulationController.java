package app.controller;

import app.model.MachineModel;
import app.model.SimulationModel;
import app.view.SimulationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimulationController {
    private final MainStageController mainStageController;
    private final SimulationView simulationView;

    public SimulationController(MainStageController mainStageController, MachineModel machineModel, String inputWord) {
        this.mainStageController = mainStageController;
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

        int flag = simulationModel.run();

//        if (flag == 5) {
//            ArrayList<String> successfulPathList = new ArrayList<>();
//            for (simulationModel.getPathList())
//                simulationView.renderSuccessfulSimulationsToView(simulationModel.getPathList())
//        }

        /**
         *
         */

    }
}
