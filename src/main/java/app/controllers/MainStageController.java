package app.controllers;

import app.models.MachineModel;
import app.views.MainStageView;
import javafx.application.Application;
import javafx.scene.Node;
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

        for (Node node : mainStageView.getContainerForCenterNodes().getChildren()) {
            System.out.println("Start " + node.toString());
        }

        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 1000));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public TransitionTableController getTransitionTableController() {
        return transitionTableController;
    }

    public DiagramController getDiagramController() {
        return diagramController;
    }


    public void triggerDiagramScene() {

        for (Node node : mainStageView.getContainerForCenterNodes().getChildren()) {
            System.out.println("Diagram button clicked: " + node.toString());
        }
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        diagramController = new DiagramController(mainStageView, this, machineModel);
    }

    public void triggerTransitionTableScene() {

        for (Node node : mainStageView.getContainerForCenterNodes().getChildren()) {
            System.out.println("Transition button clicked: " + node.toString());
        }
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        transitionTableController = new TransitionTableController(mainStageView, this, machineModel);
    }

    public void saveInputWord(String userInputWord) {
        mainStageView.getInputWordSet().add(userInputWord);
        if (mainStageView.getAutoCompletionBinding() != null) {
            mainStageView.getAutoCompletionBinding().dispose();
        }
        mainStageView.setAutoCompletionBinding(TextFields.bindAutoCompletion(mainStageView.getInputTextField(), mainStageView.getInputWordSet()));
    }
}