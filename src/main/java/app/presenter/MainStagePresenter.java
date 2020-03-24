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
public class MainStagePresenter extends Application {
    private MachineModel machineModel;
    private MainStage mainStage;
    private TransitionTablePresenter transitionTablePresenter;
    private DiagramPresenter diagramPresenter;
    private Stage primaryWindow;
    private StackPane headPointerStackPane;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryWindow) throws Exception {
        this.machineModel = new MachineModel();
        this.mainStage = new MainStage(this);
        this.transitionTablePresenter = new TransitionTablePresenter(mainStage, machineModel);
        this.diagramPresenter = new DiagramPresenter(mainStage, this, machineModel);
        diagramPresenter.loadDiagramViewOntoStage(transitionTablePresenter);
        this.primaryWindow = primaryWindow;
        this.primaryWindow.setTitle("Pushdown Automata");
        this.primaryWindow.setScene(new Scene(mainStage, 1500, 1000));
        this.primaryWindow.setResizable(false);
        this.primaryWindow.show();
    }
    public void triggerDiagramView() {
        mainStage.getContainerForCenterNodes().getChildren().remove(1);
        mainStage.getInputTextField().setDisable(false);
        diagramPresenter.loadDiagramViewOntoStage(transitionTablePresenter);
    }
    public void triggerTransitionTableView() {
        mainStage.getContainerForCenterNodes().getChildren().remove(1);
        mainStage.getInputTextField().setDisable(true);
        transitionTablePresenter.loadTransitionTableSceneOntoMainStage(diagramPresenter);
    }
    public void triggerSimulationView(String inputWord) {
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
    public void setUpTapeView(String inputWord) {
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
    public DiagramPresenter getDiagramPresenter() {
        return diagramPresenter;
    }
    public Stage getPrimaryWindow() {
        return primaryWindow;
    }
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
    public void saveInputWord(String userInputWord) {
        mainStage.getInputWordSet().add(userInputWord);
        if (mainStage.getAutoCompletionBinding() != null) {
            mainStage.getAutoCompletionBinding().dispose();
        }
        mainStage.setAutoCompletionBinding(TextFields.bindAutoCompletion(mainStage.getInputTextField(), mainStage.getInputWordSet()));
    }
    public void launchWiki() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/SadaqueKhan/pushdown-automata"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
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
                this.transitionTablePresenter = new TransitionTablePresenter(mainStage, machineModel);
                transitionTablePresenter.loadTransitionTableView();
                this.diagramPresenter = new DiagramPresenter(mainStage, this, machineModel);
                diagramPresenter.loadStatesOntoDiagram();
                diagramPresenter.loadTransitionsOntoDiagram();
                diagramPresenter.loadDiagramViewOntoStage(transitionTablePresenter);
                primaryWindow.setTitle("Pushdown Automata");
                primaryWindow.setScene(new Scene(mainStage, 1500, 1000));
                primaryWindow.setResizable(false);
                primaryWindow.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    public void setSimulationToQuickRun() {
        mainStage.getSimulationByQuickRunMenuItem().setSelected(true);
        mainStage.getSimulationByStepRunMenuItem().setSelected(false);
    }
    public void setSimulationToStepRun() {
        mainStage.getSimulationByStepRunMenuItem().setSelected(true);
        mainStage.getSimulationByQuickRunMenuItem().setSelected(false);
    }
    public void setAcceptanceCriteriaToFinalState() {
        machineModel.setAcceptanceByFinalState(true);
        machineModel.setAcceptanceByEmptyStack(false);
        mainStage.getAcceptanceByFinalStateMenuItem().setSelected(true);
        mainStage.getAcceptanceByEmptyStackMenuItem().setSelected(false);
        mainStage.getInputTextLabel().setText("Input word (acceptance by final state)");
    }
    public void setAcceptanceCriteriaToEmptyStack() {
        machineModel.setAcceptanceByFinalState(false);
        machineModel.setAcceptanceByEmptyStack(true);
        mainStage.getAcceptanceByFinalStateMenuItem().setSelected(false);
        mainStage.getAcceptanceByEmptyStackMenuItem().setSelected(true);
        mainStage.getInputTextLabel().setText("Input word (acceptance by empty stack)");
    }
    public void updateTapeView(int headPosition) {
        if (headPointerStackPane != null) {
            headPointerStackPane.getChildren().get(2).setVisible(false);
        }
        HBox tapeViewVBoxContainer = mainStage.getTapeScene().getTapeViewHBoxContainer();
        if (!(headPosition == 0)) {
            this.headPointerStackPane = (StackPane) tapeViewVBoxContainer.getChildren().get(headPosition - 1);
            headPointerStackPane.getChildren().get(2).setVisible(true);
        }
    }
    public void updateStackView(ArrayList<String> stackContent) {
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
    public MainStage getMainStage() {
        return mainStage;
    }
}