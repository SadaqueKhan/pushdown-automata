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
        });
    }


    public void updateDiagramViewForSelectedConfiguration(Configuration selectedConfiguration) {
        TransitionModel transitionModelToHighlight = selectedConfiguration.getTransitionModelTakenToReachCurrentConfiguration();
        DiagramController diagramController = mainStageController.getDiagramController();
        diagramController.highlightTransitionView(transitionModelToHighlight);

    }

    public void updateTapeViewForSelectedConfiguration(Configuration selectedConfiguration) {

        mainStageController.updateTapeView(selectedConfiguration.getHeadPosition());
    }

    public void updateStackViewForSelectedConfiguration(Configuration selectedConfiguration) {
        mainStageController.updateStackView(selectedConfiguration.getStackContent());
    }
}
