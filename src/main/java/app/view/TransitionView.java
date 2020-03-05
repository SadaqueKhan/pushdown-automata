package app.view;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TransitionView extends Line {

    private StateView source;
    private StateView target;
    private VBox transitionListVBox;

    public TransitionView(StateView source, StateView target) {
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


    public StateView getSource() {
        return source;
    }


    public StateView getTarget() {
        return target;
    }

    public VBox getTransitionListVBox() {
        return transitionListVBox;
    }
}