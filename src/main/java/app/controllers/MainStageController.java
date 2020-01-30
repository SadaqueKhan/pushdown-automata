package app.controllers;

import app.models.DiagramModel;
import app.views.MainStageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Serializable;


public class MainStageController extends Application implements Serializable {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        MainStageView mainStageView = new MainStageView(this);


        //Basically acting
        DiagramModel diagramModel = new DiagramModel();


        new DiagramController(mainStageView, diagramModel);


        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 500));
        primaryStage.show();

    }

}