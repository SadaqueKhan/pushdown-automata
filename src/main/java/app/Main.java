package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    Button button;

    private Line line = new Line();

    public static void main(String[] args) {

        launch(args);
        System.out.println("Helloworld");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}