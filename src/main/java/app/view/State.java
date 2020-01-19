package app.view;

import app.model.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class State extends Cell {

    public State(String id) {
        super(id);

        Circle state = new Circle();
        state.setCenterX(100);
        state.setCenterY(100);
        state.setRadius(50);
        state.setStroke(Color.GREEN);
        state.setFill(Color.RED);

        setView(state);

    }

}