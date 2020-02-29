package app.controller;

import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.view.SimulationView;
import javafx.application.Platform;
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
    private ArrayList<ConfigurationModel> simulationPath;

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

        int flag = simulationModel.run();
        if (flag == 200) {
            this.simulationPath = simulationModel.getConfigurationPath();
            loadConfigurationsOntoSimulationView();

        }
    }


    private void loadConfigurationsOntoSimulationView() {
        ListView<ConfigurationModel> simulationListView = simulationView.getTransitionsTakenlistView();

        simulationListView.getItems().addAll(simulationPath);
        Platform.runLater(() -> simulationListView.setCellFactory(new Callback<ListView<ConfigurationModel>, ListCell<ConfigurationModel>>() {
            @Override
            public ListCell<ConfigurationModel> call(ListView<ConfigurationModel> param) {
                return new ListCell<ConfigurationModel>() {
                    @Override
                    protected void updateItem(ConfigurationModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setStyle(null);
                        } else {

                            int index = getIndex();
                            String itemToPrint = item.toString();

                            setStyle("-fx-control-inner-background: " + "derive(#eeeeee, 100%);");

                            if (simulationPath.lastIndexOf(item) == index) {
                                setStyle("-fx-control-inner-background: " + "derive(#ff6c5c, 50%);");
                                itemToPrint += " (No More Paths!)";
                            } else if (simulationPath.indexOf(item) < index && index < simulationPath.lastIndexOf(item)) {
                                setStyle("-fx-control-inner-background: " + "derive(#aedaff, 70%);");
                                itemToPrint += " (New Path!)";
                            }

                            if (item.getParentConfiguration() == null) {
                                setStyle("-fx-control-inner-background: " + "derive(#eeeeee, 100%);");
                                itemToPrint = item.toString() + " (At the root!)";
                            }


                            if (item.isSuccessConfig()) {
                                setStyle("-fx-control-inner-background: " + "derive(#b3ff05, 50%);");
                                itemToPrint = item.toString() + "  (Success!)";
                            }

                            setText(itemToPrint);
                        }
                    }
                };
            }
        }));
    }


    public void updateDiagramViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        DiagramController diagramController = mainStageController.getDiagramController();
        diagramController.highlightTransitionTakenInDiagram(selectedConfiguration);
    }

    public void updateTapeViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStageController.updateTapeView(selectedConfiguration.getHeadPosition());
    }

    public void updateStackViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStageController.updateStackView(selectedConfiguration.getStackContent());
    }

    public void updateTransitionTableViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
//        TransitionModel transitionModelToHighlight = selectedConfiguration.getTransitionModelTakenToReachCurrentConfiguration();
//        TransitionTableController transitionTableController = mainStageController.getTransitionTableController();
//        transitionTableController.highlightTransitionTakenInTransitionTable(transitionModelToHighlight);
    }
}
