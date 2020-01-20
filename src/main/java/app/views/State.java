package app.views;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;

public class State extends Pane {

    private String stateId;

    private List<State> children = new ArrayList<>();
    private List<State> parents = new ArrayList<>();

    private Circle state;

    public State(String stateId) {
        this.stateId = stateId;
        //Set up the components to represent the state in the view
        setUpComponents(stateId);
    }

    private void setUpComponents(String stateId) {


        Text text = new Text(stateId);
        text.setBoundsType(TextBoundsType.VISUAL);


        this.state = new Circle();
        state.setCenterX(100);
        state.setCenterY(100);
        state.setRadius(30);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        StackPane stack = new StackPane();
        stack.getChildren().addAll(state, text);

        getChildren().add(stack);

    }

    // TODO Remove these getters/setters from the view and break it down into MVC
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

    public String getStateId() {
        return stateId;
    }
}

