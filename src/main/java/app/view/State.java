package app.view;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class State extends Pane {

    String cellId;

    List<State> children = new ArrayList<>();
    List<State> parents = new ArrayList<>();

    Node view;

    public State(String cellId) {

        this.cellId = cellId;
        Circle state = new Circle();
        state.setCenterX(100);
        state.setCenterY(100);
        state.setRadius(50);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        setView(state);

    }


    public void addCellChild(State state) {
        children.add(state);
    }

    public List<State> getCellChildren() {
        return children;
    }

    public void addCellParent(State state) {
        parents.add(state);
    }

    public List<State> getCellParents() {
        return parents;
    }

    public void removeCellChild(State state) {
        children.remove(state);
    }

    public Node getView() {
        return this.view;
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public String getCellId() {
        return cellId;
    }
}

