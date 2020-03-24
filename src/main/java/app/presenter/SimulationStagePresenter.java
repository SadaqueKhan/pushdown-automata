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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Presenter retrieves data from repositories (the model), and formats it for display in the simulation stage.
 * </p>
 */
public class SimulationStagePresenter {
    private final MainStagePresenter mainStagePresenter;
    private final Stage simulationStage;
    private QuickRunSimulationScene quickRunSimulationScene;
    private StepRunSimulationScene stepRunSimulationScene;
    private SimulationModel quickRunSimulationModel;
    private SimulationModel stepRunSimulationModel;
    /**
     * Constructor of the simulation stage presenter, used to instantiate an instance of the presenter.
     * @param mainStagePresenter
     * @param machineModel
     * @param inputWord
     * @param typeOfSimulation
     */
    SimulationStagePresenter(MainStagePresenter mainStagePresenter, MachineModel machineModel, String inputWord, String typeOfSimulation) {
        this.mainStagePresenter = mainStagePresenter;
        this.simulationStage = new Stage();
        if (typeOfSimulation.equals("By Quick Run")) {
            this.quickRunSimulationScene = new QuickRunSimulationScene(this);
            quickRunSimulationModel = new SimulationModel(machineModel, inputWord);
            int flag = quickRunSimulationModel.createTree();
            if (flag == 200) {
                loadAlgorithmScene();
                String simulationStatsString = "Simulation Facts - " + "\n";
                if (quickRunSimulationModel.isNFA()) {
                    simulationStatsString += "\n" + "Type: " + "NFA" + "\n";
                } else {
                    simulationStatsString += "\n" + "Type: " + "DFA" + "\n";
                }
                simulationStatsString += "\n" + "Input alphabet: " + machineModel.getInputAlphabetSet() + "\n" + "Stack alphabet: " + machineModel.getInputAlphabetSet() + "\n";
                simulationStatsString += "\n" +
                        "Success paths: " + quickRunSimulationModel.getNumOfPossibleSuccessPaths() + "\n" +
                        "Fail paths: " + quickRunSimulationModel.getNumOfPossibleFailPaths() + "\n" +
                        "Stuck paths: " + quickRunSimulationModel.getNumOfPossibleStuckPaths() + "\n" +
                        "Possible infinite paths: " + quickRunSimulationModel.getNumOfPossibleInfinitePaths();
                quickRunSimulationScene.getSimulationStatsLabel().setText(simulationStatsString);
            }
            //Create a new scene to render simulation
            simulationStage.setTitle("Simulation: Quick Run");
            Scene scene = new Scene(quickRunSimulationScene, 550, 500);
            simulationStage.setScene(scene);
        }
        if (typeOfSimulation.equals("By Step Run")) {
            this.stepRunSimulationModel = new SimulationModel(machineModel, inputWord);
            // Load root configuration
            ConfigurationModel rootConfigurationModel = stepRunSimulationModel.getCurrentConfig();
            List<ConfigurationModel> rootChildrenConfigurationList = stepRunSimulationModel.findChildrenConfigurations(rootConfigurationModel);
            rootConfigurationModel.setChildrenConfigurations(rootChildrenConfigurationList);
            //Load view
            this.stepRunSimulationScene = new StepRunSimulationScene(this);
            stepRunSimulationScene.getCurrentConfigTextField().setText("Current configuration: " + rootConfigurationModel.toString());
            for (ConfigurationModel rootConfigurationModelChild : rootChildrenConfigurationList) {
                stepRunSimulationScene.getTransitionOptionsListView().getItems().add(rootConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration());
            }
            //Load scene onto the stage.
            simulationStage.setTitle("Simulation: Step Run");
            Scene scene = new Scene(stepRunSimulationScene, 550, 500);
            simulationStage.setScene(scene);
        }
        mainStagePresenter.getMainStage().getMenuBar().setDisable(true);
        mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(true);
        mainStagePresenter.getMainStage().getInputTextField().setDisable(true);
        simulationStage.setResizable(false);
        simulationStage.show();
        simulationStage.setOnCloseRequest(event -> {
            mainStagePresenter.getMainStage().getMenuBar().setDisable(false);
            mainStagePresenter.getMainStage().getContainerForCenterNodes().setDisable(false);
            mainStagePresenter.getMainStage().getInputTextField().setDisable(false);
            // Notify other scenes.
            mainStagePresenter.updateStackScene(new ArrayList<>());
            mainStagePresenter.updateTapeScene(0);
            mainStagePresenter.getDiagramPresenter().removeHighlightedTransitionView();
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
            ArrayList<ConfigurationModel> leafConfigurationPath = quickRunSimulationModel.getLeafConfigurationArrayList();
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
     * node selected by the user in the computation tree listing found in the algorithm scene.
     * @param doubleClickConfiguration
     */
    public void createIndependentPathSimulationStage(ConfigurationModel doubleClickConfiguration) {
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
        //Create a new scene to render computation path. ¬
        Scene scene = new Scene(transitionsTakenListView, 750, 500);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(simulationStage);
        stage.setResizable(false);
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
            stepRunSimulationScene.setBackground(Background.EMPTY);
            // Highlight diagram scene
            updateDiagramViewForSelectedConfiguration(nextConfigurationModel);
            // Update tape scene
            updateTapeViewForSelectedConfiguration(nextConfigurationModel);
            // Update stack scene
            updateStackViewForSelectedConfiguration(nextConfigurationModel);
        }
    }
    /**
     * Request to the diagram presenter to update its view for which it controls.
     * @param selectedConfiguration
     */
    public void updateDiagramViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        DiagramPresenter diagramPresenter = mainStagePresenter.getDiagramPresenter();
        diagramPresenter.highlightTransitionTakenInDiagram(selectedConfiguration);
    }
    /**
     * Request to the main stage presenter to update the tape scene for which it controls.
     * @param selectedConfiguration
     */
    public void updateTapeViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateTapeScene(selectedConfiguration.getHeadPosition());
    }
    // Highlight tape scene
    /**
     * Request to the main stage presenter to update the stack scene for which it controls.
     * @param selectedConfiguration
     */
    public void updateStackViewForSelectedConfiguration(ConfigurationModel selectedConfiguration) {
        mainStagePresenter.updateStackScene(selectedConfiguration.getStackContent());
    }
    /**
     * Moves the step run simulation back to a previous configuration, by computing the previous configuration
     * using the repository of models and formats it to display in the step-run scene.
     */
    public void stepBack() {
        ListView<TransitionModel> listView = stepRunSimulationScene.getTransitionOptionsListView();
        if (stepRunSimulationModel.getCurrentConfig().getParentConfiguration() != null) {
            listView.getItems().clear();
            stepRunSimulationModel.previous();
            ConfigurationModel prevConfigurationModel = stepRunSimulationModel.getCurrentConfig();
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
            stepRunSimulationScene.setBackground(Background.EMPTY);
            updateDiagramViewForSelectedConfiguration(prevConfigurationModel);
            updateTapeViewForSelectedConfiguration(prevConfigurationModel);
            updateStackViewForSelectedConfiguration(prevConfigurationModel);
        }
    }
    /**
     * Create dynamic listener for items in lists found in the simulation stage to be highlighted in the diagram
     * scene/tape/stack scene.
     * @param newListView
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
}
