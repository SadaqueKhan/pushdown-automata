//Complete

package app.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

public class TransitionView extends Line {

    private StateView source;
    private StateView target;

    private final String transitions;

    private Line arrowShaft;
    private Line arrowTipSide1;
    private Line arrowTipSide2;

    private double arrowTipLength = 20;
    private double arrowTipWidth = 7;

    private Text arrowLabel;

    public TransitionView(StateView source, StateView target, String transitions) {

        this.source = source;
        this.target = target;
        this.transitions = transitions;

        source.addStateChild(target);
        target.addStateParent(source);

        if (source.equals(target)) {
            setUpUIReflexiveTransitionComponents();
        } else {
            setUpUIStandardTransitionComponents();
        }
    }

    private void setUpUIReflexiveTransitionComponents() {

        double x = 0.0;
        double y = -30.0;

        Arc arc = new Arc();

        arc.setCenterX(50);
        arc.setCenterY(70);

        arc.setRadiusX(40);
        arc.setRadiusY(40);

        arc.setStartAngle(340);
        arc.setLength(220);

        arc.setType(ArcType.OPEN);
        arc.setStrokeWidth(3);
        arc.setStroke(Color.BLACK);
        arc.setStrokeType(StrokeType.INSIDE);
        arc.setFill(null);

        arc.centerXProperty().bind(source.layoutXProperty().add(x));
        //Bind arrow shaft start point to the source state (i.e. where the arrow will be point from)
        arc.centerYProperty().bind(source.layoutYProperty().add(y));

//        getChildren().add(arc);
    }

    private void setUpUIStandardTransitionComponents() {

        this.setStroke(Color.BLUE);
        this.setStrokeWidth(2);

        //Bind arrow shaft start point to the source state (i.e. where the arrow will be point from)
        this.startXProperty().bind(source.layoutXProperty().add(source.getCurrentStateXPosition() / 2));
        this.startYProperty().bind(source.layoutYProperty().add(source.getCurrentStateYPosition() / 2));

        //Bind arrow shaft end point to the target state (i.e. where the arrow will be point towards)
        this.endXProperty().bind(target.layoutXProperty().add(target.getCurrentStateXPosition() / 2));
        this.endYProperty().bind(target.layoutYProperty().add(target.getCurrentStateYPosition() / 2));

        this.setOpacity(0);
    }


    public StateView getSource() {
        return source;
    }

    public StateView getTarget() {
        return target;
    }


}