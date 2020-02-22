package app.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class TapeView extends ScrollPane {

    private HBox tapeViewHBoxContainer;

    public TapeView() {
        setUpUIComponents();
    }

    private void setUpUIComponents() {
        tapeViewHBoxContainer = new HBox();

        ArrayList<StackPane> tapeSquareList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Polygon reflexiveArrowTipPolygon = new Polygon(4, 0, 8, 8, 0, 8);
            reflexiveArrowTipPolygon.setFill(Color.BLACK);
            reflexiveArrowTipPolygon.setStroke(Color.BLACK);
            reflexiveArrowTipPolygon.setStrokeWidth(10);
            reflexiveArrowTipPolygon.setRotate(65);
            reflexiveArrowTipPolygon.setTranslateX(5);
            reflexiveArrowTipPolygon.setTranslateY(-50);
            reflexiveArrowTipPolygon.setVisible(false);
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
            stackPane.getChildren().addAll(rectangle, new Text("..."), reflexiveArrowTipPolygon);
            tapeSquareList.add(stackPane);
            tapeViewHBoxContainer.getChildren().add(stackPane);
        }

        this.pannableProperty().set(true);
        this.fitToWidthProperty().set(true);
        this.fitToHeightProperty().set(true);
        this.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        this.setContent(tapeViewHBoxContainer);

    }

    public HBox getTapeViewHBoxContainer() {
        return tapeViewHBoxContainer;
    }
}
