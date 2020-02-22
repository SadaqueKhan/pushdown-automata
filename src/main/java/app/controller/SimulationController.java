package app.controller;

import app.model.Configuration;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.model.TransitionModel;
import app.view.SimulationView;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class SimulationController {
    private final MainStageController mainStageController;
    private final MachineModel machineModel;
    private final SimulationView simulationView;
    private ArrayList<Configuration> simulationPath;

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
                this.simulationPath = simulationModel.getConfigurationPath();
                loadConfigurationsOntoSimulationView();

            }
        }
    }

    private void loadConfigurationsOntoSimulationView() {
        ListView<Configuration> simulationListView = simulationView.getTransitionsTakenlistView();
        simulationListView.getItems().addAll(simulationPath);
        simulationListView.setCellFactory(new Callback<ListView<Configuration>, ListCell<Configuration>>() {
            @Override
            public ListCell<Configuration> call(ListView<Configuration> param) {
                return new ListCell<Configuration>() {
                    @Override
                    protected void updateItem(Configuration item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle(null);
                        } else {
                            setText(item.toString());
                            if (item.isSuccessConfig()) {
                                setStyle("-fx-control-inner-background: " + "derive(#b3ff05, 50%);");
                            }

                            if (item.isFailConfig()) {
                                setStyle("-fx-control-inner-background: " + "derive(#ff6c5c, 50%);");
                            }

                            if (item.isStuckConfig()) {
                                setStyle("-fx-control-inner-background: " + "derive(#ffa22e, 50%);");
                            }
                        }
                    }
                };
            }
        });
    }


    public void updateDiagramViewForSelectedConfiguration(Configuration selectedConfiguration) {
        TransitionModel transitionModelToHighlight = selectedConfiguration.getTransitionModelTakenToReachCurrentConfiguration();
        DiagramController diagramController = mainStageController.getDiagramController();
        diagramController.highlightTransitionView(transitionModelToHighlight);

    }

    public void updateTapeViewForSelectedConfiguration(Configuration selectedConfiguration) {
        mainStageController.renderNewTapeView(selectedConfiguration);
    }

    public void updateStackViewForSelectedConfiguration(Configuration selectedConfiguration) {
        mainStageController.renderNewStackView(selectedConfiguration.getStackContent());
    }
}
