package app.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TapeView extends ScrollPane {

    private HBox tapeViewHBoxContainer;

    public TapeView() {
        setUpUIComponents();
    }

    private void setUpUIComponents() {
        tapeViewHBoxContainer = new HBox();

        for (int i = 0; i < 10; i++) {
            //Drawing a Rectangle
            Rectangle rectangle = new Rectangle();
            //Setting the properties of the rectangle
            rectangle.setX(10);
            rectangle.setY(0);
            rectangle.setWidth(100);
            rectangle.setHeight(100);
            rectangle.setFill(Color.WHITE);
            rectangle.setStroke(Color.BLACK);

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, new Text("..."));
            tapeViewHBoxContainer.getChildren().add(stackPane);
        }
        
        this.pannableProperty().set(true);
        this.fitToWidthProperty().set(true);
        this.fitToHeightProperty().set(true);
        this.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.setContent(tapeViewHBoxContainer);

    }

    public HBox getTapeViewHBoxContainer() {
        return tapeViewHBoxContainer;
    }
}
