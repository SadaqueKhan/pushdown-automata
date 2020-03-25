package app.presenter;
import app.model.MachineModel;
import app.view.MainStage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
import java.util.ArrayList;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Main stage presenter retrieves data from repositories (the model), and formats it for display in the main stage.
 * </p>
 */
public class MainStagePresenter extends Application {
    private MachineModel machineModel;
    private MainStage mainStage;
    private TransitionTableScenePresenter transitionTableScenePresenter;
    private DiagramScenePresenter diagramScenePresenter;
    private Stage primaryWindow;
    private StackPane headPointerStackPane;
    /**
     * Method so you can run JAR files that were created without the JavaFX Launcher, such as when using an IDE in
     * which the JavaFX tools are not fully integrated.
     * @param args command line arguments when executing a program.
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Method is the main entry point for the applications.
     * @param primaryWindow the initial window that is rendered.
     */
    @Override
    public void start(Stage primaryWindow) {
        this.machineModel = new MachineModel();
        this.mainStage = new MainStage(this);
        this.transitionTableScenePresenter = new TransitionTableScenePresenter(mainStage, machineModel);
        this.diagramScenePresenter = new DiagramScenePresenter(mainStage, this, machineModel);
        diagramScenePresenter.loadDiagramViewOntoStage(transitionTableScenePresenter);
        this.primaryWindow = primaryWindow;
        this.primaryWindow.setTitle("Pushdown Automata");
        this.primaryWindow.setScene(new Scene(mainStage, 1500, 1000));
        this.primaryWindow.setResizable(false);
        this.primaryWindow.show();
    }
    /**
     * Loads the diagram scene onto the main stage when selected via the tab found on in the main stage.
     */
    public void loadDiagramScene() {
        mainStage.getContainerForCenterNodes().getChildren().remove(1);
        mainStage.getInputTextField().setDisable(false);
        diagramScenePresenter.loadDiagramViewOntoStage(transitionTableScenePresenter);
    }
    /**
     * Loads the transition table scene onto the main stage when selected via the tab found on in the main stage.
     */
    public void loadTransitionTableScene() {
        mainStage.getContainerForCenterNodes().getChildren().remove(1);
        mainStage.getInputTextField().setDisable(true);
        transitionTableScenePresenter.loadTransitionTableSceneOntoMainStage(diagramScenePresenter);
    }
    /**
     * Loads the simulation stage when the user presses enter on the textfield retaining an input word to be
     * simulated against there constructed PDA.
     * @param inputWord requested to be simulated.
     */
    public void loadSimulationStage(String inputWord) {
        if (machineModel.findStartStateModel() == null) {
            Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                    "No start state defined for machine simulation can not be executed.", ButtonType.OK);
            invalidActionAlert.setHeaderText("Information");
            invalidActionAlert.setTitle("Invalid Action");
            invalidActionAlert.show();
        } else {
            if (mainStage.getSimulationByQuickRunMenuItem().isSelected()) {
                setUpTapeView(inputWord);
                new SimulationStagePresenter(this, machineModel, inputWord, mainStage.getSimulationByQuickRunMenuItem().getText());
            }
            if (mainStage.getSimulationByStepRunMenuItem().isSelected()) {
                setUpTapeView(inputWord);
                new SimulationStagePresenter(this, machineModel, inputWord, mainStage.getSimulationByStepRunMenuItem().getText());
            }
        }
    }
    /**
     * Sets up the tape scene with the input word selected to be simulated.
     * @param inputWord requested to be simulated.
     */
    private void setUpTapeView(String inputWord) {
        HBox tapeViewHBoxContainer = mainStage.getTapeScene().getTapeViewHBoxContainer();
        tapeViewHBoxContainer.getChildren().clear();
        for (String inputSymbol : inputWord.split("")) {
            Polygon headPointer = new Polygon(4, 0, 8, 8, 0, 8);
            headPointer.setFill(Color.BLACK);
            headPointer.setStroke(Color.BLACK);
            headPointer.setStrokeWidth(10);
            headPointer.setRotate(65);
            headPointer.setTranslateX(5);
            headPointer.setTranslateY(-50);
            headPointer.setVisible(false);
            Rectangle rectangle = new Rectangle();
            rectangle.setX(10);
            rectangle.setY(0);
            rectangle.setWidth(100);
            rectangle.setHeight(100);
            rectangle.setFill(Color.WHITE);
            rectangle.setStroke(Color.BLACK);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, new Text(inputSymbol), headPointer);
            tapeViewHBoxContainer.getChildren().add(stackPane);
        }
    }
    /**
     * Handles saving an input word to a history list for auto completion.
     * @param inputWord requested to be simulated.
     */
    public void saveInputWord(String inputWord) {
        mainStage.getInputWordSet().add(inputWord);
        if (mainStage.getAutoCompletionBinding() != null) {
            mainStage.getAutoCompletionBinding().dispose();
        }
        mainStage.setAutoCompletionBinding(TextFields.bindAutoCompletion(mainStage.getInputTextField(), mainStage.getInputWordSet()));
    }
    /**
     * Handles the launching of the GitHub wiki.
     */
    public void launchWiki() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SadaqueKhan/pushdown-automata"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    /**
     * Handles the saving of a user defined machine.
     */
    public void saveMachine() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Machine");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));
            File fileChosen = fileChooser.showSaveDialog(primaryWindow);
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
    /**
     * Handles the loading of a user defined machine.
     */
    public void loadMachine() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Machine");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));
            File fileChosen = fileChooser.showOpenDialog(primaryWindow);
            if (fileChosen != null) {
                JAXBContext jaxbContext = JAXBContext.newInstance(MachineModel.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                MachineModel machineModelLoaded = (MachineModel) jaxbUnmarshaller.unmarshal(fileChosen);
                primaryWindow.close();
                this.machineModel = machineModelLoaded;
                this.mainStage = new MainStage(this);
                this.transitionTableScenePresenter = new TransitionTableScenePresenter(mainStage, machineModel);
                transitionTableScenePresenter.loadTransitionTableView();
                this.diagramScenePresenter = new DiagramScenePresenter(mainStage, this, machineModel);
                diagramScenePresenter.loadStatesOntoDiagram();
                diagramScenePresenter.loadTransitionsOntoDiagram();
                diagramScenePresenter.loadDiagramViewOntoStage(transitionTableScenePresenter);
                primaryWindow.setTitle("Pushdown Automata");
                primaryWindow.setScene(new Scene(mainStage, 1500, 1000));
                primaryWindow.setResizable(false);
                primaryWindow.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    /**
     * Set the simulation to quick run.
     */
    public void setSimulationToQuickRun() {
        mainStage.getSimulationByQuickRunMenuItem().setSelected(true);
        mainStage.getSimulationByStepRunMenuItem().setSelected(false);
    }
    /**
     * Sets the simulation to step run.
     */
    public void setSimulationToStepRun() {
        mainStage.getSimulationByStepRunMenuItem().setSelected(true);
        mainStage.getSimulationByQuickRunMenuItem().setSelected(false);
    }
    /**
     * Sets the acceptance criteria to final state.
     */
    public void setAcceptanceCriteriaToFinalState() {
        machineModel.setAcceptanceByFinalState(true);
        machineModel.setAcceptanceByEmptyStack(false);
        mainStage.getAcceptanceByFinalStateMenuItem().setSelected(true);
        mainStage.getAcceptanceByEmptyStackMenuItem().setSelected(false);
        mainStage.getInputTextLabel().setText("Input word (acceptance by final state)");
    }
    /**
     * Sets the acceptance criteria to empty stack.
     */
    public void setAcceptanceCriteriaToEmptyStack() {
        machineModel.setAcceptanceByFinalState(false);
        machineModel.setAcceptanceByEmptyStack(true);
        mainStage.getAcceptanceByFinalStateMenuItem().setSelected(false);
        mainStage.getAcceptanceByEmptyStackMenuItem().setSelected(true);
        mainStage.getInputTextLabel().setText("Input word (acceptance by empty stack)");
    }
    /**
     * Updates the tape scene given a head position integer value.
     * @param headPosition the current head position for a simulation.
     */
    void updateTapeScene(int headPosition) {
        if (headPointerStackPane != null) {
            headPointerStackPane.getChildren().get(2).setVisible(false);
        }
        HBox tapeViewVBoxContainer = mainStage.getTapeScene().getTapeViewHBoxContainer();
        if (!(headPosition == 0)) {
            this.headPointerStackPane = (StackPane) tapeViewVBoxContainer.getChildren().get(headPosition - 1);
            headPointerStackPane.getChildren().get(2).setVisible(true);
        }
    }
    /**
     * Updates the stack scene given a stack content list.
     * @param stackContent the current stack content for a simulation.
     */
    void updateStackScene(ArrayList<String> stackContent) {
        VBox stackViewVBoxContainer = mainStage.getStackScene().getStackViewVBoxContainer();
        stackViewVBoxContainer.getChildren().clear();
        if (stackContent.isEmpty()) {
            Rectangle rectangle = new Rectangle();
            rectangle.setX(10);
            rectangle.setY(0);
            rectangle.setWidth(70);
            rectangle.setHeight(35);
            rectangle.setFill(Color.WHITE);
            rectangle.setStroke(Color.BLACK);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, new Text("..."));
            stackViewVBoxContainer.getChildren().add(stackPane);
        } else {
            for (int i = stackContent.size(); i-- > 0; ) {
                //Drawing a Rectangle
                Rectangle rectangle = new Rectangle();
                //Setting the properties of the rectangle
                rectangle.setX(10);
                rectangle.setY(0);
                rectangle.setWidth(70);
                rectangle.setHeight(35);
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(rectangle, new Text(stackContent.get(i)));
                stackViewVBoxContainer.getChildren().add(stackPane);
            }
        }
    }
    // Getters to provide communication between scenes.
    MainStage getMainStage() {
        return mainStage;
    }
    DiagramScenePresenter getDiagramScenePresenter() {
        return diagramScenePresenter;
    }
    Stage getPrimaryWindow() {
        return primaryWindow;
    }
}