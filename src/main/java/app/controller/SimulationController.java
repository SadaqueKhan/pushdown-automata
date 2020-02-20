package app.controller;

import app.model.Configuration;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.model.TransitionModel;
import app.view.SimulationView;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
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

        SimulationModel simulationModel = new SimulationModel(machineModel, inputWord);

        HashMap<Integer, ArrayList<TransitionModel>> transitionsTakenToReachASuccessConfigurationMap = new HashMap<>();

        if (machineModel.isAcceptanceByFinalState() && machineModel.findFinalStateModel() == null) {
            simulationView.renderSuccessfulSimulationsToView(transitionsTakenToReachASuccessConfigurationMap);
        } else {
            int flag = simulationModel.run();

            if (flag == 200) {
                transitionsTakenToReachASuccessConfigurationMap = createTransitionMapping(simulationModel);
                simulationView.renderSuccessfulSimulationsToView(transitionsTakenToReachASuccessConfigurationMap);
            }


        }
    }

    private HashMap<Integer, ArrayList<TransitionModel>> createTransitionMapping(SimulationModel simulationModel) {
        HashMap<Integer, ArrayList<Configuration>> successConfigurations = new HashMap<>();


        for (Map.Entry<Integer, Configuration> entry : simulationModel.getSuccessConfigurations().entrySet()) {

            ArrayList<Configuration> successPathViaState = new ArrayList<>();
            ArrayList<Configuration> reverse = new ArrayList<>();

            successPathViaState.add(entry.getValue()); // Success state

            Configuration checkHasParent = entry.getValue().getParentConfiguration();
            while (checkHasParent != null) {
                successPathViaState.add(checkHasParent);
                checkHasParent = checkHasParent.getParentConfiguration();
            }

            for (int i = successPathViaState.size() - 1; i >= 0; i--) {
                reverse.add(successPathViaState.get(i));
            }
            successConfigurations.put(entry.getKey(), reverse);
        }


        HashMap<Integer, ArrayList<TransitionModel>> transitionsTakenToReachASuccessConfigurationMap = new HashMap<>();

        for (Map.Entry<Integer, ArrayList<Configuration>> entry : successConfigurations.entrySet()) {

            ArrayList<TransitionModel> transitionModelsTakenToReachASuccessConfigurationList = new ArrayList<>();

            for (Configuration configuration : entry.getValue()) {
                TransitionModel transitionModelTakenToReachSuccessConfiguration = configuration.getTransitionModelTakenToReachCurrentConfiguration();
                if (transitionModelTakenToReachSuccessConfiguration == null) {
                    continue;
                }
                transitionModelsTakenToReachASuccessConfigurationList.add(transitionModelTakenToReachSuccessConfiguration);
            }

            transitionsTakenToReachASuccessConfigurationMap.put(entry.getKey(), transitionModelsTakenToReachASuccessConfigurationList);
        }

        return transitionsTakenToReachASuccessConfigurationMap;
    }

    public void highlightDiagram(ListView<TransitionModel> listView) {
        for (TransitionModel transitionModel : listView.getSelectionModel().getSelectedItems()) {
            System.out.println(transitionModel);
        }

    }
}
