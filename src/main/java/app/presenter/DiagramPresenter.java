package app.presenter;
import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.DiagramScene;
import app.view.MainStage;
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
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
public class DiagramPresenter {
    private final MainStage mainStage;
    private final MainStagePresenter mainStagePresenter;
    private final MachineModel machineModel;
    private final DiagramScene diagramScene;
    private TransitionTablePresenter transitionTablePresenter;
    // Defines the x and y coordinate of the translation that is added to this {@code Node}'transform for the purpose of layout.
    private double layoutX;
    private double layoutY;
    private double sceneX;
    private double sceneY;
    private TransitionModel transitionModelHighlighted;
    private StateNode startStateNode;
    private Map<StateModel, StateNode> stateMap;
    private Map<StateNode, HashSet<HashSet<Node>>> linkedTransitionViewsMap;
    private LinkedHashMap<TransitionNode, PopOver> popOvers;
    DiagramPresenter(MainStage mainStage, MainStagePresenter mainStagePresenter, MachineModel machineModel) {
        this.mainStagePresenter = mainStagePresenter;
        this.mainStage = mainStage;
        this.machineModel = machineModel;
        this.diagramScene = new DiagramScene(this);
        this.stateMap = new HashMap<>();
        this.linkedTransitionViewsMap = new HashMap<>();
    }
    void loadDiagramViewOntoStage(TransitionTablePresenter transitionTablePresenter) {
        this.transitionTablePresenter = transitionTablePresenter;
        this.mainStage.getContainerForCenterNodes().getChildren().add(diagramScene);
    }
    void loadStatesOntoDiagram() {
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
    public void addStateViewOntoDiagramView(StateModel newStateModel) {
        StateNode stateNode = new StateNode(newStateModel.getStateId(), this);
        stateNode.relocate(newStateModel.getxCoordinateOnDiagram(), newStateModel.getyCoordinateOnDiagram());
        diagramScene.getChildren().add(stateNode);
        stateMap.put(newStateModel, stateNode);
        linkedTransitionViewsMap.put(stateNode, new HashSet<>());
    }
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
        newStateModel.setxCoordinateOnDiagram(xCoordinateOnDiagram);
        newStateModel.setyCoordinateOnDiagram(yCoordinateOnDiagram);
        machineModel.addStateModelToStateModelSet(newStateModel);
        addStateViewOntoDiagramView(newStateModel);
        transitionTablePresenter.updateAvailableStateListForCombobox();
    }
    public void stateViewContextMenuPopUp(StateNode stateNode) {
        StateModel stateModelSelected = machineModel.getStateModelFromStateModelSet(stateNode.getStateID());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");
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
                if (machineModel.findStartStateModel() != null) { // Check to see if start state exists in machine
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "Only one initial state allowed per machine. " + "State " + machineModel.findStartStateModel() + " is currently defined as the initial state for this machine.", ButtonType.OK);
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
            inputSymbolComboBox.setPrefWidth(110);
            inputSymbolComboBox.setEditable(true);
            inputSymbolComboBox.getItems().addAll(machineModel.getInputAlphabetSet());
            setUpComboBoxesListeners(inputSymbolComboBox);
            gridPane.add(new Label("Input Symbol"), 2, 1);
            gridPane.add(inputSymbolComboBox, 2, 2);
            ComboBox<String> stackSymbolToPopComboBox = new ComboBox<>();
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
            stackSymbolToPushComboBox.setPrefWidth(110);
            stackSymbolToPushComboBox.setEditable(true);
            stackSymbolToPushComboBox.getItems().addAll(machineModel.getStackAlphabetSet());
            setUpComboBoxesListeners(stackSymbolToPushComboBox);
            gridPane.add(new Label("Stack Symbol to Push"), 6, 1);
            gridPane.add(stackSymbolToPushComboBox, 6, 2);
            //Create submit button for the user to submit a transition
            Button submitTransitionButton = new Button("Submit");
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
                StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingState);
                if (resultingStateModel == null) {
                    resultingStateModel = new StateModel(userEntryResultingState);
                    machineModel.addStateModelToStateModelSet(resultingStateModel);
                    this.addStateViewOntoDiagramView(resultingStateModel);
                    transitionTablePresenter.updateAvailableStateListForCombobox();
                }
                //Create transition model placeholder
                TransitionModel newTransitionModel = new TransitionModel(stateModelSelected, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);
                //Check to see if the transition already exists for the current state model
                for (TransitionModel transitionModel : getExitingTranstionsFromStateModel(stateModelSelected)) {
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
                transitionTablePresenter.addTransitionModelEntryToTransitionTable(newTransitionModel);
                transitionTablePresenter.updateAvailableStateListForCombobox();
                transitionTablePresenter.updateInputAlphabetForComboBox();
                transitionTablePresenter.updateStackAlphabetForComboBox();
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
            //Update machine model
            HashSet<TransitionModel> exitingTranstionsFromStateModel = getExitingTranstionsFromStateModel(stateModelSelected);
            HashSet<TransitionModel> enteringTranstionsFromStateModel = getEnteringTransitionsFromStateModel(stateModelSelected);
            machineModel.removeTransitionModelsFromTransitionModelSet(exitingTranstionsFromStateModel);
            machineModel.removeTransitionModelsFromTransitionModelSet(enteringTranstionsFromStateModel);
            machineModel.removeStateModelFromStateModelSet(stateModelSelected);
            //Notify transition table controller
            transitionTablePresenter.deleteTransitionsLinkedToDeletedStateFromTransitionTable(exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
            //Update view
            //Update view
            deleteStateViewOnDiagramView(stateModelSelected, exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
        });
        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);
        contextMenu.show(stateNode, Side.RIGHT, 5, 5);
    }
    private StackPane createArrowTip(boolean toLineEnd, Line line, StackPane startDot, StackPane endDot) {
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
                dt = lineLength - (endDot.getWidth() / 2) - (arrowTipStackPane.getWidth() / 2);
            } else {
                // When determining the point towards start, the required distance is just (radius + arrow half width)
                dt = (startDot.getWidth() / 2) + (arrowTipStackPane.getWidth() / 2);
            }
            double t = dt / lineLength;
            double dx = ((1 - t) * line.getStartX()) + (t * line.getEndX());
            return dx;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());
        // Determining the y point on the line which is at a certain distance.
        DoubleBinding tY = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                dt = lineLength - (endDot.getHeight() / 2) - (arrowTipStackPane.getHeight() / 2);
            } else {
                dt = (startDot.getHeight() / 2) + (arrowTipStackPane.getHeight() / 2);
            }
            double t = dt / lineLength;
            double dy = ((1 - t) * line.getStartY()) + (t * line.getEndY());
            return dy;
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
    public void addReflexiveTransitionToDiagramView(TransitionModel newTransitionModel) {
        StateNode sourceCell = stateMap.get(newTransitionModel.getCurrentStateModel());
        if (sourceCell.getListOfTransitionsVBox() == null) {
            VBox newListOfTransitionsVBox = new VBox();
            newListOfTransitionsVBox.setStyle("-fx-background-color:#ffffff;-fx-border-width:2px;-fx-border-color:black;");
            newListOfTransitionsVBox.setOnMousePressed(mouseEvent -> {
                sceneX = mouseEvent.getScreenX();
                sceneY = mouseEvent.getScreenY();
                layoutX = newListOfTransitionsVBox.getLayoutX();
                layoutY = newListOfTransitionsVBox.getLayoutY();
            });
            newListOfTransitionsVBox.setOnMouseDragged(mouseEvent -> {
                // Offset of drag
                double offsetX = mouseEvent.getScreenX() - sceneX;
                double offsetY = mouseEvent.getScreenY() - sceneY;
                // Taking parent bounds
                Bounds parentBounds = newListOfTransitionsVBox.getParent().getLayoutBounds();
                // Drag node bounds
                double currPaneLayoutX = newListOfTransitionsVBox.getLayoutX();
                double currPaneWidth = newListOfTransitionsVBox.getWidth();
                double currPaneLayoutY = newListOfTransitionsVBox.getLayoutY();
                double currPaneHeight = newListOfTransitionsVBox.getHeight();
                if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                    // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                    newListOfTransitionsVBox.setTranslateX(offsetX);
                } else if (currPaneLayoutX + offsetX < 0) {
                    // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                    // which makes the final layout position to 0 in mouse released event.
                    newListOfTransitionsVBox.setTranslateX(-currPaneLayoutX);
                } else {
                    // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                    newListOfTransitionsVBox.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
                }
                if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                    newListOfTransitionsVBox.setTranslateY(offsetY);
                } else if (currPaneLayoutY + offsetY < 0) {
                    newListOfTransitionsVBox.setTranslateY(-currPaneLayoutY);
                } else {
                    newListOfTransitionsVBox.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
                }
            });
            newListOfTransitionsVBox.setOnMouseReleased(event -> {
                // Updating the new layout positions
                newListOfTransitionsVBox.setLayoutX(layoutX + newListOfTransitionsVBox.getTranslateX());
                newListOfTransitionsVBox.setLayoutY(layoutY + newListOfTransitionsVBox.getTranslateY());
                //Update coordinate stored in transition tabel model
                newTransitionModel.setxCoordinateOnDiagram(layoutX + newListOfTransitionsVBox.getTranslateX());
                newTransitionModel.setyCoordinateOnDiagram(layoutY + newListOfTransitionsVBox.getTranslateY());
                // Resetting the translate positions
                newListOfTransitionsVBox.setTranslateX(0);
                newListOfTransitionsVBox.setTranslateY(0);
            });
            sourceCell.setListOfTransitionsVBox(newListOfTransitionsVBox);
            diagramScene.getChildren().add(sourceCell.getListOfTransitionsVBox());
        }
        sourceCell.getListOfTransitionsVBox().getChildren().clear();
        for (TransitionModel transitionModel : getRelatedTransitions(newTransitionModel)) {
            Label newLabel = new Label(transitionModel.toString());
            sourceCell.getListOfTransitionsVBox().getChildren().add(newLabel);
        }
        sourceCell.getListOfTransitionsVBox().relocate(newTransitionModel.getxCoordinateOnDiagram(), newTransitionModel.getyCoordinateOnDiagram());
        sourceCell.getReflexiveArrowShaftArc().setVisible(true);
        sourceCell.getReflexiveArrowTipPolygon().setVisible(true);
    }
    void addDirectionalTransitionToView(TransitionModel newTransitionModel) {
        //Get state from map using state ID
        StateNode currentStateNode = stateMap.get(newTransitionModel.getCurrentStateModel());
        StateNode resultingStateNode = stateMap.get(newTransitionModel.getResultingStateModel());
        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateNode);
        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionNode) {
                    TransitionNode transitionNodeToCheck = (TransitionNode) node;
                    if (transitionNodeToCheck.getCurrentStateNode() == currentStateNode && transitionNodeToCheck.getResultingStateNode()
                            ==
                            resultingStateNode) {
                        VBox newTransitionListVBox = transitionNodeToCheck.getTransitionListVBox();
                        newTransitionListVBox.getChildren().clear();
                        for (TransitionModel transitionModel : getRelatedTransitions(newTransitionModel)) {
                            newTransitionListVBox.getChildren().add(new Label(transitionModel.toString()));
                        }
                        newTransitionListVBox.relocate(newTransitionModel.getxCoordinateOnDiagram(), newTransitionModel.getyCoordinateOnDiagram());
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
        VBox newTransitionListVBox = transitionNode.getTransitionListVBox();
        newTransitionListVBox.setOnMousePressed(mouseEvent -> {
            sceneX = mouseEvent.getScreenX();
            sceneY = mouseEvent.getScreenY();
            layoutX = newTransitionListVBox.getLayoutX();
            layoutY = newTransitionListVBox.getLayoutY();
        });
        newTransitionListVBox.setOnMouseDragged(mouseEvent -> {
            // Offset of drag
            double offsetX = mouseEvent.getScreenX() - sceneX;
            double offsetY = mouseEvent.getScreenY() - sceneY;
            // Taking parent bounds
            Bounds parentBounds = newTransitionListVBox.getParent().getLayoutBounds();
            // Drag node bounds
            double currPaneLayoutX = newTransitionListVBox.getLayoutX();
            double currPaneWidth = newTransitionListVBox.getWidth();
            double currPaneLayoutY = newTransitionListVBox.getLayoutY();
            double currPaneHeight = newTransitionListVBox.getHeight();
            if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                newTransitionListVBox.setTranslateX(offsetX);
            } else if (currPaneLayoutX + offsetX < 0) {
                // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                // which makes the final layout position to 0 in mouse released event.
                newTransitionListVBox.setTranslateX(-currPaneLayoutX);
            } else {
                // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                newTransitionListVBox.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
            }
            if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                newTransitionListVBox.setTranslateY(offsetY);
            } else if (currPaneLayoutY + offsetY < 0) {
                newTransitionListVBox.setTranslateY(-currPaneLayoutY);
            } else {
                newTransitionListVBox.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
            }
        });
        newTransitionListVBox.setOnMouseReleased(event -> {
            // Updating the new layout positions
            newTransitionListVBox.setLayoutX(layoutX + newTransitionListVBox.getTranslateX());
            newTransitionListVBox.setLayoutY(layoutY + newTransitionListVBox.getTranslateY());
            //Set transitionlist model coordinates
            newTransitionModel.setxCoordinateOnDiagram(layoutX + newTransitionListVBox.getTranslateX());
            newTransitionModel.setyCoordinateOnDiagram(layoutY + newTransitionListVBox.getTranslateY());
            // Resetting the translate positions
            newTransitionListVBox.setTranslateX(0);
            newTransitionListVBox.setTranslateY(0);
        });
        for (TransitionModel transitionModel : getRelatedTransitions(newTransitionModel)) {
            newTransitionListVBox.getChildren().add(new Label(transitionModel.toString()));
        }
        newTransitionListVBox.relocate(newTransitionModel.getxCoordinateOnDiagram(), newTransitionModel.getyCoordinateOnDiagram());
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
        diagramScene.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, transitionNode, arrowTip, transitionNode.getTransitionListVBox());
        diagramScene.getChildren().addAll(currentStateNode, resultingStateNode);
    }
    private void deleteStateViewOnDiagramView(StateModel stateModelToDelete, HashSet<TransitionModel> exitingTransitionModelsSetToDelete, HashSet<TransitionModel> enteringTransitionModelsSetToDelete) {
        //Retrieve stateview to be deleted
        StateNode stateNodeToRemove = stateMap.get(stateModelToDelete);
        //Retrieve and remove linked transitionviews to stateview
        deleteTransitionView(exitingTransitionModelsSetToDelete);
        deleteTransitionView(enteringTransitionModelsSetToDelete);
        // Remove mapping of stateview in data structures
        stateMap.remove(stateModelToDelete);
        linkedTransitionViewsMap.remove(stateNodeToRemove);
        //Remove the stateview from the diagramview
        diagramScene.getChildren().remove(stateNodeToRemove);
    }
    //StateGUIEventResponses
    public void stateViewOnMousePressed(StateNode stateNode, double xPositionOfMouse, double yPositionOfMouse) {
        sceneX = xPositionOfMouse;
        sceneY = yPositionOfMouse;
        layoutX = stateNode.getLayoutX();
        layoutY = stateNode.getLayoutY();
    }
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
    public void stateViewOnMouseReleased(StateNode stateNode) {
        // Updating the new layout positions
        stateNode.setLayoutX(layoutX + stateNode.getTranslateX());
        stateNode.setLayoutY(layoutY + stateNode.getTranslateY());
        // Updating state model coordinates
        StateModel stateModelDragged = machineModel.getStateModelFromStateModelSet(stateNode.getStateID());
        stateModelDragged.setxCoordinateOnDiagram(layoutX + stateNode.getTranslateX());
        stateModelDragged.setyCoordinateOnDiagram(layoutY + stateNode.getTranslateY());
        // Resetting the translate positions
        stateNode.setTranslateX(0);
        stateNode.setTranslateY(0);
    }
    void deleteTransitionView(HashSet<TransitionModel> deletedTransitionModelsSet) {
        HashSet<HashSet<Node>> transitionViewNodesToRemoveSet = new HashSet<>();
        HashSet<StateNode> stateViewsWithTransitionToRemoveSet = new HashSet<>();
        for (TransitionModel deletedTransition : deletedTransitionModelsSet) {
            String currentStateModelID = deletedTransition.getCurrentStateModel().getStateId();
            String resultingStateModelID = deletedTransition.getResultingStateModel().getStateId();
            StateNode currentStateNode = stateMap.get(deletedTransition.getCurrentStateModel());
            //Check type of transition
            if (currentStateModelID.equals(resultingStateModelID)) {
                VBox listOfTransitionsVBox = currentStateNode.getListOfTransitionsVBox();
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
                            if (transitionNodeToUpdate.getCurrentStateNode().getStateID().equals(currentStateModelID) &&
                                    transitionNodeToUpdate.getResultingStateNode().getStateID().equals(resultingStateModelID)) {
                                if (getRelatedTransitions(deletedTransition).isEmpty()) {
                                    transitionViewNodesToRemoveSet.add(nextHashSet);
                                    stateViewsWithTransitionToRemoveSet.add(currentStateNode);
                                }
                                for (TransitionModel transitionModel : getRelatedTransitions(deletedTransition)) {
                                    transitionNodeToUpdate.getTransitionListVBox().getChildren().add(new Label(transitionModel.toString()));
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
                        if (nextHashSet == nodeSetToRemove) {
                            for (Node node : nextHashSet) {
                                if (node instanceof TransitionNode) {
                                    TransitionNode isTransitionNode = (TransitionNode) node;
                                    diagramScene.getChildren().remove(isTransitionNode.getTransitionListVBox());
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
    private HashSet<Node> retrieveDirectionalTransitionView(TransitionModel transitionModel) {
        StateNode currentStateNode = stateMap.get(transitionModel.getCurrentStateModel());
        StateNode resultingStateNode = stateMap.get(transitionModel.getResultingStateModel());
        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateNode);
        HashSet<Node> TransitionViewSet = new HashSet<>();
        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionNode) {
                    TransitionNode transitionNodeToUpdate = (TransitionNode) node;
                    if (transitionNodeToUpdate.getResultingStateNode().getStateID().equals(resultingStateNode.getStateID())) {
                        TransitionViewSet.add(transitionNodeToUpdate);
                    }
                } else if (node instanceof StackPane) {
                    StackPane arrowTipStackPaneToUpdate = (StackPane) node;
                    if (arrowTipStackPaneToUpdate.getId() == null) {
                        continue;
                    }
                    if (arrowTipStackPaneToUpdate.getId().equals(resultingStateNode.getStateID())) {
                        TransitionViewSet.add(arrowTipStackPaneToUpdate);
                    }
                }
            }
        }
        return TransitionViewSet;
    }
    public HashSet<TransitionModel> getRelatedTransitions(TransitionModel transitionModel) {
        HashSet<TransitionModel> relatedTransitionModelsToReturn = new HashSet<>();
        StateModel currentStateModelToCompare = transitionModel.getCurrentStateModel();
        StateModel resultingStateModelToCompare = transitionModel.getResultingStateModel();
        for (TransitionModel isRelatedTransitionModel : machineModel.getTransitionModelSet()) {
            StateModel currentStateModel = isRelatedTransitionModel.getCurrentStateModel();
            StateModel resultingStateModel = isRelatedTransitionModel.getResultingStateModel();
            if (currentStateModelToCompare.equals(currentStateModel) && resultingStateModelToCompare.equals(resultingStateModel)) {
                relatedTransitionModelsToReturn.add(isRelatedTransitionModel);
            }
        }
        return relatedTransitionModelsToReturn;
    }
    public HashSet<TransitionModel> getExitingTranstionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> exitingTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : machineModel.getTransitionModelSet()) {
            if (isExitingTransitionModel.getCurrentStateModel().equals(stateModel)) {
                exitingTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return exitingTransitionFromStateModelToReturn;
    }
    public HashSet<TransitionModel> getEnteringTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> enteringTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isEnteringTransitionModel : machineModel.getTransitionModelSet()) {
            if (isEnteringTransitionModel.getResultingStateModel().equals(stateModel)) {
                enteringTransitionFromStateModelToReturn.add(isEnteringTransitionModel);
            }
        }
        return enteringTransitionFromStateModelToReturn;
    }
}