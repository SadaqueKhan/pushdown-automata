package app.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TransitionView extends Line {

    private StateView source;
    private StateView target;

    public TransitionView(StateView source, StateView target) {
        this.source = source;
        this.target = target;
        setUpUIComponents();
    }

    private void setUpUIComponents() {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
    }


    public StateView getSource() {
        return source;
    }


    public StateView getTarget() {
        return target;
    }
}