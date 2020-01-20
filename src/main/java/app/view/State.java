package app.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class State extends Pane {

    private String stateId;

    private List<State> children = new ArrayList<>();
    private List<State> parents = new ArrayList<>();

    private Node view;

    public State(String stateId) {

        this.stateId = stateId;
        Circle state = new Circle();
        state.setCenterX(100);
        state.setCenterY(100);
        state.setRadius(40);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        setView(state);

    }


    public void addStateChild(State state) {
        children.add(state);
    }

    public List<State> getStateChildren() {
        return children;
    }

    public void addStateParent(State state) {
        parents.add(state);
    }

    public List<State> getStateParents() {
        return parents;
    }

    public void removeStateChild(State state) {
        children.remove(state);
    }

    public Node getView() {
        return this.view;
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public String getStateId() {
        return stateId;
    }
}

