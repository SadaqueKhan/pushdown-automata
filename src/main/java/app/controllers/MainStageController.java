package app.controllers;

import app.views.MainStageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainStageController extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        MainStageView mainStageView = new MainStageView(this);


        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 500));
        primaryStage.show();

    }

}