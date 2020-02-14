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

    private Stage primaryStage;

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

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 1000));
        primaryStage.show();
    }

    public void triggerDiagramView() {
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        diagramController.loadDiagramView(transitionTableController);
    }

    public void triggerTransitionTableView() {
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        transitionTableController.loadTransitionTable(diagramController);
    }

    public void triggerSimulationView(String inputWord) {
        new SimulationController(this, machineModel, inputWord);
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


    public void setSimulationProgressBar(boolean isSimulationInProgress) {
        mainStageView.getProgressBar().setVisible(isSimulationInProgress);
    }

    public void setUpTapeView(String inputWord) {
        mainStageView.getTapeView().setUpUIComponents(inputWord);
    }

    //TODO: REMOVE THESE GETTERS
    public MachineModel getMachineModel() {
        return machineModel;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}