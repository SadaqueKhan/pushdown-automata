package app.controller;

import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.view.SimulationView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class SimulationController {
    private final MainStageController mainStageController;
    private final MachineModel machineModel;
    private final SimulationView simulationView;
    private final Stage simulationStage;
    private ArrayList<ConfigurationModel> simulationPath;
    private SimulationModel simulationModel;

    public SimulationController(MainStageController mainStageController, MachineModel machineModel, String inputWord) {
        this.mainStageController = mainStageController;
        this.machineModel = machineModel;
        this.simulationView = new SimulationView(this);
        generateSimulation(machineModel, inputWord);

        //Create a new scene to render simulation
        Scene scene = new Scene(simulationView, 750, 500);
        simulationStage = new Stage();
        simulationStage.initModality(Modality.WINDOW_MODAL);
        simulationStage.initOwner(mainStageController.getPrimaryWindow());
        simulationStage.setResizable(false);
        simulationStage.setTitle("Simulation");
        simulationStage.setScene(scene);
        simulationStage.show();
    }

    private void generateSimulation(MachineModel machineModel, String inputWord) {
        simulationModel = new SimulationModel(machineModel, inputWord);

        int flag = simulationModel.run();

        if (flag == 200) {
            this.simulationPath = simulationModel.getConfigurationPath();
            loadConfigurationsOntoSimulationView();
            String simulationStatsString = "Success paths: " + simulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + simulationModel.getNumOfPossibleInfinitePaths();
            simulationView.getSimulationStatsTextField().setText(simulationStatsString);
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
                                itemToPrint = item.toString() + " (At the start state!)";
                            }


                            if (item.isInfiniteConfig()) {
                                setStyle("-fx-control-inner-background: " + "derive(#ffc023, 50%);");
                                itemToPrint = item.toString() + "  (Possible infinite path!)";
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


    public void createSuccessSimulationStage(ConfigurationModel doubleClickConfiguration) {


        if (doubleClickConfiguration.isSuccessConfig()) {
            ListView<ConfigurationModel> transitionsTakenlistView = new ListView<>();
            ArrayList<ConfigurationModel> successPathBackward = new ArrayList<>();
            successPathBackward.add(doubleClickConfiguration);

            ConfigurationModel previousConfig = doubleClickConfiguration;
            for (int i = doubleClickConfiguration.getStep() - 1; i >= 0; i--) {
                // whatever
                previousConfig = previousConfig.getParentConfiguration();
                successPathBackward.add(previousConfig);
            }

            ArrayList<ConfigurationModel> successPathForward = new ArrayList<>();
            for (int j = doubleClickConfiguration.getStep() - 1; j >= 0; j--) {
                // whatever
                successPathForward.add(successPathBackward.get(j));
            }

            transitionsTakenlistView.setOnMouseReleased(event -> {
                ObservableList<ConfigurationModel> selectedConfigurationsToHighlightList = transitionsTakenlistView.getSelectionModel().getSelectedItems();
                if (!(selectedConfigurationsToHighlightList.isEmpty())) {
                    ConfigurationModel selectedConfiguration = selectedConfigurationsToHighlightList.get(0);
                    updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                    updateTapeViewForSelectedConfiguration(selectedConfiguration);
                    updateStackViewForSelectedConfiguration(selectedConfiguration);
                }
            });


            transitionsTakenlistView.getItems().addAll(successPathForward);
            //Create a new scene to render simulation
            Scene scene = new Scene(transitionsTakenlistView, 750, 500);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(simulationStage);
            stage.setResizable(false);
            stage.setTitle("Success Simulation");
            stage.setScene(scene);
            stage.show();
        }
        //UI components in the center of the scene


    }
}
