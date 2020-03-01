package app.controller;

import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.DiagramView;
import app.view.MainStageView;
import app.view.StateView;
import app.view.TransitionView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import java.util.*;

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
    private TransitionModel transitionModelHighlighted;
    private StateView startStateView;

    private Map<StateModel, StateView> stateMap;
    private Map<StateView, HashSet<HashSet<Node>>> linkedTransitionViewsMap;
    private LinkedHashMap<TransitionView, PopOver> popOvers;


    public DiagramController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {
        this.mainStageController = mainStageController;
        this.mainStageView = mainStageView;
        this.machineModel = machineModel;
        this.diagramView = new DiagramView(this, mainStageView);

        this.stateMap = new HashMap<>();
        this.linkedTransitionViewsMap = new HashMap<>();
    }


    public void loadDiagramViewOntoStage(TransitionTableController transitionTableController) {
        this.transitionTableController = transitionTableController;
        this.mainStageView.getContainerForCenterNodes().getChildren().add(diagramView);
    }


    public void loadStatesOntoDiagram() {
        StateView stateViewToLoad;
        for (StateModel stateModelToLoad : machineModel.getStateModelSet()) {
            addStateViewOntoDiagramView(stateModelToLoad);
            stateViewToLoad = stateMap.get(stateModelToLoad);
            if (stateModelToLoad.isStartState()) {
                stateViewToLoad.getStartStatePointLine1().setVisible(stateModelToLoad.isStartState());
                stateViewToLoad.getStartStatePointLine2().setVisible(stateModelToLoad.isStartState());
            }
            if (stateModelToLoad.isFinalState()) {
                stateViewToLoad.getFinalStateArc().setVisible(stateModelToLoad.isFinalState());
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
        //Create stateview UI
        StateView stateView = new StateView(newStateModel.getStateId(), this);
        stateView.relocate(newStateModel.getxCoordinateOnDiagram(), newStateModel.getyCoordinateOnDiagram());
        diagramView.getChildren().add(stateView);
        stateMap.put(newStateModel, stateView);
        linkedTransitionViewsMap.put(stateView, new HashSet<>());
    }


    public void addStateViewOntoDiagramViewDynamicRender(double x, double y) {
        StateModel newStateModel = null;
        if (machineModel.getStateModelSet().isEmpty()) {
            newStateModel = new StateModel(x, y);
        } else {
            boolean stateModelExists = true;
            outerloop:
            while (stateModelExists) {
                newStateModel = new StateModel(x, y);
                for (StateModel stateModel : machineModel.getStateModelSet()) {
                    if (stateModel.equals(newStateModel)) {
                        continue outerloop;
                    }
                }
                stateModelExists = false;
            }
        }
        machineModel.addStateModelToStateModelSet(newStateModel);
        addStateViewOntoDiagramView(newStateModel);
        transitionTableController.updateAvailableStateListForCombobox();
    }


    public void addDirectionalTransitionToView(TransitionModel newTransitionModel) {

        //Get state from map using state ID
        StateView currentStateView = stateMap.get(newTransitionModel.getCurrentStateModel());
        StateView resultingStateView = stateMap.get(newTransitionModel.getResultingStateModel());

        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateView);

        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionView) {
                    TransitionView transitionViewToCheck = (TransitionView) node;
                    if (transitionViewToCheck.getSource() == currentStateView && transitionViewToCheck.getTarget() == resultingStateView) {
                        createNewListOfTransitionsPopOver(transitionViewToCheck, machineModel.getRelatedTransitions(newTransitionModel));
                        return;
                    }
                }
            }
        }

        //Transition does not exist create fresh transition
        diagramView.getChildren().remove(currentStateView);
        diagramView.getChildren().remove(resultingStateView);

        Line virtualCenterLine = new Line();
        virtualCenterLine.startXProperty().bind(currentStateView.layoutXProperty().add(currentStateView.translateXProperty()).add(currentStateView.widthProperty().divide(2)));
        virtualCenterLine.startYProperty().bind(currentStateView.layoutYProperty().add(currentStateView.translateYProperty()).add(currentStateView.heightProperty().divide(2)));
        virtualCenterLine.endXProperty().bind(resultingStateView.layoutXProperty().add(resultingStateView.translateXProperty()).add(resultingStateView.widthProperty().divide(2)));
        virtualCenterLine.endYProperty().bind(resultingStateView.layoutYProperty().add(resultingStateView.translateYProperty()).add(resultingStateView.heightProperty().divide(2)));
        virtualCenterLine.setOpacity(0);

        StackPane centerLineArrowAB = createArrowTip(true, virtualCenterLine, currentStateView, resultingStateView);
        centerLineArrowAB.setOpacity(0);
        StackPane centerLineArrowBA = createArrowTip(false, virtualCenterLine, currentStateView, resultingStateView);
        centerLineArrowBA.setOpacity(0);

        TransitionView transitionView = new TransitionView(currentStateView, resultingStateView);
        transitionView.setStroke(Color.BLACK);
        transitionView.setStrokeWidth(2);
        createNewListOfTransitionsPopOver(transitionView, machineModel.getRelatedTransitions(newTransitionModel));

        double diff = true ? -centerLineArrowAB.getPrefWidth() / 2 : centerLineArrowAB.getPrefWidth() / 2;
        final ChangeListener<Number> listener = (obs, old, newVal) -> {
            Rotate r = new Rotate();
            r.setPivotX(virtualCenterLine.getStartX());
            r.setPivotY(virtualCenterLine.getStartY());
            r.setAngle(centerLineArrowAB.getRotate());
            Point2D point = r.transform(new Point2D(virtualCenterLine.getStartX(), virtualCenterLine.getStartY() + diff));
            transitionView.setStartX(point.getX());
            transitionView.setStartY(point.getY());

            Rotate r2 = new Rotate();
            r2.setPivotX(virtualCenterLine.getEndX());
            r2.setPivotY(virtualCenterLine.getEndY());
            r2.setAngle(centerLineArrowBA.getRotate());
            Point2D point2 = r2.transform(new Point2D(virtualCenterLine.getEndX(), virtualCenterLine.getEndY() - diff));
            transitionView.setEndX(point2.getX());
            transitionView.setEndY(point2.getY());
        };
        centerLineArrowAB.rotateProperty().addListener(listener);
        centerLineArrowBA.rotateProperty().addListener(listener);
        virtualCenterLine.startXProperty().addListener(listener);
        virtualCenterLine.startYProperty().addListener(listener);
        virtualCenterLine.endXProperty().addListener(listener);
        virtualCenterLine.endYProperty().addListener(listener);


        StackPane arrowTip = createArrowTip(true, transitionView, currentStateView, resultingStateView);
        arrowTip.setId(newTransitionModel.getResultingStateModel().getStateId());

        HashSet<Node> setOfNode = new HashSet<>();
        setOfNode.add(virtualCenterLine);
        setOfNode.add(centerLineArrowAB);
        setOfNode.add(centerLineArrowBA);
        setOfNode.add(transitionView);
        setOfNode.add(arrowTip);
        linkedTransitionViewsMap.get(currentStateView).add(setOfNode);

        diagramView.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, transitionView, arrowTip);
        diagramView.getChildren().addAll(currentStateView, resultingStateView);

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


    private void createNewListOfTransitionsPopOver(TransitionView transitionViewToCheck, HashSet<TransitionModel> newTransitionModelsAttachedToStateModelSet) {
        VBox newVBox = new VBox();
        PopOver newListOfTransitionsPopOver = new PopOver(newVBox);

        newListOfTransitionsPopOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        newListOfTransitionsPopOver.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_BOTTOM_RIGHT);
        newListOfTransitionsPopOver.setArrowIndent(5);
        newListOfTransitionsPopOver.setAutoHide(false);
        newListOfTransitionsPopOver.setAnimated(false);
        newListOfTransitionsPopOver.setDetachable(false);
        newListOfTransitionsPopOver.setDetached(false);

        for (TransitionModel transitionModel : newTransitionModelsAttachedToStateModelSet) {
            newVBox.getChildren().add(new Label(transitionModel.toString()));
        }

        transitionViewToCheck.setOnMouseEntered(mouseEvent -> {
            newListOfTransitionsPopOver.show(transitionViewToCheck);
        });


        transitionViewToCheck.setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            newListOfTransitionsPopOver.hide();
        });
    }


    public void addReflexiveTransitionToDiagramView(TransitionModel newTransitionModel) {
        StateView sourceCell = stateMap.get(newTransitionModel.getCurrentStateModel());
        VBox newListOfTransitionsVBox = new VBox();
        for (TransitionModel transitionModel : machineModel.getRelatedTransitions(newTransitionModel)) {
            Label newLabel = new Label(transitionModel.toString());
            newListOfTransitionsVBox.getChildren().add(newLabel);
        }

        PopOver newListOfTransitionsPopOver = new PopOver(newListOfTransitionsVBox);
        sourceCell.getReflexiveArrowShaftArc().setOnMouseEntered(mouseEvent -> {
            newListOfTransitionsPopOver.show(sourceCell.getReflexiveArrowShaftArc());
        });

        sourceCell.getReflexiveArrowShaftArc().setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            newListOfTransitionsPopOver.hide();
        });
        sourceCell.getReflexiveArrowShaftArc().setVisible(true);
        sourceCell.getReflexiveArrowTipPolygon().setVisible(true);
        sourceCell.setListOfTransitionsVBox(newListOfTransitionsVBox);
    }


    public void deleteStateViewOnDiagramView(StateModel stateModelToDelete, HashSet<TransitionModel> exitingTransitionModelsSetToDelete, HashSet<TransitionModel> enteringTransitionModelsSetToDelete) {
        //Retrieve stateview to be deleted
        StateView stateViewToRemove = stateMap.get(stateModelToDelete);
        //Retrieve and remove linked transitionviews to stateview
        deleteTransitionView(exitingTransitionModelsSetToDelete);
        deleteTransitionView(enteringTransitionModelsSetToDelete);

        // Remove mapping of stateview in data structures
        stateMap.remove(stateModelToDelete);
        linkedTransitionViewsMap.remove(stateViewToRemove);

        //Remove the stateview from the diagramview
        diagramView.getChildren().remove(stateViewToRemove);
    }


    public void deleteTransitionView(HashSet<TransitionModel> deletedTransitionModelsSet) {

        HashSet<HashSet<Node>> transitionViewNodesToRemoveSet = new HashSet<>();
        HashSet<StateView> stateViewsWithTransitionToRemoveSet = new HashSet<>();

        for (TransitionModel deletedTransition : deletedTransitionModelsSet) {
            String currentStateModelID = deletedTransition.getCurrentStateModel().getStateId();
            String resultingStateModelID = deletedTransition.getResultingStateModel().getStateId();
            StateView currentStateView = stateMap.get(deletedTransition.getCurrentStateModel());

            //Check type of transition
            if (currentStateModelID.equals(resultingStateModelID)) {
                VBox listOfTransitionsVBox = currentStateView.getListOfTransitionsVBox();
                Iterator<Node> iter = listOfTransitionsVBox.getChildren().iterator();
                while (iter.hasNext()) {
                    Label labelToRemove = (Label) iter.next();
                    if (labelToRemove.getText().equals(deletedTransition.toString())) {
                        iter.remove();
                    }
                }
                if (listOfTransitionsVBox.getChildren().isEmpty()) {
                    currentStateView.getReflexiveArrowShaftArc().setVisible(false);
                    currentStateView.getReflexiveArrowTipPolygon().setVisible(false);
                }
            } else {
                //Find bi-directional transition view
                HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateView);

                for (HashSet<Node> nextHashSet : linkedTransitionViews) {
                    for (Node node : nextHashSet) {
                        if (node instanceof TransitionView) {
                            TransitionView transitionViewToUpdate = (TransitionView) node;
                            if (transitionViewToUpdate.getSource().getStateID().equals(currentStateModelID) && transitionViewToUpdate.getTarget().getStateID().equals(resultingStateModelID)) {
                                if (machineModel.getRelatedTransitions(deletedTransition).isEmpty()) {
                                    transitionViewNodesToRemoveSet.add(nextHashSet);
                                    stateViewsWithTransitionToRemoveSet.add(currentStateView);
                                }
                                createNewListOfTransitionsPopOver(transitionViewToUpdate, machineModel.getRelatedTransitions(deletedTransition));
                            }
                        }
                    }
                }
            }
        }
        if (!(transitionViewNodesToRemoveSet.isEmpty())) {
            // Get affect key i.e. ControlState view to access map
            for (StateView stateView : stateViewsWithTransitionToRemoveSet) {
                // Retrieve all transitionview linked to stateview
                Iterator<HashSet<Node>> iter = linkedTransitionViewsMap.get(stateView).iterator();
                while (iter.hasNext()) {
                    HashSet<Node> nextHashSet = iter.next();
                    for (HashSet<Node> nodeSetToRemove : transitionViewNodesToRemoveSet) {
                        //Find the hashset containing the components for the transition view in the map = the hashset to remove
                        if (nextHashSet == nodeSetToRemove) {
                            for (Node node : nextHashSet) {
                                diagramView.getChildren().remove(node);
                            }
                            iter.remove();
                        }
                    }
                }
            }
        }
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

        // Updating state model coordinates
        StateModel stateModelDragged = machineModel.getStateModelFromStateModelSet(stateView.getStateID());
        stateModelDragged.setxCoordinateOnDiagram(layoutX + stateView.getTranslateX());
        stateModelDragged.setyCoordinateOnDiagram(layoutY + stateView.getTranslateY());

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

            stateView.getStartStatePointLine1().setVisible(false);
            stateView.getStartStatePointLine2().setVisible(false);
            stateView.getFinalStateArc().setVisible(false);
        });

        toggleStartStateItem.setOnAction(e -> {
            if (stateModelSelected.isStartState()) {
                stateModelSelected.setStartState(false);
                stateView.getStartStatePointLine1().setVisible(stateModelSelected.isStartState());
                stateView.getStartStatePointLine2().setVisible(stateModelSelected.isStartState());
            } else {
                if (machineModel.findStartStateModel() != null) { // Check to see if start state exists in machine
                    Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                            "Only one initial state allowed per machine. " + "State " + machineModel.findStartStateModel() + " is currently defined as the initial state for this machine.", ButtonType.OK);
                    invalidActionAlert.setHeaderText("Information");
                    invalidActionAlert.setTitle("Invalid Action");
                    invalidActionAlert.show();
                } else {
                    stateModelSelected.setStartState(true);
                    stateView.getStartStatePointLine1().setVisible(stateModelSelected.isStartState());
                    stateView.getStartStatePointLine2().setVisible(stateModelSelected.isStartState());
                }
            }
        });
        toggleFinalStateItem.setOnAction(e -> {
            if (stateModelSelected.isFinalState()) {
                stateModelSelected.setFinalState(false);
                stateView.getFinalStateArc().setVisible(stateModelSelected.isFinalState());
            } else {
                stateModelSelected.setFinalState(true);
                stateView.getFinalStateArc().setVisible(stateModelSelected.isFinalState());
            }
        });


        //TODO need to remove this listeners logic
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
            stage.initOwner(mainStageController.getPrimaryWindow());
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
                    transitionTableController.updateAvailableStateListForCombobox();
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
                    addReflexiveTransitionToDiagramView(newTransitionModel);
                } else {
                    addDirectionalTransitionToView(newTransitionModel);
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

            //Update view
            deleteStateViewOnDiagramView(stateModelSelected, exitingTranstionsFromStateModel, enteringTranstionsFromStateModel);
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


    public void highlightTransitionTakenInDiagram(ConfigurationModel selectedConfiguration) {
        TransitionModel transitionModelToHighlight = selectedConfiguration.getTransitionModelTakenToReachCurrentConfiguration();

        if (transitionModelToHighlight == null) {
            this.startStateView = stateMap.get(selectedConfiguration.getCurrentStateModel());
            startStateView.getStateCircle().setStroke(Color.LAWNGREEN);
            if (transitionModelHighlighted != null) {
                removeHighlightedTransitionView(transitionModelHighlighted);
                //TODO: FIX THIS LOGIC CURRENT IMP IS QUICK FIX
                startStateView.getStateCircle().setStroke(Color.LAWNGREEN);
            }
        } else {
            if (startStateView != null) {
                startStateView.getStateCircle().setStroke(Color.BLACK);
            }
            if (transitionModelHighlighted != null) {
                removeHighlightedTransitionView(transitionModelHighlighted);
            }
            transitionModelHighlighted = transitionModelToHighlight;

            StateView currentStateView = stateMap.get(transitionModelToHighlight.getCurrentStateModel());
            StateView resultingStateView = stateMap.get(transitionModelToHighlight.getResultingStateModel());
            currentStateView.getStateCircle().setStroke(Color.LAWNGREEN);
            resultingStateView.getStateCircle().setStroke(Color.LAWNGREEN);

            if (transitionModelToHighlight.getCurrentStateModel().equals(transitionModelToHighlight.getResultingStateModel())) {
                currentStateView.getReflexiveArrowShaftArc().setStroke(Color.LAWNGREEN);
                currentStateView.getReflexiveArrowTipPolygon().setFill(Color.LAWNGREEN);
                currentStateView.getReflexiveArrowTipPolygon().setStroke(Color.LAWNGREEN);
            } else {
                HashSet<Node> transitionViewHighlightedSet = retrieveDirectionalTransitionView(transitionModelToHighlight);

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
    }


    public void removeHighlightedTransitionView(TransitionModel transitionModelToRemoveHightlight) {

        StateView currentStateView = stateMap.get(transitionModelToRemoveHightlight.getCurrentStateModel());
        StateView resultingStateView = stateMap.get(transitionModelToRemoveHightlight.getResultingStateModel());
        currentStateView.getStateCircle().setStroke(Color.BLACK);

        //TODO: Figure out why you have to null check for this
        if (resultingStateView != null) {
            resultingStateView.getStateCircle().setStroke(Color.BLACK);
        }
        if (transitionModelToRemoveHightlight.getCurrentStateModel().equals(transitionModelToRemoveHightlight.getResultingStateModel())) {
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

        StateView currentStateView = stateMap.get(transitionModel.getCurrentStateModel());
        StateView resultingStateView = stateMap.get(transitionModel.getResultingStateModel());

        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateView);
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