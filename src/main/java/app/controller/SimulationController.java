package app.controller;

import app.model.*;
import app.view.SimulationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        for (StateModel stateModel : machineModel.getStateModelSet()) {
            System.out.println(stateModel);
        }

        for (TransitionModel transitionModel : machineModel.getTransitionModelSet()) {
            System.out.println(transitionModel);
        }

        SimulationModel simulationModel = new SimulationModel(machineModel, inputWord);

        int flag = simulationModel.run();

        HashMap<Integer, ArrayList<Configuration>> successConfigurations = new HashMap<>();

        for (Map.Entry<Integer, Configuration> entry : simulationModel.getSuccessConfigurations().entrySet()) {

            ArrayList<Configuration> successPath = new ArrayList<>();
            Configuration checkHasParent = entry.getValue().getParentConfiguration();

            while (checkHasParent != null) {
                successPath.add(checkHasParent);
                checkHasParent = checkHasParent.getParentConfiguration();
            }
            successPath.add(entry.getValue());
            successConfigurations.put(entry.getKey(), successPath);
        }


        for (Map.Entry<Integer, ArrayList<Configuration>> entry : successConfigurations.entrySet()) {

            System.out.println("Success Path: " + entry.getKey());

            for (Configuration configuration : entry.getValue()) {
                System.out.print(" " + configuration.getCurrentStateModel());

            }
            System.out.println();
        }


    }
}
