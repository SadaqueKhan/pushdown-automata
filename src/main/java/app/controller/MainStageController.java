package app.controller;

import app.model.MachineModel;
import app.view.MainStageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;


public class MainStageController extends Application {

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

        diagramController.loadDiagramViewOntoStage(transitionTableController);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Pushdown Automata");
        primaryStage.setScene(new Scene(mainStageView, 1500, 1000));
        primaryStage.show();
    }

    public void triggerDiagramView() {
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        diagramController.loadDiagramViewOntoStage(transitionTableController);
    }

    public void triggerTransitionTableView() {
        mainStageView.getContainerForCenterNodes().getChildren().remove(1);
        transitionTableController.loadTransitionTableOntoStage(diagramController);
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

    public void saveMachine() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Machine");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));
            File fileChosen = fileChooser.showSaveDialog(primaryStage);

            if (fileChosen != null) {
                JAXBContext contextObj = JAXBContext.newInstance(MachineModel.class);
                Marshaller marshallerObj = contextObj.createMarshaller();
                marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshallerObj.marshal(machineModel, new FileOutputStream(fileChosen));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void loadMachine() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Machine");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));

            File fileChosen = fileChooser.showOpenDialog(primaryStage);

            if (fileChosen != null) {

                JAXBContext jaxbContext = JAXBContext.newInstance(MachineModel.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                MachineModel machineModelLoaded = (MachineModel) jaxbUnmarshaller.unmarshal(fileChosen);

                primaryStage.close();
                this.machineModel = machineModelLoaded;
                this.mainStageView = new MainStageView(this);

                this.transitionTableController = new TransitionTableController(mainStageView, this, machineModel);
                transitionTableController.loadTansitionsOntoTransitionTable();

                this.diagramController = new DiagramController(mainStageView, this, machineModel);
                diagramController.loadStatesOntoDiagram();
                diagramController.loadTransitionsOntoDiagram();
                diagramController.loadDiagramViewOntoStage(transitionTableController);


                primaryStage.setTitle("Pushdown Automata");
                primaryStage.setScene(new Scene(mainStageView, 1500, 1000));
                primaryStage.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void launchWiki() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SadaqueKhan/pushdown-automata"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}