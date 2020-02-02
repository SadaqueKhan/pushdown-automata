//Complete

package app.views;

import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TransitionView extends Group {

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

        setUpComponents();

    }

    private void setUpComponents() {

        //Create arrow shaft using line object
        this.arrowShaft = new Line();
        arrowShaft.setStrokeWidth(3);

        double x = 40.0;
        double y = 0.0;

        //Bind arrow shaft start point to the source state (i.e. where the arrow will be point from)
        arrowShaft.startXProperty().bind(source.layoutXProperty().add(x));
        arrowShaft.startYProperty().bind(source.layoutYProperty().add(y));

        System.out.println("X direction of source: " + source.getBoundsInParent().getWidth());
        System.out.println("Y direction of source: " + source.getBoundsInParent().getHeight());


        //Bind arrow shaft end point to the target state (i.e. where the arrow will be point towards)
        arrowShaft.endXProperty().bind(target.layoutXProperty().add(x));
        arrowShaft.endYProperty().bind(target.layoutYProperty().add(y));


        System.out.println("X direction of target: " + target.getBoundsInParent().getWidth() / 2.0);
        System.out.println("Y direction of target: " + target.getBoundsInParent().getHeight() / 2.0);

        //Create first side of arrow tip using Line object
        this.arrowTipSide1 = new Line();
        arrowTipSide1.setStrokeWidth(3);

        //Create second side of arrow tip using Line object
        this.arrowTipSide2 = new Line();
        arrowTipSide2.setStrokeWidth(3);


        arrowLabel = new Text(transitions);

        //TODO Remove this listeners from the view.

        //Create listener to help update positioning of arrows tip on the shaft
        InvalidationListener updater = o -> {
            // Store start/end points of arrow shaft in a variable
            double sx = arrowShaft.getStartX();
            double sy = arrowShaft.getStartY();
            double ex = arrowShaft.getEndX();
            double ey = arrowShaft.getEndY();

            // Set start/end points of arrows tips (i.e. the end point of the arrow shaft)
            arrowTipSide1.setEndX(ex);
            arrowTipSide1.setEndY(ey);
            arrowTipSide2.setEndX(ex);
            arrowTipSide2.setEndY(ey);

            arrowLabel.setX((sx + ex) / 2);
            arrowLabel.setY((sy + ey) / 2);

            if (ex == sx && ey == sy) {
                // arrow parts of length 0
                arrowTipSide1.setStartX(ex);
                arrowTipSide1.setStartY(ey);
                arrowTipSide2.setStartX(ex);
                arrowTipSide2.setStartY(ey);
            } else {
                double factor = arrowTipLength / Math.hypot(sx - ex, sy - ey);
                double factorO = arrowTipWidth / Math.hypot(sx - ex, sy - ey);

                // part in direction of main line
                double dx = (sx - ex) * factor;
                double dy = (sy - ey) * factor;

                // part ortogonal to main line
                double ox = (sx - ex) * factorO;
                double oy = (sy - ey) * factorO;

                arrowTipSide1.setStartX(ex + dx - oy);
                arrowTipSide1.setStartY(ey + dy + ox);
                arrowTipSide2.setStartX(ex + dx + oy);
                arrowTipSide2.setStartY(ey + dy - ox);
            }
        };

        // add listener to arrow shafts
        arrowShaft.startXProperty().addListener(updater);
        arrowShaft.startYProperty().addListener(updater);
        arrowShaft.endXProperty().addListener(updater);
        arrowShaft.endYProperty().addListener(updater);

        // add listener to text
        arrowLabel.xProperty().addListener(updater);
        arrowLabel.yProperty().addListener(updater);

        updater.invalidated(null);


        // Add arrow shaft/arrowtips into a group to create an arrow
        getChildren().add(arrowShaft);
        getChildren().add(arrowTipSide1);
        getChildren().add(arrowTipSide2);
        getChildren().add(arrowLabel);


    }


    public StateView getSource() {
        return source;
    }

    public StateView getTarget() {
        return target;
    }


}