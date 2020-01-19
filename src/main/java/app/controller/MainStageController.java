package app.controller;

import app.view.MainStageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainStageController extends Application {


    private MainStageView mainStageView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.mainStageView = new MainStageView(this);

        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 500));
        primaryStage.show();


    }

}