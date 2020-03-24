package app.view;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TransitionNode extends Line {

    private StateNode source;
    private StateNode target;
    private VBox transitionListVBox;

    public TransitionNode(StateNode source, StateNode target) {
        this.source = source;
        this.target = target;
        setUpUIComponents();
    }

    private void setUpUIComponents() {
        transitionListVBox = new VBox();
        transitionListVBox.setStyle("-fx-background-color:#ffffff;-fx-border-width:2px;-fx-border-color:black;");

        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
    }


    public StateNode getSource() {
        return source;
    }


    public StateNode getTarget() {
        return target;
    }

    public VBox getTransitionListVBox() {
        return transitionListVBox;
    }
}