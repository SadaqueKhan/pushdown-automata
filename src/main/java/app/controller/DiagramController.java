package app.controller;

import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.DiagramView;
import app.view.MainStageView;
import app.view.StateView;
import app.view.TransitionView;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class DiagramController {

    private final MainStageView mainStageView;
    private final MainStageController mainStageController;
    private final MachineModel machineModel;

    private TransitionTableController transitionTableController;

    private final DiagramView diagramView;

    // Defines the x and y coordinate of the translation that is added to this {@code Node}'transform for the purpose of layout.
    private double layoutX;
    private double layoutY;

    private double sceneX;
    private double sceneY;
    private TransitionModel transitionModeIsHighlighted;


    public DiagramController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {
        this.mainStageController = mainStageController;
        this.mainStageView = mainStageView;
        this.machineModel = machineModel;
        this.diagramView = new DiagramView(this, mainStageView);
    }


    public void loadDiagramViewOntoStage(TransitionTableController transitionTableController) {
        this.transitionTableController = transitionTableController;
        this.mainStageView.getContainerForCenterNodes().getChildren().add(diagramView);
    }


    public void loadStatesOntoDiagram() {
        Map<String, StateView> stateMap = diagramView.getStateMap();
        for (StateModel stateModelToLoad : machineModel.getStateModelSet()) {
            diagramView.addStateView(ThreadLocalRandom.current().nextInt(0, 1275 + 1), ThreadLocalRandom.current().nextInt(0, 450 + 1), this, stateModelToLoad.getStateId());
            StateView stateViewToLoad = stateMap.get(stateModelToLoad.getStateId());
            if (stateModelToLoad.isStartState()) {
                stateViewToLoad.toggleStartStateUIComponent(stateModelToLoad.isStartState());
            }
            if (stateModelToLoad.isFinalState()) {
                stateViewToLoad.toggleFinalStateUIComponent(stateModelToLoad.isFinalState());
            }
        }
    }

    public void loadTransitionsOntoDiagram() {
        for (TransitionModel transitionModelToLoad : machineModel.getTransitionModelSet()) {
            String currentStateModelToLoadID = transitionModelToLoad.getCurrentStateModel().getStateId();
            String resultingStateModelToLoadID = transitionModelToLoad.getResultingStateModel().getStateId();
            //Add transitionview onto diagram view
            if (currentStateModelToLoadID.equals(resultingStateModelToLoadID)) {
                addReflexiveTransitionToView(currentStateModelToLoadID, resultingStateModelToLoadID, transitionModelToLoad);
            } else {
                addDirectionalTransitionToView(currentStateModelToLoadID, resultingStateModelToLoadID, transitionModelToLoad);
            }
        }
    }

    public void addStateToViewTransitionTableEventResponse(double x, double y, String userEntryCurrentStateId) {
        diagramView.addStateView(x, y, this, userEntryCurrentStateId);
    }

    public void addStateToViewMouseEventResponse(double x, double y) {
        StateModel newStateModel = null;
        if (machineModel.getStateModelSet().isEmpty()) {
            newStateModel = new StateModel();
        } else {
            boolean stateModelExists = true;
            outerloop:
            while (stateModelExists) {
                newStateModel = new StateModel();
                for (StateModel stateModel : machineModel.getStateModelSet()) {
                    if (stateModel.equals(newStateModel)) {
                        continue outerloop;
                    }
                }
                stateModelExists = false;
            }
        }
        machineModel.addStateModelToStateModelSet(newStateModel);
        diagramView.addStateView(x, y, this, newStateModel.getStateId());
        transitionTableController.updateAvailableStateListForCombobox();
    }

    public void addDirectionalTransitionToView(String currentStateID, String resultingStateID, TransitionModel newTransitionModel) {
        diagramView.addDirectionalTransitionView(currentStateID, resultingStateID, getRelatedTransitions(newTransitionModel));
    }


    public void addReflexiveTransitionToView(String currentStateID, String resultingStateID, TransitionModel newTransitionModel) {
        diagramView.addReflexiveTransitionView(currentStateID, resultingStateID, getRelatedTransitions(newTransitionModel));
    }

    public void deleteMultipleTransitions(HashSet<TransitionModel> deletedTransitionModelsSet) {
        diagramView.deleteTransitionView(deletedTransitionModelsSet);
    }

    public HashSet<TransitionModel> getRelatedTransitions(TransitionModel transitionModel) {
        return machineModel.getRelatedTransitions(transitionModel);
    }

    //StateGUIEventResponses
    public void stateViewOnMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        sceneX = xPositionOfMouse;
        sceneY = yPositionOfMouse;

        layoutX = stateView.getLayoutX();
        layoutY = stateView.getLayoutY();
    }

    public void stateViewOnMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        // Offset of drag
        double offsetX = xPositionOfMouse - sceneX;
        double offsetY = yPositionOfMouse - sceneY;

        // Taking parent bounds
        Bounds parentBounds = stateView.getParent().getLayoutBounds();

        // Drag node bounds
        double currPaneLayoutX = stateView.getLayoutX();
        double currPaneWidth = stateView.getWidth();
        double currPaneLayoutY = stateView.getLayoutY();
        double currPaneHeight = stateView.getHeight();

        if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
            // If the dragNode bounds is within the parent bounds, then you can set the offset value.
            stateView.setTranslateX(offsetX);
        } else if (currPaneLayoutX + offsetX < 0) {
            // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
            // which makes the final layout position to 0 in mouse released event.
            stateView.setTranslateX(-currPaneLayoutX);
        } else {
            // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
            stateView.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
        }

        if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
            stateView.setTranslateY(offsetY);
        } else if (currPaneLayoutY + offsetY < 0) {
            stateView.setTranslateY(-currPaneLayoutY);
        } else {
            stateView.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
        }
    }

    public void stateViewOnMouseReleased(StateView stateView) {
        // Updating the new layout positions
        stateView.setLayoutX(layoutX + stateView.getTranslateX());
        stateView.setLayoutY(layoutY + stateView.getTranslateY());

        // Resetting the translate positions
        stateView.setTranslateX(0);
        stateView.setTranslateY(0);
    }


    public void stateViewContextMenuPopUp(StateView stateView) {

        StateModel stateModelSelected = machineModel.getStateModelFromStateModelSet(stateView.getStateID());

        //TODO need to MVC this
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");

        toggleStandardStateItem.setOnAction(e -> {
            stateModelSelected.setStartState(false);
            stateModelSelected.setFinalState(false);
            stateView.toggleStandardStateUIComponent();
        });

        toggleStartStateItem.setOnAction(e -> {
            if (stateModelSelected.isStartState()) {
                stateModelSelected.setStartState(false);
                stateView.toggleStartStateUIComponent(stateModelSelected.isStartState());
            } else {
                if (machineModel.findStartStateModel() != null) { // Check to see if start state exists in machine
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "Only one initial state allowed per machine. " + "State " + machineModel.findStartStateModel() + " is currently defined as the initial state for this machine.", ButtonType.OK);
                    invalidActionAlert.setHeaderText("Information");
                    invalidActionAlert.setTitle("Invalid Action");
                    invalidActionAlert.show();
                } else {
                    stateModelSelected.setStartState(true);
                    stateView.toggleStartStateUIComponent(stateModelSelected.isStartState());
                }
            }
        });
        toggleFinalStateItem.setOnAction(e -> {
            if (stateModelSelected.isFinalState()) {
                stateModelSelected.setFinalState(false);
                stateView.toggleFinalStateUIComponent(stateModelSelected.isFinalState());
            } else {
                stateModelSelected.setFinalState(true);
                stateView.toggleFinalStateUIComponent(stateModelSelected.isFinalState());
            }
        });


        //TODO need to remove this listeners logic
        createTransitionItem.setOnAction(e -> {
//Create input widgets for the user to enter a configuration
            TextField currentStateTextField = new TextField();
            currentStateTextField.setText(stateModelSelected.getStateId());
            currentStateTextField.setPrefWidth(65);
            // currentStateTextField.setEditable(false);
            currentStateTextField.setDisable(true);

            ComboBox<String> inputSymbolComboBox = new ComboBox<>();
            inputSymbolComboBox.setPrefWidth(65);
            inputSymbolComboBox.setEditable(true);
            inputSymbolComboBox.getItems().addAll(machineModel.getInputAlphabetSet());
            setUpComboBoxesListeners(inputSymbolComboBox);

            ComboBox<String> stackSymbolToPopComboBox = new ComboBox<>();
            stackSymbolToPopComboBox.setPrefWidth(65);
            stackSymbolToPopComboBox.setEditable(true);
            stackSymbolToPopComboBox.getItems().addAll(machineModel.getStackAlphabetSet());
            setUpComboBoxesListeners(stackSymbolToPopComboBox);

// Create a arrow label to connect the configuration input widgets to action input widgets
            final Label arrowLabel = new Label("->");
            arrowLabel.setPrefWidth(40);

//Create input widgets for the user to enter a configuration
            ComboBox<String> resultingStateComboBox = new ComboBox<>();
            resultingStateComboBox.setPrefWidth(110);
            resultingStateComboBox.setEditable(true);
            ArrayList<String> availableStateList = new ArrayList<>();
            for (StateModel availableStateModel : machineModel.getStateModelSet()) {
                availableStateList.add(availableStateModel.getStateId());
            }
            resultingStateComboBox.getItems().addAll(availableStateList);

            ComboBox<String> stackSymbolToPushComboBox = new ComboBox<>();
            stackSymbolToPushComboBox.setPrefWidth(65);
            stackSymbolToPushComboBox.setEditable(true);
            stackSymbolToPushComboBox.getItems().addAll(machineModel.getStackAlphabetSet());
            setUpComboBoxesListeners(stackSymbolToPushComboBox);

//Create submit button for the user to submit a transition
            Button submitTransitionButton = new Button("Submit");

            final HBox hBox = new HBox();
            hBox.setPadding(new Insets(10, 10, 10, 10));
            hBox.setSpacing(10);
            hBox.getChildren().addAll(currentStateTextField, inputSymbolComboBox, stackSymbolToPopComboBox, arrowLabel, resultingStateComboBox, stackSymbolToPushComboBox);

            final VBox vBox = new VBox(hBox, submitTransitionButton);
            vBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(vBox, 450, 150);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Create Transition");
            stage.setScene(scene);
            stage.show();

            submitTransitionButton.setOnAction(e1 -> {
                //User input for a configuration
                String userEntryCurrentState = currentStateTextField.getText();
                String userEntryInputSymbol = inputSymbolComboBox.getValue();
                String userEntryStackSymbolToPop = stackSymbolToPopComboBox.getValue();

                //User input for a action
                String userEntryResultingState = resultingStateComboBox.getValue();
                String userEntryStackSymbolToPush = stackSymbolToPushComboBox.getValue();

                if (((userEntryInputSymbol == null || userEntryInputSymbol.equals("")) || (userEntryStackSymbolToPop == null || userEntryStackSymbolToPop.equals("")) ||
                        (userEntryResultingState == null || userEntryResultingState.equals("")) || (userEntryStackSymbolToPush == null || userEntryStackSymbolToPush.equals("")))) {
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "All fields must be filled out to create a transition.", ButtonType.OK);
                    invalidActionAlert.setHeaderText("Information");
                    invalidActionAlert.setTitle("Invalid Action");
                    invalidActionAlert.show();
                    return;
                }

                //Update alphabets for machine
                machineModel.getInputAlphabetSet().add(userEntryInputSymbol);
                machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPop);
                machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPush);

                StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingState);

                if (resultingStateModel == null) {
                    resultingStateModel = new StateModel(userEntryResultingState);
                    machineModel.addStateModelToStateModelSet(resultingStateModel);
                    this.addStateToViewTransitionTableEventResponse(ThreadLocalRandom.current().nextInt(0, 1275 + 1), ThreadLocalRandom.current().nextInt(0, 450 + 1), userEntryResultingState);
                }

                //Create transition model placeholder
                TransitionModel newTransitionModel = new TransitionModel(stateModelSelected, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

                //Check to see if the transition already exists for the current state model
                for (TransitionModel transitionModel : machineModel.getExitingTranstionsFromStateModel(stateModelSelected)) {
                    if (transitionModel.equals(newTransitionModel)) {
                        // if transition exists alert the user and don't do anything further
                        Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                                "Transition '" + newTransitionModel + "' already exists.", ButtonType.OK);
                        invalidActionAlert.setHeaderText("Information");
                        invalidActionAlert.setTitle("Invalid Action");
                        invalidActionAlert.show();
                        return;
                    }
                }

                //Add transition model to machinemodel
                machineModel.addTransitionModelToTransitionModelSet(newTransitionModel);

                //Update table view
                transitionTableController.addTransitionModelEntryToTransitionTable(newTransitionModel);

                //Add transitionview onto diagram view
                if (userEntryCurrentState.equals(userEntryResultingState)) {
                    this.addReflexiveTransitionToView(stateModelSelected.getStateId(), resultingStateModel.getStateId(), newTransitionModel);
                } else {
                    this.addDirectionalTransitionToView(stateModelSelected.getStateId(), resultingStateModel.getStateId(), newTransitionModel);
                }
                stage.close();
            });


        });

        deleteStateItem.setOnAction(e -> {
            //Update machine model
            HashSet<TransitionModel> exitingTranstionsFromStateModel = machineModel.getExitingTranstionsFromStateModel(stateModelSelected);
            HashSet<TransitionModel> enteringTranstionsFromStateModel = machineModel.getEnteringTransitionsFromStateModel(stateModelSelected);
            machineModel.removeTransitionModelsFromTransitionModelSet(exitingTranstionsFromStateModel);
            machineModel.removeTransitionModelsFromTransitionModelSet(enteringTranstionsFromStateModel);
            machineModel.removeStateModelFromStateModelSet(stateModelSelected);
            //Notify transition table controller
            transitionTableController.deleteTransitionsLinkedToDeletedStateFromTransitionTable(exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
            //Update view
            diagramView.deleteStateView(stateModelSelected.getStateId(), exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
        });

        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);

    }

    private void setUpComboBoxesListeners(ComboBox comboBox) {
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            if ((newValue.matches("^\\w{1}$")) || newValue.equals("\u03B5")) {
                return;
            }
            Platform.runLater(() -> {
                comboBox.getEditor().clear();
            });
        });
    }


    public void highlightTransitionView(TransitionModel transitionModelToHightlight) {
        if (transitionModeIsHighlighted != null) {
            removeHighlightedTransitionView(transitionModeIsHighlighted);
        }
        transitionModeIsHighlighted = transitionModelToHightlight;

        if (transitionModelToHightlight.getCurrentStateModel().equals(transitionModelToHightlight.getResultingStateModel())) {
            Map<String, StateView> stateMap = diagramView.getStateMap();
            StateView currentStateView = stateMap.get(transitionModelToHightlight.getCurrentStateModel().getStateId());
            currentStateView.getReflexiveArrowShaftArc().setStroke(Color.LAWNGREEN);
            currentStateView.getReflexiveArrowTipPolygon().setFill(Color.LAWNGREEN);
            currentStateView.getReflexiveArrowTipPolygon().setStroke(Color.LAWNGREEN);
        } else {
            HashSet<Node> transitionViewHighlightedSet = retrieveDirectionalTransitionView(transitionModelToHightlight);

            for (Node node : transitionViewHighlightedSet) {
                if (node instanceof TransitionView) {
                    TransitionView transitionView = (TransitionView) node;
                    transitionView.setStroke(Color.LAWNGREEN);
                }
                if (node instanceof StackPane) {
                    StackPane arrowTipStackPane = (StackPane) node;
                    arrowTipStackPane.setStyle("-fx-background-color:lawngreen;-fx-border-width:2px;-fx-border-color:lawngreen;-fx-shape: \"M0,-4L4,0L0,4Z\"");
                }
            }
        }
    }


    public void removeHighlightedTransitionView(TransitionModel transitionModelToRemoveHightlight) {
        if (transitionModelToRemoveHightlight.getCurrentStateModel().equals(transitionModelToRemoveHightlight.getResultingStateModel())) {
            Map<String, StateView> stateMap = diagramView.getStateMap();
            StateView currentStateView = stateMap.get(transitionModelToRemoveHightlight.getCurrentStateModel().getStateId());
            currentStateView.getReflexiveArrowShaftArc().setStroke(Color.BLACK);
            currentStateView.getReflexiveArrowTipPolygon().setFill(Color.BLACK);
            currentStateView.getReflexiveArrowTipPolygon().setStroke(Color.BLACK);
        } else {
            HashSet<Node> transitionViewHighlightedSet = retrieveDirectionalTransitionView(transitionModelToRemoveHightlight);

            for (Node node : transitionViewHighlightedSet) {
                if (node instanceof TransitionView) {
                    TransitionView transitionView = (TransitionView) node;
                    transitionView.setStroke(Color.BLACK);
                }
                if (node instanceof StackPane) {
                    StackPane arrowTipStackPane = (StackPane) node;
                    arrowTipStackPane.setStyle("-fx-background-color:black;-fx-border-width:2px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\"");
                }
            }
        }
    }

    private HashSet<Node> retrieveDirectionalTransitionView(TransitionModel transitionModel) {

        Map<String, StateView> stateMap = diagramView.getStateMap();

        String currentStateModelID = transitionModel.getCurrentStateModel().getStateId();
        String resultingStateModelID = transitionModel.getResultingStateModel().getStateId();

        StateView currentStateView = stateMap.get(currentStateModelID);
        StateView resultingStateView = stateMap.get(resultingStateModelID);

        HashSet<HashSet<Node>> linkedTransitionViews = diagramView.getLinkedTransitionViewsMap().get(currentStateView);
        HashSet<Node> TransitionViewSet = new HashSet<>();

        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionView) {
                    TransitionView transitionViewToUpdate = (TransitionView) node;
                    if (transitionViewToUpdate.getTarget().getStateID().equals(resultingStateView.getStateID())) {
                        TransitionViewSet.add(transitionViewToUpdate);
                    }
                } else if (node instanceof StackPane) {
                    StackPane arrowTipStackPaneToUpdate = (StackPane) node;
                    if (arrowTipStackPaneToUpdate.getId() == null) {
                        continue;
                    }
                    if (arrowTipStackPaneToUpdate.getId().equals(resultingStateView.getStateID())) {
                        TransitionViewSet.add(arrowTipStackPaneToUpdate);
                    }
                }
            }
        }
        return TransitionViewSet;
    }

}