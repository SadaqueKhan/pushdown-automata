package app.controllers;

import app.models.MachineModel;
import app.views.MainStageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.Serializable;


public class MainStageController extends Application implements Serializable {

    private MachineModel machineModel;
    private MainStageView mainStageView;

    private TransitionTableController transitionTableController;
    private DiagramController diagramController;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.machineModel = new MachineModel();

        this.mainStageView = new MainStageView(this);
        this.transitionTableController = new TransitionTableController(mainStageView, this, machineModel);
        this.diagramController = new DiagramController(mainStageView, this, machineModel);

        mainStageView.getContainerForCenterNodes().getChildren().remove(1);

        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 1000));
        primaryStage.show();

    }


    public void triggerDiagramView() {
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        diagramController.loadDiagramView();
    }

    public void triggerTransitionTableView() {

        mainStageView.getContainerForCenterNodes().getChildren().remove(1);


        transitionTableController.loadTransitionTable(diagramController);
    }

    public void saveInputWord(String userInputWord) {
        mainStageView.getInputWordSet().add(userInputWord);
        if (mainStageView.getAutoCompletionBinding() != null) {
            mainStageView.getAutoCompletionBinding().dispose();
        }
        mainStageView.setAutoCompletionBinding(TextFields.bindAutoCompletion(mainStageView.getInputTextField(), mainStageView.getInputWordSet()));
    }


    public TransitionTableController getTransitionTableController() {
        return transitionTableController;
    }

    public DiagramController getDiagramController() {
        return diagramController;
    }

}