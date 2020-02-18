package app.view;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TapeView extends HBox {

    public TapeView() {

        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIListeners() {
    }

    private void setUpUIComponents() {
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

            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle, new Text("..."));

            this.getChildren().add(stack);
        }
    }

    public void setUpUIComponents(String inputWord) {

        this.getChildren().clear();

        for (String inputSymbol : inputWord.split("")) {
            //Drawing a Rectangle
            Rectangle rectangle = new Rectangle();
            //Setting the properties of the rectangle
            rectangle.setX(10);
            rectangle.setY(0);
            rectangle.setWidth(100);
            rectangle.setHeight(100);
            rectangle.setFill(Color.WHITE);
            rectangle.setStroke(Color.BLACK);

            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle, new Text(inputSymbol));

            this.getChildren().add(stack);
        }
    }
}
