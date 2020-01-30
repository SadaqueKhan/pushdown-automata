package app.views;

//Complete

import app.controllers.StateController;
import app.listeners.StateListener;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class StateView extends Circle {

    private static int num = 0;

    private String stateId;
    private final StateController stateController;
    private double centerX;

    private List<StateView> children = new ArrayList<>();
    private List<StateView> parents = new ArrayList<>();
    private double centerY;


    //Popup component
    private ContextMenu contextMenu;
    private MenuItem createTransition;
    private MenuItem deleteItem;

    public StateView(double x, double y, StateController stateController) {
        this.centerX = x;
        this.centerY = y;

        this.stateId = "Q" + (num++);

        // Reference to the controller of this view
        this.stateController = stateController;

        //Set up the components to represent the state in the view
        setUpComponents();
        setUpListeners();
    }


    private void setUpComponents() {
        this.setCenterX(centerX);
        this.setCenterY(centerY);
        this.setRadius(40);
        this.setStroke(Color.GREEN);
        this.setFill(Color.RED);

    }

    private void setUpListeners() {

        //Create listener for this view
        StateListener stateListener = new StateListener(stateController);

        //Link listeners to events
        this.setOnMousePressed(stateListener);
        this.setOnMouseDragged(stateListener);

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
}

