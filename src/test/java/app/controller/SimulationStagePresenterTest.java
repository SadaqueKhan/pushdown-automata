package app.controller;
import app.model.ConfigurationModel;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.presenter.MainStagePresenter;
import app.presenter.SimulationStagePresenter;
import app.view.MainStage;
import app.view.QuickRunSimulationScene;
import app.view.StepRunSimulationScene;
import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class SimulationStagePresenterTest extends ApplicationTest {
    private MachineModel machineModel;
    private Stage stage;
    private MainStage mainStage;
    private SimulationStagePresenter simulationStagePresenter;
    private MainStagePresenter mainStagePresenter;
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStagePresenter = new MainStagePresenter();
        stage.setAlwaysOnTop(true);
        mainStagePresenter.start(stage);
        this.stage = stage;
        mainStage = mainStagePresenter.getMainStage();
        //Machine for On = 1n
        machineModel = mainStagePresenter.getMachineModel();
        StateModel stateModelQ0 = new StateModel();
        stateModelQ0.setStartState(true);
        stateModelQ0.setFinalState(true);
        machineModel.addStateModelToStateModelSet(stateModelQ0);
        StateModel stateModelQ1 = new StateModel();
        machineModel.addStateModelToStateModelSet(stateModelQ1);
        StateModel stateModelQ2 = new StateModel();
        machineModel.addStateModelToStateModelSet(stateModelQ2);
        StateModel stateModelQ3 = new StateModel();
        stateModelQ3.setFinalState(true);
        machineModel.addStateModelToStateModelSet(stateModelQ3);
        TransitionModel transitionModel1 = new TransitionModel(stateModelQ0, "\u03B5", "\u03B5", stateModelQ1, "$");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel1);
        TransitionModel transitionModel2 = new TransitionModel(stateModelQ1, "1", "0", stateModelQ2, "\u03B5");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel2);
        TransitionModel transitionModel3 = new TransitionModel(stateModelQ2, "1", "0", stateModelQ2, "\u03B5");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel3);
        TransitionModel transitionModel4 = new TransitionModel(stateModelQ2, "\u03B5", "$", stateModelQ3, "\u03B5");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel4);
        TransitionModel transitionModel5 = new TransitionModel(stateModelQ1, "0", "\u03B5", stateModelQ1, "0");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel5);
        mainStagePresenter.getDiagramScenePresenter().loadStatesOntoDiagram();
        mainStagePresenter.getDiagramScenePresenter().loadTransitionsOntoDiagram();
    }
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    @Test
    public void selectingSimulationQuickRunAndEnteringInputWordShouldRenderQuickRunScene() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByQuickRunMenuItem");
        String inputWordString = "0011";
        robot.clickOn(mainStage.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        assertNotNull(mainStagePresenter.getSimulationStagePresenter().getQuickRunSimulationScene());
        robot.closeCurrentWindow();
    }
    @Test
    public void selectingSimulationStepRunAndEnteringInputWordShouldRenderStepRunScene() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByStepRunMenuItem");
        String inputWordString = "0011";
        robot.clickOn(mainStage.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        assertNotNull(mainStagePresenter.getSimulationStagePresenter().getStepRunSimulationScene());
        robot.closeCurrentWindow();
    }
    @Test
    public void pressingForwardStepRunSimulationShouldUpdateDisplayBoardWithNewConfiguration() throws Exception {
        Platform.runLater(
                () -> {
                    mainStagePresenter.setSimulationToStepRun();
                    mainStagePresenter.loadSimulationStage("0011");
                    SimulationStagePresenter simulationStagePresenter = mainStagePresenter.getSimulationStagePresenter();
                    StepRunSimulationScene stepRunSimulationScene = simulationStagePresenter.getStepRunSimulationScene();
                    ListView transitionOptionsListView = stepRunSimulationScene.getTransitionOptionsListView();
                    transitionOptionsListView.getSelectionModel().selectFirst();
                    String configurationBeforeClickingForward = stepRunSimulationScene.getCurrentConfigTextField().getText();
                    simulationStagePresenter.stepForward();
                    String configurationAfterClickingForward = stepRunSimulationScene.getCurrentConfigTextField().getText();
                    assertFalse(configurationBeforeClickingForward.equals(configurationAfterClickingForward));
                }
        );
    }
    @Test
    public void pressingBackStepRunSimulationShouldUpdateDisplayBoardWithNewConfiguration() throws Exception {
        Platform.runLater(
                () -> {
                    mainStagePresenter.setSimulationToStepRun();
                    mainStagePresenter.loadSimulationStage("0011");
                    SimulationStagePresenter simulationStagePresenter = mainStagePresenter.getSimulationStagePresenter();
                    StepRunSimulationScene stepRunSimulationScene = simulationStagePresenter.getStepRunSimulationScene();
                    ListView transitionOptionsListView = stepRunSimulationScene.getTransitionOptionsListView();
                    transitionOptionsListView.getSelectionModel().select(1);
                    simulationStagePresenter.stepForward();
                    String configurationBeforeClickingBack = stepRunSimulationScene.getCurrentConfigTextField().getText();
                    simulationStagePresenter.stepBack();
                    String configurationAfterClickingBack = stepRunSimulationScene.getCurrentConfigTextField().getText();
                    assertFalse(configurationBeforeClickingBack.equals(configurationAfterClickingBack));
                }
        );
    }
    @Test
    public void clickingAlgorithmTabShouldRenderPathList() {
        Platform.runLater(
                () -> {
                    mainStagePresenter.setSimulationToQuickRun();
                    mainStagePresenter.loadSimulationStage("0011");
                    SimulationStagePresenter simulationStagePresenter = mainStagePresenter.getSimulationStagePresenter();
                    simulationStagePresenter.loadAlgorithmScene();
                    QuickRunSimulationScene quickRunSimulationScene = simulationStagePresenter.getQuickRunSimulationScene();
                    ListView algorithmListView = quickRunSimulationScene.getAlgorithmlistView();
                    assertTrue(0 < algorithmListView.getItems().size());
                }
        );
    }
    @Test
    public void clickingPathTabShouldRenderPathList() {
        Platform.runLater(
                () -> {
                    mainStagePresenter.setSimulationToQuickRun();
                    mainStagePresenter.loadSimulationStage("0011");
                    SimulationStagePresenter simulationStagePresenter = mainStagePresenter.getSimulationStagePresenter();
                    simulationStagePresenter.loadPathsScene();
                    QuickRunSimulationScene quickRunSimulationScene = simulationStagePresenter.getQuickRunSimulationScene();
                    Accordion accordion = (Accordion) quickRunSimulationScene.getPathsVBox().getChildren().get(0);
                    assertTrue(0 < accordion.getPanes().size());
                }
        );
    }
    @Test
    public void doubleClickingAlgorithmItemShouldRenderIndependentPath() {
        Platform.runLater(
                () -> {
                    mainStagePresenter.setSimulationToQuickRun();
                    mainStagePresenter.loadSimulationStage("0011");
                    SimulationStagePresenter simulationStagePresenter = mainStagePresenter.getSimulationStagePresenter();
                    QuickRunSimulationScene quickRunSimulationScene = simulationStagePresenter.getQuickRunSimulationScene();
                    ListView listView = quickRunSimulationScene.getAlgorithmlistView();
                    listView.getSelectionModel().selectFirst();
                    ConfigurationModel configuration = (ConfigurationModel) listView.getSelectionModel().getSelectedItem();
                    simulationStagePresenter.createIndependentPathSimulationStage(configuration);
                    assertFalse(stage.isFocused());
                }
        );
    }
}
