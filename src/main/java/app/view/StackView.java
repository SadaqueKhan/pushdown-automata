package app.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class StackView extends ScrollPane {

    private VBox vBox;

    public StackView() {

        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIListeners() {
    }

    private void setUpUIComponents() {
        vBox = new VBox();

        for (int i = 0; i < 1; i++) {
            //Drawing a Rectangle
            Rectangle rectangle = new Rectangle();
            //Setting the properties of the rectangle
            rectangle.setX(10);
            rectangle.setY(0);
            rectangle.setWidth(50);
            rectangle.setHeight(50);
            rectangle.setFill(Color.WHITE);
            rectangle.setStroke(Color.BLACK);

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, new Text("..."));

            vBox.getChildren().add(stackPane);
        }
        this.pannableProperty().set(true);
        this.fitToWidthProperty().set(true);
        this.fitToHeightProperty().set(true);
        this.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.setContent(vBox);
    }
}