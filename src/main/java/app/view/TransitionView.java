package app.view;

import javafx.geometry.Bounds;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class TransitionView extends Line {

    private StateView source;
    private StateView target;
    private double layoutX;
    private double layoutY;

    private double sceneX;
    private double sceneY;
    private VBox transitionListVBox;

    public TransitionView(StateView source, StateView target) {
        this.source = source;
        this.target = target;
        setUpUIComponents();
    }

    private void setUpUIComponents() {
        transitionListVBox = new VBox();
        transitionListVBox.setStyle("-fx-background-color:grey;-fx-border-width:1px;-fx-border-color:black;");

        transitionListVBox.setOnMousePressed(mouseEvent -> {
            sceneX = mouseEvent.getScreenX();
            sceneY = mouseEvent.getScreenY();
            layoutX = transitionListVBox.getLayoutX();
            layoutY = transitionListVBox.getLayoutY();
        });

        transitionListVBox.setOnMouseDragged(mouseEvent -> {
            // Offset of drag
            double offsetX = mouseEvent.getScreenX() - sceneX;
            double offsetY = mouseEvent.getScreenY() - sceneY;

            // Taking parent bounds
            Bounds parentBounds = transitionListVBox.getParent().getLayoutBounds();

            // Drag node bounds
            double currPaneLayoutX = transitionListVBox.getLayoutX();
            double currPaneWidth = transitionListVBox.getWidth();
            double currPaneLayoutY = transitionListVBox.getLayoutY();
            double currPaneHeight = transitionListVBox.getHeight();

            if ((currPaneLayoutX + offsetX < parentBounds.getWidth() - currPaneWidth) && (currPaneLayoutX + offsetX > -1)) {
                // If the dragNode bounds is within the parent bounds, then you can set the offset value.
                transitionListVBox.setTranslateX(offsetX);

            } else if (currPaneLayoutX + offsetX < 0) {
                // If the sum of your offset and current layout position is negative, then you ALWAYS update your translate to negative layout value
                // which makes the final layout position to 0 in mouse released event.
                transitionListVBox.setTranslateX(-currPaneLayoutX);
            } else {
                // If your dragNode bounds are outside parent bounds,ALWAYS setting the translate value that fits your node at end.
                transitionListVBox.setTranslateX(parentBounds.getWidth() - currPaneLayoutX - currPaneWidth);
            }

            if ((currPaneLayoutY + offsetY < parentBounds.getHeight() - currPaneHeight) && (currPaneLayoutY + offsetY > -1)) {
                transitionListVBox.setTranslateY(offsetY);
            } else if (currPaneLayoutY + offsetY < 0) {
                transitionListVBox.setTranslateY(-currPaneLayoutY);
            } else {
                transitionListVBox.setTranslateY(parentBounds.getHeight() - currPaneLayoutY - currPaneHeight);
            }
        });


        transitionListVBox.setOnMouseReleased(event -> {
            // Updating the new layout positions
            transitionListVBox.setLayoutX(layoutX + transitionListVBox.getTranslateX());
            transitionListVBox.setLayoutY(layoutY + transitionListVBox.getTranslateY());

            // Resetting the translate positions
            transitionListVBox.setTranslateX(0);
            transitionListVBox.setTranslateY(0);
        });
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