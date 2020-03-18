package app.presenter;

import app.model.*;
import app.view.QuickRunSimulationStage;
import app.view.StepRunSimulationStage;
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
import java.util.List;

public class SimulationPresenter {
    private final MainStagePresenter mainStagePresenter;
    private final MachineModel machineModel;
    private final String typeOfSimulation;
    private QuickRunSimulationStage quickRunSimulationStage;
    private StepRunSimulationStage stepRunSimulationStage;
    private Stage simulationStage;
    private SimulationModel quickRunSimulationModel;
    private SimulationModel stepRunSimulationModel;

    public SimulationPresenter(MainStagePresenter mainStagePresenter, MachineModel machineModel, String inputWord, String typeOfSimulation) {
        this.mainStagePresenter = mainStagePresenter;
        this.machineModel = machineModel;
        this.typeOfSimulation = typeOfSimulation;
        System.out.println("check 2");

        if (typeOfSimulation.equals("By Quick Run")) {
            this.quickRunSimulationStage = new QuickRunSimulationStage(this);
            generateSimulation(machineModel, inputWord);

            mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(true);
            mainStagePresenter.getMainStage().getInputTextField().setDisable(true);
            //Create a new scene to render simulation
            Scene scene = new Scene(quickRunSimulationStage, 550, 500);
            simulationStage = new Stage();
            simulationStage.setResizable(false);
            simulationStage.setTitle("Simulation: Quick Run");
            simulationStage.setScene(scene);
            simulationStage.show();
            simulationStage.setOnCloseRequest(event -> {
                DiagramPresenter diagramPresenter = mainStagePresenter.getDiagramPresenter();
                diagramPresenter.removeHighlightedTransitionView();
                mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(false);
                mainStagePresenter.getMainStage().getInputTextField().setDisable(false);
                mainStagePresenter.updateStackView(new ArrayList<>());
                mainStagePresenter.updateTapeView(0);
            });
        }


        if (typeOfSimulation.equals("By Step Run")) {
            this.stepRunSimulationStage = new StepRunSimulationStage(this);
            this.stepRunSimulationModel = new SimulationModel(machineModel, inputWord);

            ConfigurationModel rootConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            TapeModel currentTapeModel = stepRunSimulationModel.getCurrentTapeModel();
            StackModel currentStackModel = stepRunSimulationModel.getCurrentStackModel();

            List<ConfigurationModel> rootChildrenConfigurationList = stepRunSimulationModel.configurationApplicable(rootConfigurationModel.getCurrentStateModel(), currentTapeModel.getAtHead(), currentStackModel.peak());

            rootConfigurationModel.setChildrenConfigurations(rootChildrenConfigurationList);

            for (ConfigurationModel rootConfigurationModelChild : rootChildrenConfigurationList) {
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(rootConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }

            mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(true);
            mainStagePresenter.getMainStage().getInputTextField().setDisable(true);
            //Create a new scene to render simulation
            Scene scene = new Scene(stepRunSimulationStage, 550, 500);
            simulationStage = new Stage();
            simulationStage.initModality(Modality.WINDOW_MODAL);
            simulationStage.initOwner(mainStagePresenter.getPrimaryWindow());
            simulationStage.setResizable(false);
            simulationStage.setTitle("Simulation: Step Run");
            simulationStage.setScene(scene);
            simulationStage.show();
            simulationStage.setOnCloseRequest(event -> {
                DiagramPresenter diagramPresenter = mainStagePresenter.getDiagramPresenter();
                diagramPresenter.removeHighlightedTransitionView();
                mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(false);
                mainStagePresenter.getMainStage().getInputTextField().setDisable(false);
                mainStagePresenter.updateStackView(new ArrayList<>());
                mainStagePresenter.updateTapeView(0);
            });
        }
    }

    private void generateSimulation(MachineModel machineModel, String inputWord) {
        quickRunSimulationModel = new SimulationModel(machineModel, inputWord);

        int flag = quickRunSimulationModel.createTree();

        if (flag == 200) {
            triggerAlgorithmView();
            String simulationStatsString;
            if (quickRunSimulationModel.isNFA()) {
                simulationStatsString = "Type: " + "NFA" + " " + "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
            } else {
                simulationStatsString = "Type: " + "DFA" + " " + "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
            }
            quickRunSimulationStage.getSimulationStatsTextField().setText(simulationStatsString);

        }
    }

    public void triggerAlgorithmView() {
        ListView<ConfigurationModel> simulationListView = quickRunSimulationStage.getAlgorithmlistView();

        if (simulationListView.getItems().isEmpty()) {
            // Get algorithm path list
            ArrayList<ConfigurationModel> algorithmPathList = quickRunSimulationModel.getConfigurationPath();
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

                                if (item.isInfiniteConfig()) {
                                    setStyle("-fx-control-inner-background: " + "derive(#ffc023, 50%);");
                                    itemToPrint = item.toString() + "  (Possible infinite path!)";
                                }
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
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().remove(0);
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().add(quickRunSimulationStage.getAlgorithmlistView());
    }


    public void triggerPathsView() {


        //Get accordian
        Accordion accordion = (Accordion) quickRunSimulationStage.getPathsVBox().getChildren().get(0);

        if (accordion.getPanes().isEmpty()) {
            //Get leaf configurations
            ArrayList<ConfigurationModel> leafConfigurationPath = quickRunSimulationModel.getLeafConfigurationPath();
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

                if (leafConfigurationModel.isSuccessConfig()) {
                    accordion.getPanes().add(new TitledPane("Path " + numPath + ": Success", newListView));
                } else if (leafConfigurationModel.isInfiniteConfig()) {
                    accordion.getPanes().add(new TitledPane("Path " + numPath + ": Infinite", newListView));
                } else {
                    accordion.getPanes().add(new TitledPane("Path " + numPath + ": Fail", newListView));
                }
            }
        }

        quickRunSimulationStage.getContainerForCenterNodes().getChildren().remove(0);
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().add(quickRunSimulationStage.getPathsScrollPane());
    }


    public void updateDiagramViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        DiagramPresenter diagramPresenter = mainStagePresenter.getDiagramPresenter();
        diagramPresenter.highlightTransitionTakenInDiagram(selectedConfiguration);
    }

    public void updateTapeViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateTapeView(selectedConfiguration.getHeadPosition());
    }

    public void updateStackViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateStackView(selectedConfiguration.getStackContent());
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

    public void stepBack() {
        ListView<TransitionModel> listView = stepRunSimulationStage.getTransitionOptionsListView();
        if (stepRunSimulationModel.getCurrentConfig().getParentConfiguration() != null) {
            listView.getItems().clear();
            stepRunSimulationModel.previous();
            ConfigurationModel currentConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            for (ConfigurationModel currentConfigurationModelChild : currentConfigurationModel.getChildrenConfigurations()) {
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(currentConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }
            updateDiagramViewForSelectedConfiguration(currentConfigurationModel);
            updateTapeViewForSelectedConfiguration(currentConfigurationModel);
            updateStackViewForSelectedConfiguration(currentConfigurationModel);
        }
    }

    public void stepForward() {
        ListView<TransitionModel> listView = stepRunSimulationStage.getTransitionOptionsListView();
        ObservableList<TransitionModel> selectedTransitionModelObservableList = listView.getSelectionModel().getSelectedItems();
        TransitionModel selectedTransitionModel = selectedTransitionModelObservableList.get(0);

        if (selectedTransitionModel != null) {
            listView.getItems().clear();

            ConfigurationModel currentConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            ConfigurationModel nextConfigurationModel = null;
            for (ConfigurationModel currentConfigurationModelChild : currentConfigurationModel.getChildrenConfigurations()) {
                if (selectedTransitionModel.equals(currentConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration())) {
                    nextConfigurationModel = currentConfigurationModelChild;
                }

            }

            stepRunSimulationModel.loadConfiguration(nextConfigurationModel);

            //Loaded configuration
            ConfigurationModel newCurrentConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            TapeModel currentTapeModel = stepRunSimulationModel.getCurrentTapeModel();
            StackModel currentStackModel = stepRunSimulationModel.getCurrentStackModel();
            List<ConfigurationModel> rootChildrenConfigurationList = stepRunSimulationModel.configurationApplicable(newCurrentConfigurationModel.getCurrentStateModel(), currentTapeModel.getAtHead(), currentStackModel.peak());
            newCurrentConfigurationModel.setChildrenConfigurations(rootChildrenConfigurationList);

            for (ConfigurationModel nextConfigurationModelChild : nextConfigurationModel.getChildrenConfigurations()) {
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(nextConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }
            updateDiagramViewForSelectedConfiguration(newCurrentConfigurationModel);
            updateTapeViewForSelectedConfiguration(newCurrentConfigurationModel);
            updateStackViewForSelectedConfiguration(newCurrentConfigurationModel);
        }
    }
}
