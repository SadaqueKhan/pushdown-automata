package app.view;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {

    protected State source;
    protected State target;

    Line line;

    public Edge(State source, State target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();


        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight() / 2.0));

        getChildren().add(line);

    }

    public State getSource() {
        return source;
    }

    public State getTarget() {
        return target;
    }

}