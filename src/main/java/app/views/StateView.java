package app.views;

//Complete

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class StateView extends Pane {

    private String stateId;

    private List<StateView> children = new ArrayList<>();
    private List<StateView> parents = new ArrayList<>();

    private Circle state;

    public StateView(String stateId) {
        this.stateId = stateId;
        //Set up the components to represent the state in the view
        setUpComponents();
        setUpListeners();
    }


    private void setUpComponents() {

        this.state = new Circle();
        state.setCenterX(100);
        state.setCenterY(100);
        state.setRadius(40);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        getChildren().add(state);

    }

    private void setUpListeners() {


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

