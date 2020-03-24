package app.view;
import app.listener.DiagramListener;
import app.presenter.DiagramScenePresenter;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for a state node.
 * </p>
 */
public class StateNode extends StackPane {
    //Reference to diagram presenter.
    private final DiagramScenePresenter diagramScenePresenter;
    //Reference data linked to view.
    private String stateID;
    //Reference to UI components for view.
    private Circle stateCircle;
    private Line startStatePointLine1;
    private Line startStatePointLine2;
    private Arc finalStateArc;
    private Arc reflexiveArrowShaftArc;
    private Polygon reflexiveArrowTipPolygon;
    private VBox listOfTransitionsVBox;
    /**
     * Constructor of the state node, used to instantiate an instance of the view.
     * @param stateID
     * @param diagramScenePresenter
     */
    public StateNode(String stateID, DiagramScenePresenter diagramScenePresenter) {
        this.stateID = stateID;
        this.diagramScenePresenter = diagramScenePresenter;
        setUpUIComponents();
        setUpUIListeners();
    }
    /**
     * Sets up the UI components of the view.
     */
    public void setUpUIComponents() {
        double radius = 25;
        double paneSize = 2 * radius;
        //Create standard state UI components.
        this.stateCircle = new Circle();
        stateCircle.setRadius(radius);
        stateCircle.setStyle("-fx-fill:orange;-fx-stroke-width:2px;-fx-stroke:black;");
        Label stateIdText = new Label(stateID);
        stateIdText.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        this.setPrefSize(paneSize, paneSize);
        this.setMaxSize(paneSize, paneSize);
        this.setMinSize(paneSize, paneSize);
        //Create start state UI components.
        this.startStatePointLine1 = new Line();
        startStatePointLine1.setStrokeWidth(2);
        Rotate rotate1 = new Rotate();
        rotate1.setAngle(90);
        startStatePointLine1.setStartX(0);
        startStatePointLine1.setStartY(0);
        startStatePointLine1.setEndX(10);
        startStatePointLine1.setEndY(10);
        startStatePointLine1.getTransforms().add(rotate1);
        startStatePointLine1.setTranslateX(-21);
        startStatePointLine1.setTranslateY(5);
        this.startStatePointLine2 = new Line();
        startStatePointLine2.setStrokeWidth(2);
        Rotate rotate2 = new Rotate();
        rotate2.setAngle(180);
        startStatePointLine2.setStartX(0);
        startStatePointLine2.setStartY(0);
        startStatePointLine2.setEndX(10);
        startStatePointLine2.setEndY(10);
        startStatePointLine2.getTransforms().add(rotate2);
        startStatePointLine2.setTranslateX(-21);
        startStatePointLine2.setTranslateY(5);
        startStatePointLine1.setVisible(false);
        startStatePointLine2.setVisible(false);
        //Create final state UI components.
        this.finalStateArc = new Arc(0, 0, radius / 1.25, radius / 1.25, 0, 360);
        finalStateArc.setType(ArcType.OPEN);
        finalStateArc.setStrokeWidth(2);
        finalStateArc.setStroke(Color.BLACK);
        finalStateArc.setStrokeType(StrokeType.INSIDE);
        finalStateArc.setFill(null);
        finalStateArc.setVisible(false);
        //Create reflexive arrow component
        reflexiveArrowShaftArc = new Arc();
        reflexiveArrowShaftArc.setCenterX(0);
        reflexiveArrowShaftArc.setCenterY(-50);
        reflexiveArrowShaftArc.setRadiusX(25);
        reflexiveArrowShaftArc.setRadiusY(25);
        reflexiveArrowShaftArc.setStartAngle(340);
        reflexiveArrowShaftArc.setLength(220);
        reflexiveArrowShaftArc.setType(ArcType.OPEN);
        reflexiveArrowShaftArc.setStrokeWidth(2);
        reflexiveArrowShaftArc.setStroke(Color.BLACK);
        reflexiveArrowShaftArc.setStrokeType(StrokeType.INSIDE);
        reflexiveArrowShaftArc.setFill(null);
        reflexiveArrowShaftArc.setTranslateX(0);
        reflexiveArrowShaftArc.setTranslateY(-28);
        reflexiveArrowTipPolygon = new Polygon(4, 0, 8, 8, 0, 8);
        reflexiveArrowTipPolygon.setFill(Color.BLACK);
        reflexiveArrowTipPolygon.setStroke(Color.BLACK);
        reflexiveArrowTipPolygon.setStrokeWidth(2);
        reflexiveArrowTipPolygon.setRotate(65);
        reflexiveArrowTipPolygon.setTranslateX(25);
        reflexiveArrowTipPolygon.setTranslateY(-20);
        reflexiveArrowShaftArc.setVisible(false);
        reflexiveArrowTipPolygon.setVisible(false);
        this.getChildren().addAll(stateCircle, stateIdText, finalStateArc, startStatePointLine1, startStatePointLine2, reflexiveArrowShaftArc, reflexiveArrowTipPolygon);
    }
    /**
     * Sets up the listeners for UI components of the view.
     */
    private void setUpUIListeners() {
        //Create listener for UI component for this view.
        DiagramListener diagramListener = new DiagramListener(diagramScenePresenter);
        //Link listener to events on UI components for this view.
        this.setOnMousePressed(diagramListener);
        this.setOnMouseDragged(diagramListener);
        this.setOnMouseReleased(diagramListener);
    }
    //Getters for data linked to this view.
    public String getStateID() {
        return stateID;
    }
    // Getters for UI components of this view.
    public Arc getReflexiveArrowShaftArc() {
        return reflexiveArrowShaftArc;
    }
    public Polygon getReflexiveArrowTipPolygon() {
        return reflexiveArrowTipPolygon;
    }
    public Circle getStateCircle() {
        return stateCircle;
    }
    public Line getStartStatePointLine1() {
        return startStatePointLine1;
    }
    public Line getStartStatePointLine2() {
        return startStatePointLine2;
    }
    public Arc getFinalStateArc() {
        return finalStateArc;
    }
    public VBox getListOfTransitionsVBox() {
        return listOfTransitionsVBox;
    }
    public void setListOfTransitionsVBox(VBox listOfTransitionsVBox) {
        this.listOfTransitionsVBox = listOfTransitionsVBox;
    }
}