package app.views;

import app.controllers.DiagramController;
import app.models.StateModel;
import app.models.TransitionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import org.controlsfx.control.PopOver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class DiagramView extends Pane {

    //Reference to other stuff external files
    private final MainStageView mainStageView;

    private final DiagramController diagramController;

    private Map<String, StateView> stateMap;

    private Map<StateView, HashSet<HashSet<Node>>> linkedTransitionViewsMap;

    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-background-color: whitesmoke,\n" +
            "linear-gradient(from 0.5px 0.0px to 10.5px  0.0px, repeat, black 5%, transparent 5%),\n" +
            "linear-gradient(from 0.0px 0.5px to  0.0px 10.5px, repeat, black 5%, transparent 5%)";

    public DiagramView(DiagramController diagramController, MainStageView mainStageView) {
        // Reference to the main application container
        this.mainStageView = mainStageView;
        // Reference to diagram controller
        this.diagramController = diagramController;
        setUpUIComponents();
    }

    public void loadToMainStage() {
        mainStageView.getContainerForCenterNodes().getChildren().add(this);
    }


    private void setUpUIComponents() {
        stateMap = new HashMap<>();
        linkedTransitionViewsMap = new HashMap<>();//
        this.setStyle(cssLayout);
        this.setMinSize(200, 500);
        loadToMainStage();
    }


    public void addStateView(double x, double y, DiagramController diagramController, String stateID) {
        StateView stateView = new StateView(x, y, diagramController, stateID);
        this.getChildren().add(stateView);
        stateMap.put(stateID, stateView);

        HashSet<HashSet<Node>> linkedTransitionViews = new HashSet<>();
        linkedTransitionViewsMap.put(stateView, linkedTransitionViews);
    }


    public void addDirectionalTransitionView(String currentStateID, String resultingStateID, HashSet<TransitionModel> transitionsLinkingToResultingStateSet) {
        //Get state from map using state ID
        StateView currentStateView = stateMap.get(currentStateID);
        StateView resultingStateView = stateMap.get(resultingStateID);

        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateView);

        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            for (Node node : nextHashSet) {
                if (node instanceof TransitionView) {
                    TransitionView transitionViewToCheck = (TransitionView) node;
                    if (transitionViewToCheck.getSource() == currentStateView && transitionViewToCheck.getTarget() == resultingStateView) {
                        createNewListOfTransitionsPopOver(transitionViewToCheck, transitionsLinkingToResultingStateSet);
                        return;
                    }
                }
            }
        }

        //Transition does not exist create fresh transition
        Line virtualCenterLine = getLine(currentStateView, resultingStateView);
        virtualCenterLine.setOpacity(0);
        this.getChildren().remove(currentStateView);
        this.getChildren().remove(resultingStateView);

        StackPane centerLineArrowAB = getArrowTip(true, virtualCenterLine, currentStateView, resultingStateView);
        centerLineArrowAB.setOpacity(0);
        StackPane centerLineArrowBA = getArrowTip(false, virtualCenterLine, currentStateView, resultingStateView);
        centerLineArrowBA.setOpacity(0);

        TransitionView transitionView = new TransitionView(currentStateView, resultingStateView);
        transitionView.setStroke(Color.BLACK);
        transitionView.setStrokeWidth(2);
        createNewListOfTransitionsPopOver(transitionView, transitionsLinkingToResultingStateSet);

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

        StackPane arrowTip = getArrowTip(true, transitionView, currentStateView, resultingStateView);

        HashSet<Node> setOfNode = new HashSet<>();
        setOfNode.add(virtualCenterLine);
        setOfNode.add(centerLineArrowAB);
        setOfNode.add(centerLineArrowBA);
        setOfNode.add(transitionView);
        setOfNode.add(arrowTip);

        linkedTransitionViewsMap.get(currentStateView).add(setOfNode);

        this.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, transitionView, arrowTip);
        this.getChildren().addAll(currentStateView, resultingStateView);
    }

    public void addReflexiveTransitionView(String sourceStateID, String targetStateID, HashSet<TransitionModel> transitionsLinkingToResultingStateSet) {
        StateView sourceCell = stateMap.get(sourceStateID);
        sourceCell.toggleReflexiveArrowUIComponent(true, transitionsLinkingToResultingStateSet);
    }

    private StackPane getArrowTip(boolean toLineEnd, Line line, StackPane startDot, StackPane endDot) {
        double size = 12; // Arrow size
        StackPane arrow = new StackPane();
        arrow.setStyle("-fx-background-color:black;-fx-border-width:2px;-fx-border-color:black;-fx-shape: \"M0,-4L4,0L0,4Z\"");//
        arrow.setPrefSize(size, size);
        arrow.setMaxSize(size, size);
        arrow.setMinSize(size, size);

        // Determining the arrow visibility unless there is enough space between dots.
        DoubleBinding xDiff = line.endXProperty().subtract(line.startXProperty());
        DoubleBinding yDiff = line.endYProperty().subtract(line.startYProperty());
        BooleanBinding visible = (xDiff.lessThanOrEqualTo(size).and(xDiff.greaterThanOrEqualTo(-size)).and(yDiff.greaterThanOrEqualTo(-size)).and(yDiff.lessThanOrEqualTo(size))).not();
        arrow.visibleProperty().bind(visible);

        // Determining the x point on the line which is at a certain distance.
        DoubleBinding tX = Bindings.createDoubleBinding(() -> {
            double xDiffSqu = (line.getEndX() - line.getStartX()) * (line.getEndX() - line.getStartX());
            double yDiffSqu = (line.getEndY() - line.getStartY()) * (line.getEndY() - line.getStartY());
            double lineLength = Math.sqrt(xDiffSqu + yDiffSqu);
            double dt;
            if (toLineEnd) {
                // When determining the point towards end, the required distance is total length minus (radius + arrow half width)
                dt = lineLength - (endDot.getWidth() / 2) - (arrow.getWidth() / 2);
            } else {
                // When determining the point towards start, the required distance is just (radius + arrow half width)
                dt = (startDot.getWidth() / 2) + (arrow.getWidth() / 2);
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
                dt = lineLength - (endDot.getHeight() / 2) - (arrow.getHeight() / 2);
            } else {
                dt = (startDot.getHeight() / 2) + (arrow.getHeight() / 2);
            }
            double t = dt / lineLength;
            double dy = ((1 - t) * line.getStartY()) + (t * line.getEndY());
            return dy;
        }, line.startXProperty(), line.endXProperty(), line.startYProperty(), line.endYProperty());

        arrow.layoutXProperty().bind(tX.subtract(arrow.widthProperty().divide(2)));
        arrow.layoutYProperty().bind(tY.subtract(arrow.heightProperty().divide(2)));

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
        arrow.rotateProperty().bind(endArrowAngle);

        return arrow;
    }

    private Line getLine(StackPane startDot, StackPane endDot) {
        Line line = new Line();
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(2);
        line.startXProperty().bind(startDot.layoutXProperty().add(startDot.translateXProperty()).add(startDot.widthProperty().divide(2)));
        line.startYProperty().bind(startDot.layoutYProperty().add(startDot.translateYProperty()).add(startDot.heightProperty().divide(2)));
        line.endXProperty().bind(endDot.layoutXProperty().add(endDot.translateXProperty()).add(endDot.widthProperty().divide(2)));
        line.endYProperty().bind(endDot.layoutYProperty().add(endDot.translateYProperty()).add(endDot.heightProperty().divide(2)));
        return line;
    }

    @Override
    public String toString() {
        return "DiagramView";
    }


    public void deleteStateView(String stateID) {
        StateView stateViewToRemove = stateMap.get(stateID);

        HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(stateViewToRemove);

        for (HashSet<Node> nextHashSet : linkedTransitionViews) {
            this.getChildren().removeAll(nextHashSet);
        }

        stateMap.remove(stateID);
        this.getChildren().remove(stateViewToRemove);
    }

    public void deleteTransitionView(HashSet<TransitionModel> changedTransitionModelsSet) {

        //TODO: READ THROUGH CODE UNDERSTAND WHAT IS GOING ON SEQUENTIALLY

        StateView currentStateView = null;
        HashSet<HashSet<Node>> nodesSetsToRemove = new HashSet<>();

        for (TransitionModel changedTransition : changedTransitionModelsSet) {
            String currentStateModelID = changedTransition.getCurrentStateModel().getStateId();
            String resultingStateModelID = changedTransition.getResultingStateModel().getStateId();

            currentStateView = stateMap.get(currentStateModelID);

            //Check type of transition
            if (currentStateModelID.equals(resultingStateModelID)) {
                //Update transition if it is reflexive transition
                currentStateView.removeReflexiveTransition(changedTransition);
            } else {
                //Find bi-directional transition view
                HashSet<HashSet<Node>> linkedTransitionViews = linkedTransitionViewsMap.get(currentStateView);
                for (HashSet<Node> nextHashSet : linkedTransitionViews) {
                    for (Node node : nextHashSet) {
                        if (node instanceof TransitionView) {
                            TransitionView transitionViewToUpdate = (TransitionView) node;
                            if (transitionViewToUpdate.getSource().getStateId().equals(currentStateModelID) && transitionViewToUpdate.getTarget().getStateId().equals(resultingStateModelID)) {
                                if (getUpdatedTransitionsForTransitionView(changedTransition).isEmpty()) {
                                    nodesSetsToRemove.add(nextHashSet);
                                }
                                createNewListOfTransitionsPopOver(transitionViewToUpdate, getUpdatedTransitionsForTransitionView(changedTransition));
                            }
                        }
                    }
                }
            }
        }
        if (currentStateView != null && !(nodesSetsToRemove.isEmpty())) {
            Iterator<HashSet<Node>> iter = linkedTransitionViewsMap.get(currentStateView).iterator();
            while (iter.hasNext()) {
                HashSet<Node> nextHashSet = iter.next();
                for (HashSet<Node> nodeSetToRemove : nodesSetsToRemove) {
                    if (nextHashSet == nodeSetToRemove) {
                        for (Node node : nodeSetToRemove) {
                            this.getChildren().remove(node);
                        }
                        iter.remove();
                    }
                }
            }
        }
    }


    private void createNewListOfTransitionsPopOver(TransitionView transitionViewToCheck, HashSet<TransitionModel> newTransitionModelsAttachedToStateModelSet) {
        VBox newVBox = new VBox();
        PopOver newListOfTransitionsPopOver = new PopOver(newVBox);

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


    public HashSet<TransitionModel> getUpdatedTransitionsForTransitionView(TransitionModel changedTransition) {
        HashSet<TransitionModel> toReturn = new HashSet<>();
        StateModel currentStateModel = changedTransition.getCurrentStateModel();
        StateModel resultingStateModel = changedTransition.getResultingStateModel();
        for (TransitionModel transitionModel : currentStateModel.getExitingTransitionModelsSet()) {
            if (transitionModel.getCurrentStateModel().equals(currentStateModel) && transitionModel.getResultingStateModel().equals(resultingStateModel)) {
                toReturn.add(transitionModel);
            }
        }
        return toReturn;
    }
}