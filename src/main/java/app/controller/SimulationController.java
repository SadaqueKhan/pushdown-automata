package app.controller;

import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.view.SimulationView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class SimulationController {
    private final MainStageController mainStageController;
    private final MachineModel machineModel;
    private final SimulationView simulationView;
    private final Stage simulationStage;
    private SimulationModel simulationModel;

    public SimulationController(MainStageController mainStageController, MachineModel machineModel, String inputWord) {
        this.mainStageController = mainStageController;
        this.machineModel = machineModel;
        this.simulationView = new SimulationView(this);

        generateSimulation(machineModel, inputWord);

        //Create a new scene to render simulation
        Scene scene = new Scene(simulationView, 550, 500);
        simulationStage = new Stage();
        simulationStage.initModality(Modality.WINDOW_MODAL);
        simulationStage.initOwner(mainStageController.getPrimaryWindow());
        simulationStage.setResizable(false);
        simulationStage.setTitle("Simulation");
        simulationStage.setScene(scene);
        simulationStage.show();
        simulationStage.setOnCloseRequest(event -> {
            DiagramController diagramController = mainStageController.getDiagramController();
            diagramController.removeHighlightedTransitionView();
        });
    }

    private void generateSimulation(MachineModel machineModel, String inputWord) {
        simulationModel = new SimulationModel(machineModel, inputWord);

        int flag = simulationModel.run();

        if (flag == 200) {
            triggerAlgorithmView();
            String simulationStatsString;
            if (simulationModel.isNFA()) {
                simulationStatsString = "Type: " + "NFA" + " " + "Success paths: " + simulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + simulationModel.getNumOfPossibleInfinitePaths();
            } else {
                simulationStatsString = "Type: " + "DFA" + " " + "Success paths: " + simulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + simulationModel.getNumOfPossibleInfinitePaths();
            }
            simulationView.getSimulationStatsTextField().setText(simulationStatsString);

        }
    }

    public void triggerAlgorithmView() {
        ListView<ConfigurationModel> simulationListView = simulationView.getAlgorithmlistView();
        simulationListView.getItems().clear();

        // Get algorithm path list
        ArrayList<ConfigurationModel> algorithmPathList = simulationModel.getConfigurationPath();
        simulationListView.getItems().addAll(algorithmPathList);

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

                            if (algorithmPathList.lastIndexOf(item) == index) {
                                setStyle("-fx-control-inner-background: " + "derive(#ff6c5c, 50%);");
                                itemToPrint += " (No More Paths!)";
                            } else if (algorithmPathList.indexOf(item) < index && index < algorithmPathList.lastIndexOf(item)) {
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
        simulationView.getContainerForCenterNodes().getChildren().remove(0);
        simulationView.getContainerForCenterNodes().getChildren().add(simulationView.getAlgorithmlistView());
    }


    public void triggerPathsView() {
        //Get leaf configurations
        ArrayList<ConfigurationModel> leafConfigurationPath = simulationModel.getLeafConfigurationPath();

        //Get accordian
        Accordion accordion = (Accordion) simulationView.getPathsVBox().getChildren().get(0);
        accordion.getPanes().clear();

        int numPath = 0;

        for (ConfigurationModel leafConfigurationModel : leafConfigurationPath) {
            ListView<ConfigurationModel> newListView = new ListView<>();
            newListView.getItems().addAll(leafConfigurationModel.getPath());

            newListView.setOnMouseReleased(event -> {
                ObservableList<ConfigurationModel> selectedConfigurationsToHighlightList = newListView.getSelectionModel().getSelectedItems();
                if (!(selectedConfigurationsToHighlightList.isEmpty())) {
                    ConfigurationModel selectedConfiguration = selectedConfigurationsToHighlightList.get(0);
                    updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                    updateTapeViewForSelectedConfiguration(selectedConfiguration);
                    updateStackViewForSelectedConfiguration(selectedConfiguration);
                }
            });

            ++numPath;
            System.out.println(leafConfigurationPath.size());
            System.out.println(leafConfigurationModel.getCurrentStateModel());
            System.out.println("Success: " + leafConfigurationModel.isSuccessConfig());
            System.out.println("Stuck: " + leafConfigurationModel.isStuckConfig());
            System.out.println("Fail: " + leafConfigurationModel.isFailConfig());
            System.out.println("Infinite: " + leafConfigurationModel.isInfiniteConfig());
            System.out.println("-----");

            if (leafConfigurationModel.isSuccessConfig()) {
                accordion.getPanes().add(new TitledPane("Path " + numPath + ": Success", newListView));
            } else if (leafConfigurationModel.isStuckConfig()) {
                accordion.getPanes().add(new TitledPane("Path " + numPath + ": Stuck", newListView));
            } else if (leafConfigurationModel.isFailConfig()) {
                accordion.getPanes().add(new TitledPane("Path " + numPath + ": Fail", newListView));
            } else if (leafConfigurationModel.isInfiniteConfig()) {
                accordion.getPanes().add(new TitledPane("Path " + numPath + ": Infinite", newListView));
            }
        }

        simulationView.getContainerForCenterNodes().getChildren().remove(0);
        simulationView.getContainerForCenterNodes().getChildren().add(simulationView.getPathsVBox());
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

        ListView<ConfigurationModel> transitionsTakenlistView = new ListView<>();
        ArrayList<ConfigurationModel> successPathBackward = new ArrayList<>();
        successPathBackward.add(doubleClickConfiguration);

        ConfigurationModel previousConfig = doubleClickConfiguration;
        for (int i = doubleClickConfiguration.getStep() - 1; i >= 0; i--) {
            previousConfig = previousConfig.getParentConfiguration();
            successPathBackward.add(previousConfig);
        }

        ArrayList<ConfigurationModel> successPathForward = new ArrayList<>();
        for (int j = doubleClickConfiguration.getStep(); j >= 0; j--) {
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

        String windowTitle = "Path";
        if (doubleClickConfiguration.isSuccessConfig()) {
            windowTitle = "Success Path";
        }

        //Create a new scene to render simulation
        Scene scene = new Scene(transitionsTakenlistView, 750, 500);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(simulationStage);
        stage.setResizable(false);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();
    }
}
