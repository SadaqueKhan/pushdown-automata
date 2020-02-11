package app.views;

import app.controllers.DiagramController;
import app.models.TransitionModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Point2D;
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
import java.util.Map;

public class DiagramView extends Pane {

    //Reference to other stuff external files
    private final MainStageView mainStageView;

    private final DiagramController diagramController;

    private Map<String, StateView> stateMap;

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
        stateMap = new HashMap<>(); //
        this.setStyle(cssLayout);
        this.setMinSize(200, 500);
        loadToMainStage();
    }


    public void addStateView(double x, double y, DiagramController diagramController, String stateID) {
        StateView stateView = new StateView(x, y, diagramController, stateID);
        this.getChildren().add(stateView);
        stateMap.put(stateID, stateView);
    }


    public void addDirectionalTransitionView(String sourceID, String targetID, HashSet<TransitionModel> transitionsLinkingToResultingStateSet) {
        //Get state from map using state ID
        StateView sourceCell = stateMap.get(sourceID);
        StateView targetCell = stateMap.get(targetID);

        Line virtualCenterLine = getLine(sourceCell, targetCell);
        virtualCenterLine.setOpacity(0);
        this.getChildren().remove(sourceCell);
        this.getChildren().remove(targetCell);

        StackPane centerLineArrowAB = getArrowTip(true, virtualCenterLine, sourceCell, targetCell);
        centerLineArrowAB.setOpacity(0);
        StackPane centerLineArrowBA = getArrowTip(false, virtualCenterLine, sourceCell, targetCell);
        centerLineArrowBA.setOpacity(0);

        Line directedLine = new Line();
        directedLine.setStroke(Color.BLACK);
        directedLine.setStrokeWidth(2);


        //Create popover to list applicable transitions for given transition
        VBox vBox = new VBox();
        for (TransitionModel transitionModel : transitionsLinkingToResultingStateSet) {
            Label newLabel = new Label(transitionModel.toString());
            vBox.getChildren().add(newLabel);
        }

        PopOver listOfTransitionsPopOver = new PopOver(vBox);

        directedLine.setOnMouseEntered(mouseEvent -> {
            listOfTransitionsPopOver.show(directedLine);
        });

        directedLine.setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            listOfTransitionsPopOver.hide();
        });


        double diff = true ? -centerLineArrowAB.getPrefWidth() / 2 : centerLineArrowAB.getPrefWidth() / 2;
        final ChangeListener<Number> listener = (obs, old, newVal) -> {
            Rotate r = new Rotate();
            r.setPivotX(virtualCenterLine.getStartX());
            r.setPivotY(virtualCenterLine.getStartY());
            r.setAngle(centerLineArrowAB.getRotate());
            Point2D point = r.transform(new Point2D(virtualCenterLine.getStartX(), virtualCenterLine.getStartY() + diff));
            directedLine.setStartX(point.getX());
            directedLine.setStartY(point.getY());

            Rotate r2 = new Rotate();
            r2.setPivotX(virtualCenterLine.getEndX());
            r2.setPivotY(virtualCenterLine.getEndY());
            r2.setAngle(centerLineArrowBA.getRotate());
            Point2D point2 = r2.transform(new Point2D(virtualCenterLine.getEndX(), virtualCenterLine.getEndY() - diff));
            directedLine.setEndX(point2.getX());
            directedLine.setEndY(point2.getY());
        };
        centerLineArrowAB.rotateProperty().addListener(listener);
        centerLineArrowBA.rotateProperty().addListener(listener);
        virtualCenterLine.startXProperty().addListener(listener);
        virtualCenterLine.startYProperty().addListener(listener);
        virtualCenterLine.endXProperty().addListener(listener);
        virtualCenterLine.endYProperty().addListener(listener);

        StackPane mainArrow = getArrowTip(true, directedLine, sourceCell, targetCell);
        this.getChildren().addAll(virtualCenterLine, centerLineArrowAB, centerLineArrowBA, directedLine, mainArrow);
        this.getChildren().addAll(sourceCell, targetCell);
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


}