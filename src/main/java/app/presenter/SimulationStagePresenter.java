package app.presenter;
import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.SimulationModel;
import app.model.TransitionModel;
import app.view.QuickRunSimulationScene;
import app.view.StepRunSimulationScene;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Simulation stage presenter retrieves data from repositories (the model), and formats it for display in the
 * simulation stage.
 * </p>
 */
public class SimulationStagePresenter {
    private final MainStagePresenter mainStagePresenter;
    private final Stage simulationStage;
    private QuickRunSimulationScene quickRunSimulationScene;
    private StepRunSimulationScene stepRunSimulationScene;
    private SimulationModel quickRunSimulationModel;
    private SimulationModel stepRunSimulationModel;
    private int stepCounter = 0;
    /**
     * Constructor of the simulation stage presenter, used to instantiate an instance of the presenter.
     * @param mainStagePresenter the presenter which needs to be notified about events on the simulation scenes.
     * @param machineModel the model containing the data about the pushdown automaton machine.
     * @param inputWord the input word to be simulated.
     * @param typeOfSimulation the type of simulation to be run, either quick run or step run.
     */
    public SimulationStagePresenter(MainStagePresenter mainStagePresenter, MachineModel machineModel, String inputWord,
                                    String typeOfSimulation) {
        this.mainStagePresenter = mainStagePresenter;
        this.simulationStage = new Stage();
        if (typeOfSimulation.equals("By Quick Run")) {
            this.quickRunSimulationScene = new QuickRunSimulationScene(this);
            quickRunSimulationModel = new SimulationModel(machineModel, inputWord);
            quickRunSimulationModel.createTree();
            loadAlgorithmScene();
            String simulationStatsString = "";
            if (quickRunSimulationModel.isNFA()) {
                simulationStatsString += "Type: " + "NFA" + " | ";
            } else {
                simulationStatsString += "Type: " + "DFA" + " | ";
            }
            simulationStatsString += "Input alphabet: " + machineModel.getInputAlphabetSet() + " | " +
                    "Stack " +
                    "alphabet: " + machineModel.getInputAlphabetSet() + " | ";
            simulationStatsString +=
                    "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + " | " +
                            "Fail paths: " + quickRunSimulationModel.getNumOfPossibleFailPaths() + " | " +
                            "Stuck paths: " + quickRunSimulationModel.getNumOfPossibleStuckPaths() + " | " +
                            "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
            quickRunSimulationScene.getSimulationStatsLabel().setText(simulationStatsString);
            //Create a new scene to render simulation
            simulationStage.setTitle("Simulation: Quick Run");
            Scene scene = new Scene(quickRunSimulationScene, 550, 500);
            simulationStage.setScene(scene);
        }
        if (typeOfSimulation.equals("By Step Run")) {
            // Load simulation model
            this.stepRunSimulationModel = new SimulationModel(machineModel, inputWord);
            ConfigurationModel rootConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            //Load view
            this.stepRunSimulationScene = new StepRunSimulationScene(this);
            stepRunSimulationScene.getCurrentConfigTextField().setText("Current configuration: " + rootConfigurationModel.toString());
            for (ConfigurationModel rootConfigurationModelChild : rootConfigurationModel.getChildrenConfigurations()) {
                stepRunSimulationScene.getTransitionOptionsListView().getItems().add(rootConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }
            VBox historyVBox = stepRunSimulationScene.getHistoryVBox();
            String stepToPrint = "At the start configuration" + " : " + " -> " + rootConfigurationModel
                    .toString();
            Label newStepLabel = new Label();
            newStepLabel.setText(stepToPrint);
            historyVBox.getChildren().add(newStepLabel);
            //Load scene onto the stage.
            simulationStage.setTitle("Simulation: Step Run");
            Scene scene = new Scene(stepRunSimulationScene, 550, 500);
            simulationStage.setScene(scene);
        }
        mainStagePresenter.getMainScene().getMenuBar().setDisable(true);
        mainStagePresenter.getMainScene().getContainerForCenterNodes().setDisable(true);
        mainStagePresenter.getMainScene().getInputTextField().setDisable(true);
        simulationStage.show();
        simulationStage.setOnCloseRequest(event -> {
            mainStagePresenter.getMainScene().getMenuBar().setDisable(false);
            mainStagePresenter.getMainScene().getContainerForCenterNodes().setDisable(false);
            mainStagePresenter.getMainScene().getInputTextField().setDisable(false);
            // Notify other scenes.
            mainStagePresenter.updateStackScene(new ArrayList<>());
            mainStagePresenter.updateTapeScene(0);
            mainStagePresenter.getDiagramScenePresenter().removeHighlightedTransitionView();
        });
    }
    /**
     * Handles the loading of the algorithm scene onto the simulation stage.
     */
    public void loadAlgorithmScene() {
        ListView<ConfigurationModel> algorithmListView = quickRunSimulationScene.getAlgorithmlistView();
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
                                // Create string of the position of the configuration in the tree search
                                String positionInTreeString = "depth " + item.getDepth() + ":branch " + item.getBranch() + ": ";
                                String configurationReachedString = item.toString();
                                if (item.getParentConfiguration() == null) {
                                    // create the string for the root node configuration in the tree
                                    itemToPrint += positionInTreeString + " -> " + configurationReachedString;
                                } else {
                                    // Create string of the position of the configuration in the tree search
                                    String transitionTakenToReachConfigString = item.getTransitionModelTakenToReachCurrentConfiguration().toString();
                                    itemToPrint += positionInTreeString + transitionTakenToReachConfigString + " -> " + configurationReachedString;
                                }
                                int index = getIndex();
                                setStyle("-fx-control-inner-background: " + "derive(#eeeeee, 100%);");
                                if (item.isInfiniteConfig()) {
                                    setStyle("-fx-control-inner-background: " + "derive(#d469ff, 50%);");
                                    itemToPrint = item.toString() + "  (Possible infinite path, backtracking to last " +
                                            "configuration!)";
                                }
                                if (algorithmPathList.lastIndexOf(item) == index) {
                                    setStyle("-fx-control-inner-background: " + "derive(#ff6c5c, 50%);");
                                    itemToPrint += " (No more configurations reachable beyond this point!)";
                                } else if (algorithmPathList.indexOf(item) < index && index < algorithmPathList.lastIndexOf(item)) {
                                    setStyle("-fx-control-inner-background: " + "derive(#aedaff, 70%);");
                                    itemToPrint += " (New configuration found beyond this point!)";
                                }
                                if (item.getParentConfiguration() == null) {
                                    setStyle("-fx-control-inner-background: " + "derive(#eeeeee, 100%);");
                                }
                                if (item.isSuccessConfig()) {
                                    setStyle("-fx-control-inner-background: " + "derive(#b3ff05, 50%);");
                                    itemToPrint += "(Success!)";
                                }
                                setText(itemToPrint);
                            }
                        }
                    };
                }
            }));
        }
        //Remove current node at the center of the stage
        quickRunSimulationScene.getContainerForCenterNodes().getChildren().remove(0);
        //Add the algorithm node to the center of the stage
        quickRunSimulationScene.getContainerForCenterNodes().getChildren().add(quickRunSimulationScene.getAlgorithmlistView());
    }
    /**
     * Handles the loading of the path scene onto the simulation stage.
     */
    public void loadPathsScene() {
        //Retrieve accordion container found in the path scene.
        Accordion accordion = (Accordion) quickRunSimulationScene.getPathsVBox().getChildren().get(0);
        if (accordion.getPanes().isEmpty()) {
            //Get leaf configurations computed in the simulation model.
            ArrayList<ConfigurationModel> leafConfigurationPath = quickRunSimulationModel.getComputationPathArrayList();
            accordion.getPanes().clear();
            int numPath = 0;
            for (ConfigurationModel leafConfigurationModel : leafConfigurationPath) {
                ListView<ConfigurationModel> newListView = new ListView<>();
                newListView.getItems().addAll(leafConfigurationModel.getPath());
                Platform.runLater(() -> newListView.setCellFactory(new Callback<ListView<ConfigurationModel>, ListCell<ConfigurationModel>>() {
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
                                    // Create string of the position of the configuration in the tree search
                                    String positionInTreeString = "depth " + item.getDepth() + ":branch " + item.getBranch() + ": ";
                                    String configurationReachedString = item.toString();
                                    if (item.getParentConfiguration() == null) {
                                        // Create the string for the root node configuration in the computation tree..
                                        itemToPrint += positionInTreeString + " -> " + configurationReachedString;
                                    } else {
                                        // Create string of the position of the configuration in the computation tree.
                                        String transitionTakenToReachConfigString = item.getTransitionModelTakenToReachCurrentConfiguration().toString();
                                        itemToPrint += positionInTreeString + transitionTakenToReachConfigString + " -> " + configurationReachedString;
                                    }
                                    setText(itemToPrint);
                                }
                            }
                        };
                    }
                }));
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
        quickRunSimulationScene.getContainerForCenterNodes().getChildren().remove(0);
        quickRunSimulationScene.getContainerForCenterNodes().getChildren().add(quickRunSimulationScene.getPathsScrollPane());
    }
    /**
     * Handles the loading of a secondary stage to show the path from the root configuration node to the configuration
     * node selected by the user in the algorithm listing in the quick run scene.
     * @param doubleClickConfiguration the configuration node chosen for its path from the root configuration node to
     * be rendered.
     */
    public void createConfigurationNodePathStage(ConfigurationModel doubleClickConfiguration) {
        ListView<ConfigurationModel> configuratioNodePathListView = new ListView<>();
        configuratioNodePathListView.getItems().addAll(doubleClickConfiguration.getPath());
        setListenerForTransitionListView(configuratioNodePathListView);
        Platform.runLater(() -> configuratioNodePathListView.setCellFactory(new Callback<ListView<ConfigurationModel>, ListCell<ConfigurationModel>>() {
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
                            String configurationReachedString = item.toString();
                            if (item.getParentConfiguration() == null) {
                                // create the string for the root node configuration in the tree
                                itemToPrint += " -> " + configurationReachedString;
                            } else {
                                // Create string of the position of the configuration in the tree search
                                String positionInTreeString = "depth " + item.getDepth() + ":branch " + item.getBranch() + ": ";
                                String transitionTakenToReachConfigString = item.getTransitionModelTakenToReachCurrentConfiguration().toString();
                                itemToPrint += positionInTreeString + transitionTakenToReachConfigString + " -> " +
                                        configurationReachedString;
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
        //Create a new scene to render computation path. ¬
        Scene scene = new Scene(configuratioNodePathListView, 750, 500);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(simulationStage);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Moves the step run simulation forward to the next applicable configuration, by computing the next configuration
     * using the repository of models and formats it to display in the step-run scene.
     */
    public void stepForward() {
        // Get the step run transition list
        ListView<TransitionModel> listView = stepRunSimulationScene.getTransitionOptionsListView();
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
            // Update simulation computation list
            stepRunSimulationModel.getComputationTreeArrayList().add(nextConfigurationModel);
            // Update history list view
            updateHistoryListView("Forward");
            // Find all applicable transitions from the current configuration to a next configuration
            for (ConfigurationModel nextConfigurationModelChild : nextConfigurationModel.getChildrenConfigurations()) {
                // Render each next applicable transition onto a the step run transition list
                stepRunSimulationScene.getTransitionOptionsListView().getItems().add(nextConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
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
            stepRunSimulationScene.getCurrentConfigTextField().setText(currentConfigTextFieldString);
            stepRunSimulationScene.setStyle(style);
            // Highlight diagram scene
            updateDiagramViewForSelectedConfiguration(nextConfigurationModel);
            // Update tape scene
            updateTapeViewForSelectedConfiguration(nextConfigurationModel);
            // Update stack scene
            updateStackViewForSelectedConfiguration(nextConfigurationModel);
        }
    }
    /**
     * Handles the updating of a new item in the step run history list when the user move backward/forward in a
     * step run simulation.
     */
    private void updateHistoryListView(String typeOfStep) {
        if (typeOfStep.equals("Backward")) {
            --stepCounter;
            if (stepCounter == 0) {
                ConfigurationModel nextConfigurationModel = stepRunSimulationModel.getCurrentConfig();
                VBox historyVBox = stepRunSimulationScene.getHistoryVBox();
                String stepToPrint = "At the start configuration" + " : " + " -> " + nextConfigurationModel
                        .toString();
                Label newStepLabel = new Label();
                newStepLabel.setText(stepToPrint);
                historyVBox.getChildren().add(newStepLabel);
            }
        } else {
            ConfigurationModel nextConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            ++stepCounter;
            VBox historyVBox = stepRunSimulationScene.getHistoryVBox();
            String stepToPrint = "Step " + stepCounter + "At the start configuration" + " : " +
                    "" + " -> " +
                    nextConfigurationModel
                            .toString();
            Label newStepLabel = new Label();
            if (nextConfigurationModel.getParentConfiguration() != null) {
                stepToPrint = "";
                String transitionTakenToReachConfigString = nextConfigurationModel.getTransitionModelTakenToReachCurrentConfiguration().toString();
                stepToPrint += "Step " + stepCounter + " : " +
                        transitionTakenToReachConfigString + " -> " +
                        nextConfigurationModel.toString();
                if (nextConfigurationModel.isSuccessConfig()) {
                    newStepLabel.setStyle("-fx-background-color: rgba(39,255,0,0.5);");
                    stepToPrint += "[SUCCESS]";
                } else if (nextConfigurationModel.isFailConfig()) {
                    newStepLabel.setStyle("-fx-background-color: rgba(255,0,56,0.5);");
                    stepToPrint += "[FAIL]";
                } else if (nextConfigurationModel.isStuckConfig()) {
                    newStepLabel.setStyle("-fx-background-color: rgba(255,138,0,0.5);");
                    stepToPrint += "[STUCK]";
                } else {
                    newStepLabel.setStyle("-fx-background-color: rgba(255,255,255,0.5);");
                }
            }
            newStepLabel.setText(stepToPrint);
            historyVBox.getChildren().add(newStepLabel);
        }
    }
    /**
     * Create dynamic listener for items in lists found in the simulation stage to be highlighted in the diagram
     * scene/tape/stack scene.
     * @param newListView the UI component for which a dynamic listener is added to.
     */
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
    /**
     * Request to the diagram presenter to update its view for which it controls.
     * @param selectedConfiguration used to inform the update.
     */
    public void updateDiagramViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        DiagramScenePresenter diagramScenePresenter = mainStagePresenter.getDiagramScenePresenter();
        diagramScenePresenter.highlightTransitionTakenInDiagram(selectedConfiguration);
    }
    // Highlight tape scene
    /**
     * Request to the main stage presenter to update the tape scene for which it controls.
     * @param selectedConfiguration used to inform the update.
     */
    public void updateTapeViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateTapeScene(selectedConfiguration.getHeadPosition());
    }
    /**
     * Moves the step run simulation back to a previous configuration, by computing the previous configuration
     * using the repository of models and formats it to display in the step-run scene.
     */
    public void stepBackward() {
        ListView<TransitionModel> listView = stepRunSimulationScene.getTransitionOptionsListView();
        if (stepRunSimulationModel.getCurrentConfig().getParentConfiguration() != null) {
            //Update step run model
            stepRunSimulationModel.previous();
            ConfigurationModel prevConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            stepRunSimulationModel.getComputationTreeArrayList().add(prevConfigurationModel);
            //Update step run view
            updateHistoryListView("Backward");
            listView.getItems().clear();
            for (ConfigurationModel currentConfigurationModelChild : prevConfigurationModel.getChildrenConfigurations()) {
                stepRunSimulationScene.getTransitionOptionsListView().getItems().add(currentConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
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
            stepRunSimulationScene.getCurrentConfigTextField().setText(currentConfigTextFieldString);
            stepRunSimulationScene.setStyle(style);
            updateDiagramViewForSelectedConfiguration(prevConfigurationModel);
            updateTapeViewForSelectedConfiguration(prevConfigurationModel);
            updateStackViewForSelectedConfiguration(prevConfigurationModel);
        }
    }
    /**
     * Request to the main stage presenter to update the stack scene for which it controls.
     * @param selectedConfiguration used to inform the update.
     */
    public void updateStackViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateStackScene(selectedConfiguration.getStackContent());
    }
    public QuickRunSimulationScene getQuickRunSimulationScene() {
        return quickRunSimulationScene;
    }
    public StepRunSimulationScene getStepRunSimulationScene() {
        return stepRunSimulationScene;
    }
}
