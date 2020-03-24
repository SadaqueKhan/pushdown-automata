package app.presenter;

import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.model.TransitionModel;
import app.view.QuickRunSimulationStage;
import app.view.StepRunSimulationStage;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
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

        simulationStage = new Stage();

        if (typeOfSimulation.equals("By Quick Run")) {
            this.quickRunSimulationStage = new QuickRunSimulationStage(this);

            quickRunSimulationModel = new SimulationModel(machineModel, inputWord);

            int flag = quickRunSimulationModel.createTree();

            if (flag == 200) {
                triggerAlgorithmScene();
                String simulationStatsString;
                if (quickRunSimulationModel.isNFA()) {
                    simulationStatsString = "Type: " + "NFA" + " " + "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
                } else {
                    simulationStatsString = "Type: " + "DFA" + " " + "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + " " + "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
                }
                quickRunSimulationStage.getSimulationStatsTextField().setText(simulationStatsString);

            }
            //Create a new scene to render simulation
            simulationStage.setTitle("Simulation: Quick Run");
            Scene scene = new Scene(quickRunSimulationStage, 550, 500);
            simulationStage.setScene(scene);

        }


        if (typeOfSimulation.equals("By Step Run")) {
            this.stepRunSimulationModel = new SimulationModel(machineModel, inputWord);
            // Load root configuration
            ConfigurationModel rootConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            List<ConfigurationModel> rootChildrenConfigurationList = stepRunSimulationModel.findChildrenConfigurations(rootConfigurationModel);
            rootConfigurationModel.setChildrenConfigurations(rootChildrenConfigurationList);


            //Load view
            simulationStage.setTitle("Simulation: Step Run");
            this.stepRunSimulationStage = new StepRunSimulationStage(this);
            stepRunSimulationStage.getCurrentConfigTextField().setText("Current configuration: " + rootConfigurationModel.toString());
            for (ConfigurationModel rootConfigurationModelChild : rootChildrenConfigurationList) {
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(rootConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }

            //Create a new scene to render simulation
            Scene scene = new Scene(stepRunSimulationStage, 550, 500);
            simulationStage.setScene(scene);
        }

        mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(true);
        mainStagePresenter.getMainStage().getInputTextField().setDisable(true);
        simulationStage.setResizable(false);
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


    public void triggerAlgorithmScene() {
        ListView<ConfigurationModel> algorithmListView = quickRunSimulationStage.getAlgorithmlistView();

        if (algorithmListView.getItems().isEmpty()) {
            // Get algorithm path list
            ArrayList<ConfigurationModel> algorithmPathList = quickRunSimulationModel.getComputationTreeArrayList();
            algorithmListView.getItems().addAll(algorithmPathList);

            Platform.runLater(() -> algorithmListView.setCellFactory(new Callback<ListView<ConfigurationModel>, ListCell<ConfigurationModel>>() {
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

                                String itemToPrint = "";

                                if (item.getParentConfiguration() == null) {
                                    // create the string for the root node configuration in the tree
                                    itemToPrint += "At the start state: " + item.getCurrentStateModel().getStateId();
                                } else {
                                    // Create string of the position of the configuration in the tree search
                                    String positionInTreeString = "depth " + item.getDepth() + ":branch " + item.getBranch() + ": ";
                                    String transitionTakenToReachConfigString = item.getTransitionModelTakenToReachCurrentConfiguration().toString();
                                    String configurationReachedString = item.toString();
                                    itemToPrint += positionInTreeString + transitionTakenToReachConfigString + " -> " + configurationReachedString;
                                }


                                int index = getIndex();

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
        //Remove current node at the center of the stage
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().remove(0);
        //Add the algorithm node to the center of the stage
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().add(quickRunSimulationStage.getAlgorithmlistView());
    }


    public void triggerPathsScene() {
        //Get accordion
        Accordion accordion = (Accordion) quickRunSimulationStage.getPathsVBox().getChildren().get(0);

        if (accordion.getPanes().isEmpty()) {
            //Get leaf configurations
            ArrayList<ConfigurationModel> leafConfigurationPath = quickRunSimulationModel.getLeafConfigurationArrayList();
            accordion.getPanes().clear();

            int numPath = 0;

            for (ConfigurationModel leafConfigurationModel : leafConfigurationPath) {
                ListView<ConfigurationModel> newListView = new ListView<>();
                newListView.getItems().addAll(leafConfigurationModel.getPath());
                setListenerForTransitionListView(newListView);
                ++numPath;
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
        }

        quickRunSimulationStage.getContainerForCenterNodes().getChildren().remove(0);
        quickRunSimulationStage.getContainerForCenterNodes().getChildren().add(quickRunSimulationStage.getPathsScrollPane());
    }


    public void createSuccessSimulationStage(ConfigurationModel doubleClickConfiguration) {

        ListView<ConfigurationModel> transitionsTakenListView = new ListView<>();

        transitionsTakenListView.getItems().addAll(doubleClickConfiguration.getPath());
        setListenerForTransitionListView(transitionsTakenListView);

        Platform.runLater(() -> transitionsTakenListView.setCellFactory(new Callback<ListView<ConfigurationModel>, ListCell<ConfigurationModel>>() {
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

                            String itemToPrint = "";
                            if (item.getParentConfiguration() == null) {
                                // create the string for the root node configuration in the tree
                                itemToPrint += "At the start state: " + item.getCurrentStateModel().getStateId();
                            } else {
                                // Create string of the position of the configuration in the tree search
                                String positionInTreeString = "depth " + item.getDepth() + ":branch " + item.getBranch() + ": ";
                                String transitionTakenToReachConfigString = item.getTransitionModelTakenToReachCurrentConfiguration().toString();
                                String configurationReachedString = item.toString();
                                itemToPrint += positionInTreeString + transitionTakenToReachConfigString + " -> " + configurationReachedString;
                            }
                            setText(itemToPrint);
                        }
                    }
                };
            }
        }));


        String windowTitle = "Path";
        if (doubleClickConfiguration.isSuccessConfig()) {
            windowTitle = "Success Path";
        }

        //Create a new scene to render simulation
        Scene scene = new Scene(transitionsTakenListView, 750, 500);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(simulationStage);
        stage.setResizable(false);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();
    }


    public void stepForward() {

        // Get the step run transition list
        ListView<TransitionModel> listView = stepRunSimulationStage.getTransitionOptionsListView();
        // Get the selected transition to move to from the transition list
        ObservableList<TransitionModel> selectedTransitionModelObservableList = listView.getSelectionModel().getSelectedItems();
        // Store the selected transition to move to into transition model instance
        TransitionModel selectedTransitionModel = selectedTransitionModelObservableList.get(0);

        if (selectedTransitionModel != null) {
            // Clear the current transition list view
            listView.getItems().clear();
            // Move to the next configuration
            stepRunSimulationModel.next(selectedTransitionModel);
            // Search for the next configuration that can be reached given the selected transition to move to
            ConfigurationModel nextConfigurationModel = stepRunSimulationModel.getCurrentConfig();

            // Find all applicable transitions from the current configuration to a next configuration
            for (ConfigurationModel nextConfigurationModelChild : nextConfigurationModel.getChildrenConfigurations()) {
                // Render each next applicable transition onto a the step run transition list
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(nextConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }

            String style = "-fx-background-color: rgba(238,238,238,0.5);";
            String currentConfigTextFieldString = "Current configuration: " + stepRunSimulationModel.getCurrentConfig().toString();

            if (nextConfigurationModel.isSuccessConfig()) {
                currentConfigTextFieldString += "[SUCCESS]";
                style = "-fx-background-color: rgba(39,255,0,0.5);";
            } else if (nextConfigurationModel.isFailConfig()) {
                currentConfigTextFieldString += "[FAIL]";
                style = "-fx-background-color: rgba(255,0,56,0.5);";
            } else if (nextConfigurationModel.isStuckConfig()) {
                currentConfigTextFieldString += "[STUCK]";
                style = "-fx-background-color: rgba(255,138,0,0.5);";
            }

            stepRunSimulationStage.getCurrentConfigTextField().setText(currentConfigTextFieldString);
            stepRunSimulationStage.setStyle(style);
            stepRunSimulationStage.setBackground(Background.EMPTY);

            // Highlight diagram scene
            updateDiagramViewForSelectedConfiguration(nextConfigurationModel);
            // Update tape scene
            updateTapeViewForSelectedConfiguration(nextConfigurationModel);
            // Update stack scene
            updateStackViewForSelectedConfiguration(nextConfigurationModel);
        }
    }

    public void stepBack() {
        ListView<TransitionModel> listView = stepRunSimulationStage.getTransitionOptionsListView();
        if (stepRunSimulationModel.getCurrentConfig().getParentConfiguration() != null) {
            listView.getItems().clear();
            stepRunSimulationModel.previous();
            ConfigurationModel prevConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            for (ConfigurationModel currentConfigurationModelChild : prevConfigurationModel.getChildrenConfigurations()) {
                stepRunSimulationStage.getTransitionOptionsListView().getItems().add(currentConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }

            String style = "-fx-background-color: rgba(238,238,238,0.5);";
            String currentConfigTextFieldString = "Current configuration: " + stepRunSimulationModel.getCurrentConfig().toString();

            if (prevConfigurationModel.isSuccessConfig()) {
                currentConfigTextFieldString += "[SUCCESS]";
                style = "-fx-background-color: rgba(39,255,0,0.5);";
            } else if (prevConfigurationModel.isFailConfig()) {
                currentConfigTextFieldString += "[FAIL]";
                style = "-fx-background-color: rgba(255,0,56,0.5);";
            } else if (prevConfigurationModel.isStuckConfig()) {
                currentConfigTextFieldString += "[STUCK]";
                style = "-fx-background-color: rgba(255,138,0,0.5);";
            }

            stepRunSimulationStage.getCurrentConfigTextField().setText(currentConfigTextFieldString);
            stepRunSimulationStage.setStyle(style);
            stepRunSimulationStage.setBackground(Background.EMPTY);

            updateDiagramViewForSelectedConfiguration(prevConfigurationModel);
            updateTapeViewForSelectedConfiguration(prevConfigurationModel);
            updateStackViewForSelectedConfiguration(prevConfigurationModel);
        }
    }


    //Auxiliary method

    // Highlight diagram scene
    public void updateDiagramViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        DiagramPresenter diagramPresenter = mainStagePresenter.getDiagramPresenter();
        diagramPresenter.highlightTransitionTakenInDiagram(selectedConfiguration);
    }

    // Highlight tape scene
    public void updateTapeViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateTapeView(selectedConfiguration.getHeadPosition());
    }

    // Highlight stack scene
    public void updateStackViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateStackView(selectedConfiguration.getStackContent());
    }

    // Create listener for items in new transition list view, to help highlight diagram scene/tape scene/stack scene
    private void setListenerForTransitionListView(ListView<ConfigurationModel> newListView) {
        newListView.setOnMouseReleased(event -> {
            ObservableList<ConfigurationModel> selectedConfigurationsToHighlightList = newListView.getSelectionModel().getSelectedItems();
            if (!(selectedConfigurationsToHighlightList.isEmpty())) {
                ConfigurationModel selectedConfiguration = selectedConfigurationsToHighlightList.get(0);
                updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                updateTapeViewForSelectedConfiguration(selectedConfiguration);
                updateStackViewForSelectedConfiguration(selectedConfiguration);
            }
        });
    }

}
