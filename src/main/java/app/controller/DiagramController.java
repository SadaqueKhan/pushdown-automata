package app.controller;

import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.DiagramView;
import app.view.MainStageView;
import app.view.StateView;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
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


    public DiagramController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {
        this.mainStageController = mainStageController;
        this.mainStageView = mainStageView;
        this.machineModel = machineModel;
        this.transitionTableController = mainStageController.getTransitionTableController();
        this.diagramView = new DiagramView(this, mainStageView);
    }


    public void loadDiagramView(TransitionTableController transitionTableController) {
        this.transitionTableController = transitionTableController;
        this.mainStageView.getContainerForCenterNodes().getChildren().add(diagramView);
    }


    public void loadStatesOntoDiagram() {
        for (StateModel stateModelToLoad : machineModel.getStateModelSet()) {
            diagramView.addStateView(ThreadLocalRandom.current().nextInt(0, 1275 + 1), ThreadLocalRandom.current().nextInt(0, 450 + 1), this, stateModelToLoad.getStateId());
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


    public void addStateToView(double x, double y, String userEntryCurrentStateId) {
        diagramView.addStateView(x, y, this, userEntryCurrentStateId);
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

        StateModel stateModel = machineModel.getStateModelFromStateModelSet(stateView.getStateID());

        //TODO need to MVC this
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");

        toggleStandardStateItem.setOnAction(e -> {
            if (stateModel.isStandardState()) {
                stateModel.setStandardState(false);
                stateView.toggleStandardStateUIComponent(stateModel.isStandardState());
            } else {
                stateModel.setStandardState(true);
                stateView.toggleStandardStateUIComponent(stateModel.isStandardState());

            }
        });

        toggleStartStateItem.setOnAction(e -> {
            if (stateModel.isStartState()) {
                stateModel.setStartState(false);
                stateView.toggleStartStateUIComponent(stateModel.isStartState());
            } else {
                if (machineModel.findStartStateModel() != null) { // Check to see if start state exists in machine
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "Only one initial state allowed per machine. " + "ControlState " + machineModel.findStartStateModel() + " is currently defined as the initial state for this machine.", ButtonType.OK);
                    invalidActionAlert.setHeaderText("Information");
                    invalidActionAlert.setTitle("Invalid Action");
                    invalidActionAlert.show();
                } else {
                    stateModel.setStartState(true);
                    stateView.toggleStartStateUIComponent(stateModel.isStartState());
                }
            }
        });

        //TODO need to remove this listeners logic
        toggleFinalStateItem.setOnAction(e -> {
            if (stateModel.isFinalState()) {
                stateModel.setFinalState(false);
                stateView.toggleFinalStateUIComponent(stateModel.isFinalState());
            } else {
                stateModel.setFinalState(true);
                stateView.toggleFinalStateUIComponent(stateModel.isFinalState());
            }
        });


        //TODO need to remove this listeners logic
        createTransitionItem.setOnAction(e -> {
//Create input widgets for the user to enter a configuration
            TextField currentStateTextField = new TextField();
            currentStateTextField.setPrefWidth(50);

            TextField inputSymbolTextField = new TextField();
            inputSymbolTextField.setPrefWidth(50);

            TextField stackSymbolToPopTextField = new TextField();
            stackSymbolToPopTextField.setPrefWidth(50);

// Create a arrow label to connect the configuration input widgets to action input widgets
            final Label arrowLabel = new Label("->");

//Create input widgets for the user to enter a configuration
            TextField resultingStateTextField = new TextField();
            resultingStateTextField.setPrefWidth(50);

            TextField stackSymbolToPushTextField = new TextField();
            stackSymbolToPushTextField.setPrefWidth(50);

//Create submit button for the user to submit a transition
            Button submitTransitionButton = new Button("Submit");


            final HBox hBox = new HBox();
            hBox.setPadding(new Insets(10, 10, 10, 10));
            hBox.setSpacing(10);
            hBox.getChildren().addAll(currentStateTextField, inputSymbolTextField, stackSymbolToPopTextField, arrowLabel, resultingStateTextField, stackSymbolToPushTextField);

            final VBox vBox = new VBox(hBox, submitTransitionButton);
            vBox.setAlignment(Pos.CENTER);

            Scene scene = new Scene(vBox, 350, 150);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Create Transition");
            stage.setScene(scene);
            stage.show();
        });

        deleteStateItem.setOnAction(e -> {
            //Update machine model
            HashSet<TransitionModel> exitingTranstionsFromStateModel = machineModel.getExitingTranstionsFromStateModel(stateModel);
            HashSet<TransitionModel> enteringTranstionsFromStateModel = machineModel.getEnteringTranstionsFromStateModel(stateModel);
            machineModel.removeTransitionModelsFromTransitionModelSet(exitingTranstionsFromStateModel);
            machineModel.removeTransitionModelsFromTransitionModelSet(enteringTranstionsFromStateModel);
            machineModel.removeStateModelFromStateModelSet(stateModel);
            //Notify transition table controller
            transitionTableController.deleteTransitionsLinkedToDeletedState(exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
            //Update view
            diagramView.deleteStateView(stateModel.getStateId(), exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
        });

        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);

    }


}