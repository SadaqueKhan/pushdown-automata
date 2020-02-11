package app.views;

import app.controllers.DiagramController;
import app.listeners.DiagramListener;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class StateView extends StackPane {

    private final DiagramController diagramController;

    private String stateId;
    private double currentStateXPosition;
    private double currentStateYPosition;

    private List<StateView> children = new ArrayList<>();
    private List<StateView> parents = new ArrayList<>();

    // GUI Components
    private Circle stateCircle;
    private Text stateIdText;
    private Line startStatePointLine1;
    private Line startStatePointLine2;
    private Arc finalStateArc;

    public StateView(double currentStateXPosition, double currentStateYPosition, DiagramController diagramController, String stateId) {
        this.currentStateXPosition = currentStateXPosition;
        this.currentStateYPosition = currentStateYPosition;

        this.stateId = stateId;

        // Reference to the controller of this view
        this.diagramController = diagramController;

        //Set up the components to represent the stateCircle in the view
        setUpUIComponents();
        setUpUIListeners();
    }


    public void setUpUIComponents() {

        double radius = 25;
        double paneSize = 2 * radius;


//<<< CREATE STANDARD STATE UI COMPONENT >>>
        //State GUI
        this.stateCircle = new Circle();


        stateCircle.setRadius(radius);
        stateCircle.setStyle("-fx-fill:" + "orange" + ";-fx-stroke-width:2px;-fx-stroke:black;");

        Label stateIdText = new Label(stateId);
        stateIdText.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        this.setPrefSize(paneSize, paneSize);
        this.setMaxSize(paneSize, paneSize);
        this.setMinSize(paneSize, paneSize);

//<<< CREATE START STATE UI COMPONENT >>>
        //Create arrow shaft using line object
        this.startStatePointLine1 = new Line();
        startStatePointLine1.setStrokeWidth(2);

        // instantiating the Rotate class.
        Rotate rotate1 = new Rotate();
        //setting properties for the rotate object.
        rotate1.setAngle(90);

        startStatePointLine1.setStartX(0);
        startStatePointLine1.setStartY(0);
        startStatePointLine1.setEndX(10);
        startStatePointLine1.setEndY(10);
        startStatePointLine1.getTransforms().add(rotate1);
        // startStatePointLine1.relocate(currentStateXPosition - 40, currentStateYPosition);

        startStatePointLine1.setTranslateX(-21);
        startStatePointLine1.setTranslateY(5);

        //Create arrow shaft using line object
        this.startStatePointLine2 = new Line();
        startStatePointLine2.setStrokeWidth(2);

        // instantiating the Rotate class.
        Rotate rotate2 = new Rotate();
        //setting properties for the rotate object.
        rotate2.setAngle(180);

        startStatePointLine2.setStartX(0);
        startStatePointLine2.setStartY(0);
        startStatePointLine2.setEndX(10);
        startStatePointLine2.setEndY(10);
        startStatePointLine2.getTransforms().add(rotate2);

        startStatePointLine2.setTranslateX(-21);
        startStatePointLine2.setTranslateY(5);
        //  startStatePointLine2.relocate(currentStateXPosition - 40, currentStateYPosition);

        startStatePointLine1.setVisible(false);
        startStatePointLine2.setVisible(false);


//<<< CREATE FINAL STATE UI COMPONENT >>>
        this.finalStateArc = new Arc(0, 0, radius / 1.25, radius / 1.25, 0, 360);

        finalStateArc.setType(ArcType.OPEN);
        finalStateArc.setStrokeWidth(2);
        finalStateArc.setStroke(Color.BLACK);
        finalStateArc.setStrokeType(StrokeType.INSIDE);
        finalStateArc.setFill(null);

        finalStateArc.setVisible(false);


        Group group = new Group(arc, );

        Arc arc = new Arc();

        arc.setCenterX(0);
        arc.setCenterY(25);

        arc.setRadiusX(25);
        arc.setRadiusY(25);

        arc.setStartAngle(340);
        arc.setLength(220);

        arc.setType(ArcType.OPEN);
        arc.setStrokeWidth(3);
        arc.setStroke(Color.BLACK);
        arc.setStrokeType(StrokeType.INSIDE);
        arc.setFill(null);

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(0.0, 0.0,
                20.0, 10.0,
                10.0, 20.0);


        arc.setTranslateX(0);
        arc.setTranslateY(-28);

        this.relocate(currentStateXPosition, currentStateYPosition);
        this.getChildren().addAll(stateCircle, stateIdText, finalStateArc, startStatePointLine1, startStatePointLine2, arc);
    }

    public void toggleStandardStateUIComponent(boolean isStandardStateVisible) {
        startStatePointLine1.setVisible(isStandardStateVisible);
        startStatePointLine2.setVisible(isStandardStateVisible);
        finalStateArc.setVisible(isStandardStateVisible);
    }


    public void toggleStartStateUIComponent(boolean isStartStateVisible) {
        startStatePointLine1.setVisible(isStartStateVisible);
        startStatePointLine2.setVisible(isStartStateVisible);
    }


    public void toggleFinalStateUIComponent(boolean isFinalStateVisible) {
        finalStateArc.setVisible(isFinalStateVisible);
    }


    private void setUpUIListeners() {

        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);

        //Link listeners to events
        this.setOnMousePressed(diagramListener);
        this.setOnMouseDragged(diagramListener);

    }

    public String getStateId() {
        return stateId;
    }

    public double getCurrentStateXPosition() {
        return currentStateXPosition;
    }

    public void setCurrentStateXPosition(double currentStateXPosition) {
        this.currentStateXPosition = currentStateXPosition;
    }

    public double getCurrentStateYPosition() {
        return currentStateYPosition;
    }

    public void setCurrentStateYPosition(double currentStateYPosition) {
        this.currentStateYPosition = currentStateYPosition;
    }


    // TODO Remove these getters/setters from the view and break it down into MVC
    public void addStateChild(StateView stateView) {
        children.add(stateView);
    }

    public List<StateView> getStateChildren() {
        return children;
    }

    public void addStateParent(StateView stateView) {
        parents.add(stateView);
    }

    public List<StateView> getStateParents() {
        return parents;
    }

    public void removeStateChild(StateView stateView) {
        children.remove(stateView);
    }


}