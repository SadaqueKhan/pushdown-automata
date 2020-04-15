package app.presenter;
import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.DiagramScene;
import app.view.MainScene;
import app.view.StateNode;
import app.view.TransitionNode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Diagram scene presenter retrieves data from repositories (the model), and formats it for display in the diagram
 * scene.
 * </p>
 */
public class DiagramScenePresenter {
    private final MainScene mainScene;
    private final MainStagePresenter mainStagePresenter;
    private final MachineModel machineModel;
    private final DiagramScene diagramScene;
    private TransitionTableScenePresenter transitionTableScenePresenter;
    private double layoutX;
    private double layoutY;
    private double sceneX;
    private double sceneY;
    private TransitionModel transitionModelHighlighted;
    private StateNode startStateNode;
    private final Map<StateModel, StateNode> stateMap;
    private final Map<StateNode, HashSet<HashSet<Node>>> linkedTransitionViewsMap;
    /**
     * Constructor of the diagram scene presenter, used to instantiate an instance of the presenter.
     * @param mainScene for which the diagram scene is rendered on.
     * @param mainStagePresenter the presenter which needs to be notified about events on the diagram scene.
     * @param machineModel the model containing the data about the pushdown automaton machine.
     */
    public DiagramScenePresenter(MainScene mainScene, MainStagePresenter mainStagePresenter, MachineModel machineModel) {
        this.mainStagePresenter = mainStagePresenter;
        this.mainScene = mainScene;
        this.machineModel = machineModel;
        this.diagramScene = new DiagramScene(this);
        this.stateMap = new HashMap<>();
        this.linkedTransitionViewsMap = new HashMap<>();
    }
    /**
     * Loads the diagram scene back onto the main stage when selected via the tab found on in the main stage.
     * @param transitionTableScenePresenter used to notify of any updates that may affect its respective view
     * it controls.
     */
    public void loadDiagramViewOntoStage(TransitionTableScenePresenter transitionTableScenePresenter) {
        this.transitionTableScenePresenter = transitionTableScenePresenter;
        this.mainScene.getContainerForCenterNodes().getChildren().add(diagramScene);
    }
    /**
     * Loads state data back the diagram scene.
     */
    public void loadStatesOntoDiagram() {
        StateNode stateNodeToLoad;
        for (StateModel stateModelToLoad : machineModel.getStateModelSet()) {
            addStateViewOntoDiagramView(stateModelToLoad);
            stateNodeToLoad = stateMap.get(stateModelToLoad);
            if (stateModelToLoad.isStartState()) {
                stateNodeToLoad.getStartStatePointLine1().setVisible(stateModelToLoad.isStartState());
                stateNodeToLoad.getStartStatePointLine2().setVisible(stateModelToLoad.isStartState());
            }
            if (stateModelToLoad.isFinalState()) {
                stateNodeToLoad.getFinalStateArc().setVisible(stateModelToLoad.isFinalState());
            }
        }
    }
    /**
     * Handles dynamic adding of a state node onto the diagram scene given x and y coordinates.
     * @param xCoordinateOnDiagram used to position the x coordinate of state node to be rendered onto the diagram
     * scene.
     * @param yCoordinateOnDiagram used to position the y coordinate of state node to be rendered onto the diagram
     * scene.
     */
    public void addStateViewOntoDiagramViewDynamicRender(double xCoordinateOnDiagram, double yCoordinateOnDiagram) {
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
        newStateModel.setXCoordinateOnDiagram(xCoordinateOnDiagram);
        newStateModel.setYCoordinateOnDiagram(yCoordinateOnDiagram);
        if (yCoordinateOnDiagram >= 475) {
            System.out.println("Ypost: " + yCoordinateOnDiagram);
            newStateModel.setYCoordinateOnDiagram(yCoordinateOnDiagram - 100);
            System.out.println("Yafterr: " + newStateModel.getYCoordinateOnDiagram());
        }
        System.out.println("XOutpost: " + xCoordinateOnDiagram);
        if (xCoordinateOnDiagram >= 1115) {
            System.out.println("Xpost: " + xCoordinateOnDiagram);
            newStateModel.setXCoordinateOnDiagram(xCoordinateOnDiagram - 100);
            System.out.println("Xafter: " + newStateModel.getXCoordinateOnDiagram());
        }


        machineModel.addStateModelToStateModelSet(newStateModel);
        addStateViewOntoDiagramView(newStateModel);
        transitionTableScenePresenter.updateAvailableStateListForCombobox();
    }
    /**
     * Loads transition data back the diagram scene.
     */
    public void loadTransitionsOntoDiagram() {
        for (TransitionModel transitionModelToLoad : machineModel.getTransitionModelSet()) {
            String currentStateModelToLoadID = transitionModelToLoad.getCurrentStateModel().getStateId();
            String resultingStateModelToLoadID = transitionModelToLoad.getResultingStateModel().getStateId();
            //Add transitionview onto diagram view
            if (currentStateModelToLoadID.equals(resultingStateModelToLoadID)) {
                addReflexiveTransitionToDiagramView(transitionModelToLoad);
            } else {
                addDirectionalTransitionToView(transitionModelToLoad);
            }
        }
    }
    /**
     * Handles the rendering of context menu on a given state node, and the respective actions when an action menu
     * item is selected from the context menu popup.
     * @param stateNode used to determine which state node to render a context menu pop on.
     */
    public void stateViewContextMenuPopUp(StateNode stateNode) {
        StateModel stateModelSelected = machineModel.getStateModelFromStateModelSet(stateNode.getStateId());
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setId("contextMenu");
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        toggleStandardStateItem.setId("toggleStandardStateItem");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        toggleStartStateItem.setId("toggleStartStateItem");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        toggleFinalStateItem.setId("toggleFinalStateItem");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        createTransitionItem.setId("createTransitionItem");
        MenuItem deleteStateItem = new MenuItem("Delete state");
        deleteStateItem.setId("deleteStateItem");
        toggleStandardStateItem.setOnAction(e -> {
            stateModelSelected.setStartState(false);
            stateModelSelected.setFinalState(false);
            stateNode.getStartStatePointLine1().setVisible(false);
            stateNode.getStartStatePointLine2().setVisible(false);
            stateNode.getFinalStateArc().setVisible(false);
        });
        toggleStartStateItem.setOnAction(e -> {
            if (stateModelSelected.isStartState()) {
                stateModelSelected.setStartState(false);
                stateNode.getStartStatePointLine1().setVisible(stateModelSelected.isStartState());
                stateNode.getStartStatePointLine2().setVisible(stateModelSelected.isStartState());
            } else {
                if (machineModel.getStartStateModel() != null) { // Check to see if start state exists in machine
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "Only one initial state allowed per machine. " + "State " + machineModel.getStartStateModel() + " is currently defined as the initial state for this machine.", ButtonType.OK);
                    invalidActionAlert.setHeaderText("Information");
                    invalidActionAlert.setTitle("Invalid Action");
                    invalidActionAlert.show();
                } else {
                    stateModelSelected.setStartState(true);
                    stateNode.getStartStatePointLine1().setVisible(stateModelSelected.isStartState());
                    stateNode.getStartStatePointLine2().setVisible(stateModelSelected.isStartState());
                }
            }
        });
        toggleFinalStateItem.setOnAction(e -> {
            if (stateModelSelected.isFinalState()) {
                stateModelSelected.setFinalState(false);
                stateNode.getFinalStateArc().setVisible(stateModelSelected.isFinalState());
            } else {
                stateModelSelected.setFinalState(true);
                stateNode.getFinalStateArc().setVisible(stateModelSelected.isFinalState());
            }
        });
        createTransitionItem.setOnAction(e -> {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setPadding(new Insets(10, 10, 0, 10));
            gridPane.setAlignment(Pos.CENTER);
            //Create input widgets for the user to enter a configuration
            TextField currentStateTextField = new TextField();
            currentStateTextField.setText(stateModelSelected.getStateId());
            currentStateTextField.setPrefWidth(65);
            // currentStateTextField.setEditable(false);
            currentStateTextField.setDisable(true);
            gridPane.add(new Label("Current State"), 1, 1);
            gridPane.add(currentStateTextField, 1, 2);
            ComboBox<String> inputSymbolComboBox = new ComboBox<>();
            inputSymbolComboBox.setId("inputSymbolComboBox");
            inputSymbolComboBox.setPrefWidth(110);
            inputSymbolComboBox.setEditable(true);
            inputSymbolComboBox.getItems().addAll(machineModel.getInputAlphabetSet());
            setUpComboBoxesListeners(inputSymbolComboBox);
            gridPane.add(new Label("Input Symbol"), 2, 1);
            gridPane.add(inputSymbolComboBox, 2, 2);
            ComboBox<String> stackSymbolToPopComboBox = new ComboBox<>();
            stackSymbolToPopComboBox.setId("stackSymbolToPopComboBox");
            stackSymbolToPopComboBox.setPrefWidth(110);
            stackSymbolToPopComboBox.setEditable(true);
            stackSymbolToPopComboBox.getItems().addAll(machineModel.getStackAlphabetSet());
            setUpComboBoxesListeners(stackSymbolToPopComboBox);
            gridPane.add(new Label("Stack Symbol to Pop"), 3, 1);
            gridPane.add(stackSymbolToPopComboBox, 3, 2);
            // Create a arrow label to connect the configuration input widgets to action input widgets
            gridPane.add(new Label("->"), 4, 2);
            //Create input widgets for the user to enter a configuration
            ComboBox<String> resultingStateComboBox = new ComboBox<>();
            resultingStateComboBox.setId("resultingStateComboBox");
            resultingStateComboBox.setPrefWidth(110);
            resultingStateComboBox.setEditable(true);
            ArrayList<String> availableStateList = new ArrayList<>();
            for (StateModel availableStateModel : machineModel.getStateModelSet()) {
                availableStateList.add(availableStateModel.getStateId());
            }
            resultingStateComboBox.getItems().addAll(availableStateList);
            gridPane.add(new Label("Resulting State"), 5, 1);
            gridPane.add(resultingStateComboBox, 5, 2);
            ComboBox<String> stackSymbolToPushComboBox = new ComboBox<>();
            stackSymbolToPushComboBox.setId("stackSymbolToPushComboBox");
            stackSymbolToPushComboBox.setPrefWidth(110);
            stackSymbolToPushComboBox.setEditable(true);
            stackSymbolToPushComboBox.getItems().addAll(machineModel.getStackAlphabetSet());
            setUpComboBoxesListeners(stackSymbolToPushComboBox);
            gridPane.add(new Label("Stack Symbol to Push"), 6, 1);
            gridPane.add(stackSymbolToPushComboBox, 6, 2);
            //Create submit button for the user to submit a transition
            Button submitTransitionButton = new Button("Submit");
            submitTransitionButton.setId("submitTransitionButton");
            HBox hBoxButtons = new HBox();
            hBoxButtons.setPadding(new Insets(10, 10, 10, 10));
            hBoxButtons.setSpacing(10);
            hBoxButtons.getChildren().add(submitTransitionButton);
            gridPane.add(hBoxButtons, 7, 2);
            Scene scene = new Scene(gridPane, 800, 150);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainStagePresenter.getPrimaryWindow());
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
                transitionTableScenePresenter.updateInputAlphabetForComboBox();
                transitionTableScenePresenter.updateStackAlphabetForComboBox();
                StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingState);
                if (resultingStateModel == null) {
                    resultingStateModel = new StateModel(userEntryResultingState);
                    machineModel.addStateModelToStateModelSet(resultingStateModel);
                    this.addStateViewOntoDiagramView(resultingStateModel);
                    transitionTableScenePresenter.updateAvailableStateListForCombobox();
                }
                //Create transition model placeholder
                TransitionModel newTransitionModel = new TransitionModel(stateModelSelected, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);
                //Check to see if the transition already exists for the current state model
                for (TransitionModel transitionModel : getExitingTransitionsFromStateModel(stateModelSelected)) {
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
                //Update transition table scene
                transitionTableScenePresenter.addTransitionModelEntryToTransitionTable(newTransitionModel);
                transitionTableScenePresenter.updateAvailableStateListForCombobox();
                //Update diagram scene
                if (userEntryCurrentState.equals(userEntryResultingState)) {
                    addReflexiveTransitionToDiagramView(newTransitionModel);
                } else {
                    addDirectionalTransitionToView(newTransitionModel);
                }
                stage.close();
            });
        });
        deleteStateItem.setOnAction(e -> {
            //Update view
            deleteStateViewOnDiagramView(stateModelSelected);
            //Notify transition table controller
            transitionTableScenePresenter.deleteTransitionsLinkedToDeletedStateFromTransitionTable(stateModelSelected);
        });
        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);
        contextMenu.show(stateNode, Side.RIGHT, 5, 5);
    }
    /**
     * Handles creating a reflexive transition given a transition model.
     * @param newTransitionModel used to structure the formulation of a transition node.
     */
    public void addReflexiveTransitionToDiagramView(TransitionModel newTransitionModel) {
        StateNode sourceCell = stateMap.get(newTransitionModel.getCurrentStateModel());
        if (sourceCell.getTransitionsListVBox() == null) {
            VBox newTransitionsListVBox = new VBox();
            newTransitionsListVBox.setStyle("-fx-background-color:#ffffff;-fx-border-width:2px;-fx-border-color:black;");
            newTransitionsListVBox.setOnMousePressed(mouseEvent -> {
                sceneX = mouseEvent.getScreenX();
                sceneY = mouseEvent.getScreenY();
                layoutX = newTransitionsListVBox.getLayoutX();
                layoutY = newTransitionsListVBox.getLayoutY();
            });
            newTransitionsListVBox.setOnMouseDragged(mouseEvent -> {
                // Offset of drag
                double offsetX = mouseEvent.getScreenX() - sceneX;
                double offsetY = mouseEvent.getScreenY() - sceneY;
                // Taking parent bounds
                Bounds parentBounds = newTransitionsListVBox.getParent().getLayoutBounds();
                // Drag node bounds
                double currPaneLayoutX = newTransitionsListVBox.getLayoutX();
                double currPaneWidth = newTransitionsListVBox.getWidth();
                double currPaneLayoutY = newTransitionsListVBox.getLayoutY();
                double currPaneHeight = newTransitionsListVBox.getHeight();
                if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                    // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                    newTransitionsListVBox.setTranslateX(offsetX);
                } else if (currPaneLayoutX + offsetX < 0) {
                    // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                    // which makes the final layout position to 0 in mouse released event.
                    newTransitionsListVBox.setTranslateX(-currPaneLayoutX);
                } else {
                    // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                    newTransitionsListVBox.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
                }
                if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                    newTransitionsListVBox.setTranslateY(offsetY);
                } else if (currPaneLayoutY + offsetY < 0) {
                    newTransitionsListVBox.setTranslateY(-currPaneLayoutY);
                } else {
                    newTransitionsListVBox.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
                }
            });
            newTransitionsListVBox.setOnMouseReleased(event -> {
                // Updating the new layout positions
                newTransitionsListVBox.setLayoutX(layoutX + newTransitionsListVBox.getTranslateX());
                newTransitionsListVBox.setLayoutY(layoutY + newTransitionsListVBox.getTranslateY());
                //Update coordinate stored in transition tabel model
                double newXCoordinateOfTransitionOnDiagram = layoutX + newTransitionsListVBox.getTranslateX();
                double newYCoordinateOfTransitionOnDiagram = layoutY + newTransitionsListVBox.getTranslateY();
                newTransitionModel.setXCoordinateOnDiagram(newXCoordinateOfTransitionOnDiagram);
                newTransitionModel.setYCoordinateOnDiagram(newYCoordinateOfTransitionOnDiagram);
                for (TransitionModel transitionModel : getRelatedTransitions(newTransitionModel)) {
                    transitionModel.setXCoordinateOnDiagram(newXCoordinateOfTransitionOnDiagram);
                    transitionModel.setYCoordinateOnDiagram(newYCoordinateOfTransitionOnDiagram);
                }
                // Resetting the translate positions
                newTransitionsListVBox.setTranslateX(0);
                newTransitionsListVBox.setTranslateY(0);
            });
            sourceCell.setTransitionsListVBox(newTransitionsListVBox);
            diagramScene.getChildren().add(sourceCell.getTransitionsListVBox());
            sourceCell.getTransitionsListVBox().relocate(newTransitionModel.getXCoordinateOnDiagram(),
                    newTransitionModel.getYCoordinateOnDiagram());
        }
        sourceCell.getTransitionsListVBox().getChildren().add(new Label(newTransitionModel.toString()));
        sourceCell.getReflexiveArrowShaftArc().setVisible(true);
        sourceCell.getReflexiveArrowTipPolygon().setVisible(true);
    }
    /**
     * Handles deletion of a state node on the diagram scene.
     * @param stateModelToDelete requested to be deleted from the diagram scene.
     */
    public void deleteStateViewOnDiagramView(StateModel stateModelToDelete) {
        // Retrieve exiting/entering transition model linked to state model to be deleted.
        HashSet<TransitionModel> exitingTransitionsFromStateModel = getExitingTransitionsFromStateModel
                (stateModelToDelete);
        HashSet<TransitionModel> enteringTransitionsFromStateModel = getEnteringTransitionsFromStateModel
                (stateModelToDelete);
        //Retrieve state view to be deleted.
        StateNode stateNodeToRemove = stateMap.get(stateModelToDelete);
        //Retrieve and remove linked transition views to state view.
        deleteTransitionView(exitingTransitionsFromStateModel);
        deleteTransitionView(enteringTransitionsFromStateModel);
        // Remove mapping of state view in data structures.
        stateMap.remove(stateModelToDelete);
        linkedTransitionViewsMap.remove(stateNodeToRemove);
        //Remove the state view from the diagram view.
        diagramScene.getChildren().remove(stateNodeToRemove);
        //Update machine model.
        machineModel.removeTransitionModelsFromTransitionModelSet(exitingTransitionsFromStateModel);
        machineModel.removeTransitionModelsFromTransitionModelSet(enteringTransitionsFromStateModel);
        machineModel.removeStateModelFromStateModelSet(stateModelToDelete);
    }
    /**
     * Handles creation of an arrow tip and linking to a transition node.
     * @param toLineEnd used for computing the binding of the arrow tip.
     * @param line used for computing the binding of the arrow tip.
     * @param currentStateNode used for computing the binding of the arrow tip.
     * @param resultingStateNode used for computing the binding of the arrow tip.
     * @return {@code StackPane} retaining the UI components that make up the arrow tip linked to a transition node
     */
    private StackPane createArrowTip(boolean toLineEnd, Line line, StackPane currentStateNode, StackPane resultingStateNode) {
        double size = 12; // Arrow size
        StackPane arrowTipStackPane = new StackPane();
        arrowTipStackPane.setStyle("-fx-background-color:black;-fx-border-width:2px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\";");
        arrowTipStackPane.setPrefSize(size, size);
        arrowTipStackPane.setMaxSize(size, size);
        arrowTipStackPane.setMinSize(size, size);
        // Determining the arrow visibility unless there is enough space between dots.
        DoubleBinding xDiff = line.endXProperty().subtract(line.startXProperty());
        DoubleBinding yDiff = line.endYProperty().subtract(line.startYProperty());
        BooleanBinding visible = (xDiff.lessThanOrEqualTo(size).and(xDiff.greaterThanOrEqualTo(-size)).and(yDiff.greaterThanOrEqualTo(-size)).and(yDiff.lessThanOrEqualTo(size))).not();
        arrowTipStackPane.visibleProperty().bind(visible);
        // Determining the x point on the line which is at a certain distance.
        DoubleBinding tX = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                // When determining the point towards end, the required distance is total length minus (radius + arrow half width)
                dt = lineLength - (resultingStateNode.getWidth() / 2) - (arrowTipStackPane.getWidth() / 2);
            } else {
                // When determining the point towards start, the required distance is just (radius + arrow half width)
                dt = (currentStateNode.getWidth() / 2) + (arrowTipStackPane.getWidth() / 2);
            }
            double t = dt / lineLength;
            return ((1 - t) * line.getStartX()) + (t * line.getEndX());
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        // Determining the y point on the line which is at a certain distance.
        DoubleBinding tY = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                dt = lineLength - (resultingStateNode.getHeight() / 2) - (arrowTipStackPane.getHeight() / 2);
            } else {
                dt = (currentStateNode.getHeight() / 2) + (arrowTipStackPane.getHeight() / 2);
            }
            double t = dt / lineLength;
            return ((1 - t) * line.getStartY()) + (t * line.getEndY());
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        arrowTipStackPane.layoutXProperty().bind(tX.subtract(arrowTipStackPane.widthProperty().divide(2)));
        arrowTipStackPane.layoutYProperty().bind(tY.subtract(arrowTipStackPane.heightProperty().divide(2)));
        DoubleBinding endArrowAngle = Bindings.createDoubleBinding(() -> {
            double stX = toLineEnd ? line.getStartX() : line.getEndX();
            double stY = toLineEnd ? line.getStartY() : line.getEndY();
            double enX = toLineEnd ? line.getEndX() : line.getStartX();
            double enY = toLineEnd ? line.getEndY() : line.getStartY();
            double angle = Math.toDegrees(Math.atan2(enY - stY, enX - stX));
            if (angle < 0) {
                angle += 360;
            }
            return angle;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        arrowTipStackPane.rotateProperty().bind(endArrowAngle);
        return arrowTipStackPane;
    }
    /**
     * Handles adding a state node onto the diagram scene given a state model.
     * @param newStateModel used to add a state node onto the diagram scene.
     */
    void addStateViewOntoDiagramView(StateModel newStateModel) {
        StateNode stateNode = new StateNode(newStateModel.getStateId(), this);
        stateNode.relocate(newStateModel.getXCoordinateOnDiagram(), newStateModel.getYCoordinateOnDiagram());
        diagramScene.getChildren().add(stateNode);
        stateMap.put(newStateModel, stateNode);
        linkedTransitionViewsMap.put(stateNode, new HashSet<>());
    }
    /**
     * Sets up dynamic listener for checkboxes for auto-updating, as functionality is limited to just checking if the
     * size of the input does not exceed 1 and clearing the input if it does back to 0.
     * @param comboBox to which a dynamic listener is to be attached.
     */
    private void setUpComboBoxesListeners(ComboBox comboBox) {
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if ((newValue.length() == 1 || newValue.equals("\u03B5"))) {
                return;
            }
            Platform.runLater(() -> {
                comboBox.getEditor().clear();
            });
        });
    }
    //StateGUIEventResponses
    /**
     * Handles creating a transition node given a transition model.
     * @param newTransitionModel used to form the transition node.
     */
    public void addDirectionalTransitionToView(TransitionModel newTransitionModel) {
        //Get state from map using state ID
        StateNode currentStateNode = stateMap.get(newTransitionModel.getCurrentStateModel());
        StateNode resultingStateNode = stateMap.get(newTransitionModel.getResultingStateModel());
        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateNode);
        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionNode) {
                    TransitionNode transitionNodeToCheck = (TransitionNode) node;
                    if (transitionNodeToCheck.getCurrentStateNode() == currentStateNode && transitionNodeToCheck.getResultingStateNode() == resultingStateNode) {
                        VBox newTransitionListVBox = transitionNodeToCheck.getTransitionsListVBox();
                        newTransitionListVBox.getChildren().add(new Label(newTransitionModel.toString()));
                        return;
                    }
                }
            }
        }
        //Transition does not exist create fresh transition
        diagramScene.getChildren().remove(currentStateNode);
        diagramScene.getChildren().remove(resultingStateNode);
        Line virtualCenterLine = new Line();
        virtualCenterLine.startXProperty().bind(currentStateNode.layoutXProperty().add(currentStateNode.translateXProperty()).add(currentStateNode.widthProperty().divide(2)));
        virtualCenterLine.startYProperty().bind(currentStateNode.layoutYProperty().add(currentStateNode.translateYProperty()).add(currentStateNode.heightProperty().divide(2)));
        virtualCenterLine.endXProperty().bind(resultingStateNode.layoutXProperty().add(resultingStateNode.translateXProperty()).add(resultingStateNode.widthProperty().divide(2)));
        virtualCenterLine.endYProperty().bind(resultingStateNode.layoutYProperty().add(resultingStateNode.translateYProperty()).add(resultingStateNode.heightProperty().divide(2)));
        virtualCenterLine.setOpacity(0);
        StackPane centerLineArrowAB = createArrowTip(true, virtualCenterLine, currentStateNode, resultingStateNode);
        centerLineArrowAB.setOpacity(0);
        StackPane centerLineArrowBA = createArrowTip(false, virtualCenterLine, currentStateNode, resultingStateNode);
        centerLineArrowBA.setOpacity(0);
        TransitionNode transitionNode = new TransitionNode(currentStateNode, resultingStateNode);
        transitionNode.setStroke(Color.BLACK);
        transitionNode.setStrokeWidth(2);
        VBox newTransitionsListVBox = transitionNode.getTransitionsListVBox();
        newTransitionsListVBox.setOnMousePressed(mouseEvent -> {
            sceneX = mouseEvent.getScreenX();
            sceneY = mouseEvent.getScreenY();
            layoutX = newTransitionsListVBox.getLayoutX();
            layoutY = newTransitionsListVBox.getLayoutY();
        });
        newTransitionsListVBox.setOnMouseDragged(mouseEvent -> {
            // Offset of drag
            double offsetX = mouseEvent.getScreenX() - sceneX;
            double offsetY = mouseEvent.getScreenY() - sceneY;
            // Taking parent bounds
            Bounds parentBounds = newTransitionsListVBox.getParent().getLayoutBounds();
            // Drag node bounds
            double currPaneLayoutX = newTransitionsListVBox.getLayoutX();
            double currPaneWidth = newTransitionsListVBox.getWidth();
            double currPaneLayoutY = newTransitionsListVBox.getLayoutY();
            double currPaneHeight = newTransitionsListVBox.getHeight();
            if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                newTransitionsListVBox.setTranslateX(offsetX);
            } else if (currPaneLayoutX + offsetX < 0) {
                // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                // which makes the final layout position to 0 in mouse released event.
                newTransitionsListVBox.setTranslateX(-currPaneLayoutX);
            } else {
                // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                newTransitionsListVBox.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
            }
            if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                newTransitionsListVBox.setTranslateY(offsetY);
            } else if (currPaneLayoutY + offsetY < 0) {
                newTransitionsListVBox.setTranslateY(-currPaneLayoutY);
            } else {
                newTransitionsListVBox.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
            }
        });
        newTransitionsListVBox.setOnMouseReleased(event -> {
            // Updating the new layout positions
            newTransitionsListVBox.setLayoutX(layoutX + newTransitionsListVBox.getTranslateX());
            newTransitionsListVBox.setLayoutY(layoutY + newTransitionsListVBox.getTranslateY());
            //Set transitionlist model coordinates
            double newXCoordinateOfTransitionOnDiagram = layoutX + newTransitionsListVBox.getTranslateX();
            double newYCoordinateOfTransitionOnDiagram = layoutY + newTransitionsListVBox.getTranslateY();
            newTransitionModel.setXCoordinateOnDiagram(newXCoordinateOfTransitionOnDiagram);
            newTransitionModel.setYCoordinateOnDiagram(newYCoordinateOfTransitionOnDiagram);
            for (TransitionModel transitionModel : getRelatedTransitions(newTransitionModel)) {
                transitionModel.setXCoordinateOnDiagram(newXCoordinateOfTransitionOnDiagram);
                transitionModel.setYCoordinateOnDiagram(newYCoordinateOfTransitionOnDiagram);
            }
            // Resetting the translate positions
            newTransitionsListVBox.setTranslateX(0);
            newTransitionsListVBox.setTranslateY(0);
        });
        newTransitionsListVBox.getChildren().add(new Label(newTransitionModel.toString()));
        newTransitionsListVBox.relocate(newTransitionModel.getXCoordinateOnDiagram(), newTransitionModel.getYCoordinateOnDiagram());
        double diff = true ? -centerLineArrowAB.getPrefWidth() / 2 : centerLineArrowAB.getPrefWidth() / 2;
        final ChangeListener<Number> listener = (obs, old, newVal) -> {
            Rotate r = new Rotate();
            r.setPivotX(virtualCenterLine.getStartX());
            r.setPivotY(virtualCenterLine.getStartY());
            r.setAngle(centerLineArrowAB.getRotate());
            Point2D point = r.transform(new Point2D(virtualCenterLine.getStartX(), virtualCenterLine.getStartY() + diff));
            transitionNode.setStartX(point.getX());
            transitionNode.setStartY(point.getY());
            Rotate r2 = new Rotate();
            r2.setPivotX(virtualCenterLine.getEndX());
            r2.setPivotY(virtualCenterLine.getEndY());
            r2.setAngle(centerLineArrowBA.getRotate());
            Point2D point2 = r2.transform(new Point2D(virtualCenterLine.getEndX(), virtualCenterLine.getEndY() - diff));
            transitionNode.setEndX(point2.getX());
            transitionNode.setEndY(point2.getY());
        };
        centerLineArrowAB.rotateProperty().addListener(listener);
        centerLineArrowBA.rotateProperty().addListener(listener);
        virtualCenterLine.startXProperty().addListener(listener);
        virtualCenterLine.startYProperty().addListener(listener);
        virtualCenterLine.endXProperty().addListener(listener);
        virtualCenterLine.endYProperty().addListener(listener);
        StackPane arrowTip = createArrowTip(true, transitionNode, currentStateNode, resultingStateNode);
        arrowTip.setId(newTransitionModel.getResultingStateModel().getStateId());
        HashSet<Node> setOfNode = new HashSet<>();
        setOfNode.add(virtualCenterLine);
        setOfNode.add(centerLineArrowAB);
        setOfNode.add(centerLineArrowBA);
        setOfNode.add(transitionNode);
        setOfNode.add(arrowTip);
        linkedTransitionViewsMap.get(currentStateNode).add(setOfNode);
        diagramScene.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, transitionNode, arrowTip, transitionNode.getTransitionsListVBox());
        diagramScene.getChildren().addAll(currentStateNode, resultingStateNode);
    }
    /**
     * @param stateModel used to determine all exiting transition model from a given state model.
     * @return {@code HashSet<TransitionModel>} of exiting transition models from a given state model.
     */
    public HashSet<TransitionModel> getExitingTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> exitingTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : machineModel.getTransitionModelSet()) {
            if (isExitingTransitionModel.getCurrentStateModel().equals(stateModel)) {
                exitingTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return exitingTransitionFromStateModelToReturn;
    }
    /**
     * @param stateModel used to determine all entering transition model from a given state model.
     * @return {@code HashSet<TransitionModel>} of entering transition models from a given state model.
     */
    public HashSet<TransitionModel> getEnteringTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> enteringTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isEnteringTransitionModel : machineModel.getTransitionModelSet()) {
            if (isEnteringTransitionModel.getResultingStateModel().equals(stateModel)) {
                enteringTransitionFromStateModelToReturn.add(isEnteringTransitionModel);
            }
        }
        return enteringTransitionFromStateModelToReturn;
    }
    /**
     * Handles deleting of a transition nodes.
     * @param deletedTransitionModelsSet used to determines which transition nodes to delete.
     */
    public void deleteTransitionView(HashSet<TransitionModel> deletedTransitionModelsSet) {
        HashSet<HashSet<Node>> transitionViewNodesToRemoveSet = new HashSet<>();
        HashSet<StateNode> stateViewsWithTransitionToRemoveSet = new HashSet<>();
        for (TransitionModel deletedTransition : deletedTransitionModelsSet) {
            String currentStateModelID = deletedTransition.getCurrentStateModel().getStateId();
            String resultingStateModelID = deletedTransition.getResultingStateModel().getStateId();
            StateNode currentStateNode = stateMap.get(deletedTransition.getCurrentStateModel());
            //Check type of transition
            if (currentStateModelID.equals(resultingStateModelID)) {
                VBox listOfTransitionsVBox = currentStateNode.getTransitionsListVBox();
                Iterator<Node> iter = listOfTransitionsVBox.getChildren().iterator();
                while (iter.hasNext()) {
                    Label labelToRemove = (Label) iter.next();
                    if (labelToRemove.getText().equals(deletedTransition.toString())) {
                        iter.remove();
                    }
                }
                if (listOfTransitionsVBox.getChildren().isEmpty()) {
                    currentStateNode.getReflexiveArrowShaftArc().setVisible(false);
                    currentStateNode.getReflexiveArrowTipPolygon().setVisible(false);
                    diagramScene.getChildren().remove(listOfTransitionsVBox);
                }
            } else {
                //Find bi-directional transition view
                HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateNode);
                for (HashSet<Node> nextHashSet : linkedTransitionViews) {
                    for (Node node : nextHashSet) {
                        if (node instanceof TransitionNode) {
                            TransitionNode transitionNodeToUpdate = (TransitionNode) node;
                            if (transitionNodeToUpdate.getResultingStateNode().getStateId().equals(resultingStateModelID)) {
                                if (getRelatedTransitions(deletedTransition).isEmpty()) {
                                    transitionViewNodesToRemoveSet.add(nextHashSet);
                                    stateViewsWithTransitionToRemoveSet.add(currentStateNode);
                                } else {
                                    VBox listOfTransitionsVBox = transitionNodeToUpdate.getTransitionsListVBox();
                                    Iterator<Node> iter = listOfTransitionsVBox.getChildren().iterator();
                                    while (iter.hasNext()) {
                                        Label labelToRemove = (Label) iter.next();
                                        if (labelToRemove.getText().equals(deletedTransition.toString())) {
                                            iter.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!(transitionViewNodesToRemoveSet.isEmpty())) {
            // Get affect key i.e. ControlState view to access map
            for (StateNode stateNode : stateViewsWithTransitionToRemoveSet) {
                // Retrieve all transitionview linked to stateview
                Iterator<HashSet<Node>> iter = linkedTransitionViewsMap.get(stateNode).iterator();
                while (iter.hasNext()) {
                    HashSet<Node> nextHashSet = iter.next();
                    for (HashSet<Node> nodeSetToRemove : transitionViewNodesToRemoveSet) {
                        //Find the hashset containing the components for the transition view in the map = the hashset to remove
                        if (nextHashSet.equals(nodeSetToRemove)) {
                            for (Node node : nextHashSet) {
                                if (node instanceof TransitionNode) {
                                    TransitionNode isTransitionNode = (TransitionNode) node;
                                    diagramScene.getChildren().remove(isTransitionNode.getTransitionsListVBox());
                                }
                                diagramScene.getChildren().remove(node);
                            }
                            iter.remove();
                        }
                    }
                }
            }
        }
    }
    /**
     * Retrieves the transition models related to a given transition model.
     * @param transitionModel used to determine all related transition model found in the transition model set in a
     * machine model.
     * @return {@code HashSet<TransitionModel>} of related transition models to a given transition model.
     */
    private HashSet<TransitionModel> getRelatedTransitions(TransitionModel transitionModel) {
        HashSet<TransitionModel> relatedTransitionModelsToReturn = new HashSet<>();
        StateModel currentStateModelToCompare = transitionModel.getCurrentStateModel();
        StateModel resultingStateModelToCompare = transitionModel.getResultingStateModel();
        for (TransitionModel isRelatedTransitionModel : machineModel.getTransitionModelSet()) {
            StateModel currentStateModel = isRelatedTransitionModel.getCurrentStateModel();
            StateModel resultingStateModel = isRelatedTransitionModel.getResultingStateModel();
            if (isRelatedTransitionModel != transitionModel) {
                if (currentStateModelToCompare.equals(currentStateModel) && resultingStateModelToCompare.equals(resultingStateModel)) {
                    relatedTransitionModelsToReturn.add(isRelatedTransitionModel);
                }
            }
        }
        return relatedTransitionModelsToReturn;
    }
    /**
     * Handles when the mouse button has been pressed given the mouse has selected a state node.
     * @param stateNode selected by mouse to be pressed.
     * @param xPositionOfMouse to move the state node to on the diagram scene.
     * @param yPositionOfMouse to move the state node to on the diagram scene.
     */
    public void stateViewOnMousePressed(StateNode stateNode, double xPositionOfMouse, double yPositionOfMouse) {
        sceneX = xPositionOfMouse;
        sceneY = yPositionOfMouse;
        layoutX = stateNode.getLayoutX();
        layoutY = stateNode.getLayoutY();
    }
    /**
     * Handles when the mouse button has been dragged given the mouse has selected a state node.
     * @param stateNode selected by mouse to be dragged.
     * @param xPositionOfMouse to move the state node to on the diagram scene.
     * @param yPositionOfMouse to move the state node to on the diagram scene.
     */
    public void stateViewOnMouseDragged(StateNode stateNode, double xPositionOfMouse, double yPositionOfMouse) {
        // Offset of drag
        double offsetX = xPositionOfMouse - sceneX;
        double offsetY = yPositionOfMouse - sceneY;
        // Taking parent bounds
        Bounds parentBounds = stateNode.getParent().getLayoutBounds();
        // Drag node bounds
        double currPaneLayoutX = stateNode.getLayoutX();
        double currPaneWidth = stateNode.getWidth();
        double currPaneLayoutY = stateNode.getLayoutY();
        double currPaneHeight = stateNode.getHeight();
        if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
            // If the dragNode bounds is within the parent bounds, then you can set the offset value.
            stateNode.setTranslateX(offsetX);
        } else if (currPaneLayoutX + offsetX < 0) {
            // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
            // which makes the final layout position to 0 in mouse released event.
            stateNode.setTranslateX(-currPaneLayoutX);
        } else {
            // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
            stateNode.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
        }
        if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
            stateNode.setTranslateY(offsetY);
        } else if (currPaneLayoutY + offsetY < 0) {
            stateNode.setTranslateY(-currPaneLayoutY);
        } else {
            stateNode.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
        }
    }
    /**
     * Handles when the mouse button has been released given the mouse has selected a state node.
     * @param stateNode selected by mouse to be released.
     */
    public void stateViewOnMouseReleased(StateNode stateNode) {
        // Updating the new layout positions
        stateNode.setLayoutX(layoutX + stateNode.getTranslateX());
        stateNode.setLayoutY(layoutY + stateNode.getTranslateY());
        // Updating state model coordinates
        StateModel stateModelDragged = machineModel.getStateModelFromStateModelSet(stateNode.getStateId());
        stateModelDragged.setXCoordinateOnDiagram(layoutX + stateNode.getTranslateX());
        stateModelDragged.setYCoordinateOnDiagram(layoutY + stateNode.getTranslateY());
        // Resetting the translate positions
        stateNode.setTranslateX(0);
        stateNode.setTranslateY(0);
        // Relocate state node
        stateNode.relocate(stateModelDragged.getXCoordinateOnDiagram(), stateModelDragged.getYCoordinateOnDiagram());
    }
    /**
     * Handles the highlighting of the movement to a given configuration.
     * @param selectedConfiguration used to determine how to highlight the diagram.
     */
    void highlightTransitionTakenInDiagram(ConfigurationModel selectedConfiguration) {
        TransitionModel transitionModelToHighlight = selectedConfiguration.getTransitionModelTakenToReachCurrentConfiguration();
        if (transitionModelToHighlight == null) {
            this.startStateNode = stateMap.get(selectedConfiguration.getCurrentStateModel());
            startStateNode.getStateCircle().setStroke(Color.LAWNGREEN);
            if (transitionModelHighlighted != null) {
                removeHighlightedTransitionView();
                startStateNode.getStateCircle().setStroke(Color.LAWNGREEN);
            }
        } else {
            if (startStateNode != null) {
                startStateNode.getStateCircle().setStroke(Color.BLACK);
            }
            if (transitionModelHighlighted != null) {
                removeHighlightedTransitionView();
            }
            transitionModelHighlighted = transitionModelToHighlight;
            StateNode currentStateNode = stateMap.get(transitionModelToHighlight.getCurrentStateModel());
            StateNode resultingStateNode = stateMap.get(transitionModelToHighlight.getResultingStateModel());
            currentStateNode.getStateCircle().setStroke(Color.LAWNGREEN);
            resultingStateNode.getStateCircle().setStroke(Color.LAWNGREEN);
            if (transitionModelToHighlight.getCurrentStateModel().equals(transitionModelToHighlight.getResultingStateModel())) {
                currentStateNode.getReflexiveArrowShaftArc().setStroke(Color.LAWNGREEN);
                currentStateNode.getReflexiveArrowTipPolygon().setFill(Color.LAWNGREEN);
                currentStateNode.getReflexiveArrowTipPolygon().setStroke(Color.LAWNGREEN);
            } else {
                HashSet<Node> transitionViewHighlightedSet = retrieveDirectionalTransitionView(transitionModelToHighlight);
                for (Node node : transitionViewHighlightedSet) {
                    if (node instanceof TransitionNode) {
                        TransitionNode transitionNode = (TransitionNode) node;
                        transitionNode.setStroke(Color.LAWNGREEN);
                    }
                    if (node instanceof StackPane) {
                        StackPane arrowTipStackPane = (StackPane) node;
                        arrowTipStackPane.setStyle("-fx-background-color:lawngreen;-fx-border-width:2px;-fx-border-color:lawngreen;-fx-shape: \"M0,-4L4,0L0,4Z\"");
                    }
                }
            }
        }
    }
    /**
     * Remove the currently highlighted items in the diagram scene.
     */
    void removeHighlightedTransitionView() {
        if (transitionModelHighlighted != null) {
            StateNode currentStateNode = stateMap.get(transitionModelHighlighted.getCurrentStateModel());
            StateNode resultingStateNode = stateMap.get(transitionModelHighlighted.getResultingStateModel());
            currentStateNode.getStateCircle().setStroke(Color.BLACK);
            if (resultingStateNode != null) {
                resultingStateNode.getStateCircle().setStroke(Color.BLACK);
            }
            if (transitionModelHighlighted.getCurrentStateModel().equals(transitionModelHighlighted.getResultingStateModel())) {
                currentStateNode.getReflexiveArrowShaftArc().setStroke(Color.BLACK);
                currentStateNode.getReflexiveArrowTipPolygon().setFill(Color.BLACK);
                currentStateNode.getReflexiveArrowTipPolygon().setStroke(Color.BLACK);
            } else {
                HashSet<Node> transitionViewHighlightedSet = retrieveDirectionalTransitionView(transitionModelHighlighted);
                for (Node node : transitionViewHighlightedSet) {
                    if (node instanceof TransitionNode) {
                        TransitionNode transitionNode = (TransitionNode) node;
                        transitionNode.setStroke(Color.BLACK);
                    }
                    if (node instanceof StackPane) {
                        StackPane arrowTipStackPane = (StackPane) node;
                        arrowTipStackPane.setStyle("-fx-background-color:black;-fx-border-width:2px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\"");
                    }
                }
            }
        }
    }
    /**
     * Handles retrieval of a directional transition nodes related to a given transition model.
     * @param transitionModel used to determine related transition node found on the diagram scene.
     * @return {@code HashSet<Node> } of components that make up the transition node on the diagram scene.
     */
    public HashSet<Node> retrieveDirectionalTransitionView(TransitionModel transitionModel) {
        StateNode currentStateNode = stateMap.get(transitionModel.getCurrentStateModel());
        StateNode resultingStateNode = stateMap.get(transitionModel.getResultingStateModel());
        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateNode);
        HashSet<Node> TransitionViewSet = new HashSet<>();
        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionNode) {
                    TransitionNode transitionNodeToUpdate = (TransitionNode) node;
                    if (transitionNodeToUpdate.getResultingStateNode().getStateId().equals(resultingStateNode.getStateId())) {
                        TransitionViewSet.add(transitionNodeToUpdate);
                    }
                } else if (node instanceof StackPane) {
                    StackPane arrowTipStackPaneToUpdate = (StackPane) node;
                    if (arrowTipStackPaneToUpdate.getId() == null) {
                        continue;
                    }
                    if (arrowTipStackPaneToUpdate.getId().equals(resultingStateNode.getStateId())) {
                        TransitionViewSet.add(arrowTipStackPaneToUpdate);
                    }
                }
            }
        }
        return TransitionViewSet;
    }
    public DiagramScene getDiagramScene() {
        return diagramScene;
    }
    public Map<StateModel, StateNode> getStateMap() {
        return stateMap;
    }
}