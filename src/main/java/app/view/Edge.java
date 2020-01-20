package app.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

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

        line.setStrokeWidth(3);



        line.startXProperty().bind(source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 2.0));
        line.startYProperty().bind(source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 2.0));

        line.endXProperty().bind(target.layoutXProperty().add(target.getBoundsInParent().getWidth() / 2.0));
        line.endYProperty().bind(target.layoutYProperty().add(target.getBoundsInParent().getHeight() / 2.0));


        Line arrow1 = new Line();
        Line arrow2 = new Line();

        Arrow arrow = new Arrow(line, arrow1, arrow2);

        getChildren().add(arrow);

    }

    private Path createPath() {
        Path path = new Path();

        double startX = 50;
        double startY = 20;
        double endX = 70;
        double endY = 70;
        double arrowHeadSize = 20;

        path.strokeProperty().bind(path.fillProperty());
        path.setFill(Color.BLACK);

        System.out.println(path.getLayoutX());

        //Line
        path.getElements().add(new MoveTo(startX, startY));
        path.getElements().add(new LineTo(endX, endY));

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);


        //point1
        double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;


//        path.getElements().add(new LineTo(x1, y1));
//        path.getElements().add(new LineTo(x2, y2));
//        path.getElements().add(new LineTo(endX, endY));

        return path;

    }

    public State getSource() {
        return source;
    }

    public State getTarget() {
        return target;
    }

}