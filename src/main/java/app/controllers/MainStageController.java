package app.controllers;

import app.models.DiagramModel;
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

        DiagramModel diagramModel = new DiagramModel();

        DiagramController diagramController = new DiagramController(mainStageView, diagramModel);



        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 500));
        primaryStage.show();

    }

}