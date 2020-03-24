package app.view;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * View blueprint for a tape scene.
 * </p>
 */
public class TapeScene extends ScrollPane {
    //Reference to UI components for view.
    private HBox tapeViewHBoxContainer;
    /**
     * Constructor of the tape scene, used to instantiate an instance of the view.
     */
    TapeScene() {
        setUpUIComponents();
    }
    /**
     * Sets up the UI components of the view.
     */
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
    // Getters for UI components of the view.¬
    public HBox getTapeViewHBoxContainer() {
        return tapeViewHBoxContainer;
    }
}
