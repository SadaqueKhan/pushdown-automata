package app.views;

//Complete

import app.controllers.DiagramController;
import app.listeners.DiagramListener;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class StateView extends Group {

    private String stateId;
    private final DiagramController diagramController;
    private double centerX;
    private double centerY;

    private List<StateView> children = new ArrayList<>();
    private List<StateView> parents = new ArrayList<>();
    private Circle state;


    public StateView(double x, double y, DiagramController diagramController, String stateId) {
        this.centerX = x;
        this.centerY = y;

        this.stateId = stateId;

        // Reference to the controller of this view
        this.diagramController = diagramController;

        //Set up the components to represent the state in the view

        setUpStandardStateUIComponents();
        setUpUIListeners();
    }


    public void setUpStandardStateUIComponents() {

        //State GUI
        this.state = new Circle();
        state.setCenterX(centerX);
        state.setCenterY(centerY);
        state.setRadius(40);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        // Text for state GUI
        Text stateIdText = new Text(stateId);
        stateIdText.relocate(centerX - 6, centerY - 6);

        this.getChildren().addAll(state, stateIdText);
    }

    public void setUpStartStateUIComponent() {

        //Create arrow shaft using line object
        Line startStatePointLine1 = new Line();
        startStatePointLine1.setStrokeWidth(3);

        // instantiating the Rotate class.
        Rotate rotate1 = new Rotate();
        //setting properties for the rotate object.
        rotate1.setAngle(180);
        startStatePointLine1.getTransforms().add(rotate1);
        startStatePointLine1.setStartX(0);
        startStatePointLine1.setStartY(0);
        startStatePointLine1.setEndX(15);
        startStatePointLine1.setEndY(15);

        startStatePointLine1.relocate(centerX - 40, centerY);


        //Create arrow shaft using line object
        Line startStatePointLine2 = new Line();
        startStatePointLine2.setStrokeWidth(3);

        startStatePointLine2.setStroke(Color.GREEN);
        // instantiating the Rotate class.
        Rotate rotate2 = new Rotate();

        //setting properties for the rotate object.
        rotate2.setAngle(90);
        startStatePointLine2.getTransforms().add(rotate2);
        startStatePointLine2.setStartX(0);
        startStatePointLine2.setStartY(0);
        startStatePointLine2.setEndX(15);
        startStatePointLine2.setEndY(15);

        startStatePointLine2.relocate(centerX - 40, centerY);


        this.getChildren().addAll(startStatePointLine1, startStatePointLine2);
    }


    public void setUpFinalStateUIComponent() {

        Arc arc = new Arc(centerX, centerY, 30, 30, 0, 360);

        arc.setType(ArcType.OPEN);
        arc.setStrokeWidth(3);
        arc.setStroke(Color.BLACK);
        arc.setStrokeType(StrokeType.INSIDE);
        arc.setFill(null);

        this.getChildren().add(arc);
    }


    private void setUpUIListeners() {

        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);

        //Link listeners to events
        this.setOnMousePressed(diagramListener);
        this.setOnMouseDragged(diagramListener);

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

    public String getStateId() {
        return stateId;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }
}

