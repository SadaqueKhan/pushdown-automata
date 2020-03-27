package app.controller;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.presenter.MainStagePresenter;
import app.view.MainStage;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
public class MainsStagePresenterTest extends ApplicationTest {
    private MainStagePresenter mainStagePresenter;
    private MainStage mainStage;
    private Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStagePresenter = new MainStagePresenter();
        stage.setAlwaysOnTop(true);
        mainStagePresenter.start(stage);
        this.stage = stage;
        this.mainStage = mainStagePresenter.getMainStage();
        //Machine for On = 1n
        MachineModel machineModel = mainStagePresenter.getMachineModel();
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
    }
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        mainStagePresenter.getDiagramScenePresenter().getDiagramScene().getChildren().clear();
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderAnInputWordOntoTextField() {
        clickOn(mainStage.getInputTextField());
        write("0011");
        assertEquals("0011", mainStage.getInputTextField().getText());
    }
    @Test
    public void clickingTableTabShoulRenderTransitionScene() {
        WaitForAsyncUtils.waitForFxEvents();
        clickOn(mainStage.getToggleTransitionTableButton());
        assertTrue(mainStage.getInputTextField().isDisabled());
    }
    @Test
    public void clickingDiagramTabShouldRenderDiagramScene() {
        clickOn(mainStage.getToggleDiagramButton());
        assertFalse(mainStage.getInputTextField().isDisabled());
    }
    @Test
    public void enteringInputWordIntoTextFieldWithNoStartStateDefinedShouldRenderAlert() {
        StateModel stateModel = mainStagePresenter.getMachineModel().getStartStateModel();
        stateModel.setStartState(false);
        String inputWordString = "0011";
        clickOn(mainStage.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        assertNull(mainStagePresenter.getMachineModel().getStartStateModel());
        stateModel.setStartState(true);
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderNewTapeScene() {
        String inputWordString = "0011";
        clickOn(mainStage.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(inputWordString.length(), mainStage.getTapeScene().getTapeViewHBoxContainer().getChildren()
                .size());
    }
    @Test
    public void updatinTapeSceneShouldRenderNewStackScene() throws Exception {
        String inputWordString = "0011";
        clickOn(mainStage.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        mainStagePresenter.updateTapeScene(1);
        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(
                () -> {
                    HBox tapeViewVBoxContainer = mainStage.getTapeScene().getTapeViewHBoxContainer();
                    StackPane headPointerStackPane = (StackPane) tapeViewVBoxContainer.getChildren().get(0);
                    Text tapeItemText = (Text) headPointerStackPane.getChildren().get(1);
                    Polygon tapeItemPointPolygon = (Polygon) headPointerStackPane.getChildren().get(2);
                    assertTrue(tapeItemPointPolygon.isVisible());
                    assertEquals("0", tapeItemText.getText());
                }
        );
    }
    @Test
    public void updatingStackSceneShouldRenderNewStackScene() throws Exception {
        ArrayList<String> stackContent = new ArrayList<>();
        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(
                () -> {
                    stackContent.add("s");
                    stackContent.add("t");
                    stackContent.add("a");
                    stackContent.add("c");
                    stackContent.add("k");
                    mainStagePresenter.updateStackScene(stackContent);
                    assertEquals(5, mainStage.getStackScene().getStackViewVBoxContainer().getChildren()
                            .size());
                }
        );
        Platform.runLater(
                () -> {
                    stackContent.clear();
                    mainStagePresenter.updateStackScene(stackContent);
                    assertEquals(1, mainStage.getStackScene().getStackViewVBoxContainer().getChildren()
                            .size());
                }
        );
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderSimulationStage() {
        String inputWordString = "0011";
        clickOn(mainStage.getInputTextField());
        write(inputWordString);
        assertNull(mainStagePresenter.getSimulationStagePresenter());
        press(KeyCode.ENTER);
        assertNotNull(mainStagePresenter.getSimulationStagePresenter());
    }
    @Test
    public void pressingSaveMenuItemShouldLaunchFileExplorer() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#fileMenu").clickOn("#saveMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertFalse(stage.isFocused());
    }
    @Test
    public void pressingLoadMenuItemShouldLaunchFileExplorer() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#fileMenu").clickOn("#loadMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertFalse(stage.isFocused());
    }
    @Test
    public void pressingAcceptByFinalStateMenuItemShouldUpdateAcceptanceCriteria() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#acceptanceMenu").clickOn("#acceptanceByFinalStateMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertTrue(mainStage.getAcceptanceByFinalStateMenuItem().isSelected());
    }
    @Test
    public void pressingAcceptByEmptyStackMenuItemShouldUpdateAcceptanceCriteria() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#acceptanceMenu").clickOn("#acceptanceByEmptyStackMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertTrue(mainStage.getAcceptanceByEmptyStackMenuItem().isSelected());
    }
    @Test
    public void pressingSimulationQuickRunShouldUpdateSimulationTypeToQuickRun() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByQuickRunMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertTrue(mainStage.getSimulationByQuickRunMenuItem().isSelected());
    }
    @Test
    public void pressingSimulationStepRunShouldUpdateSimulationTypeToStepRun() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByStepRunMenuItem");
        WaitForAsyncUtils.sleep(1000, TimeUnit.MILLISECONDS);
        assertTrue(mainStage.getSimulationByStepRunMenuItem().isSelected());
    }
    @Test
    public void checkHelpLabelShouldReturnGuide() throws Exception {
        assertEquals("Guide", mainStage.getMenuBar().getMenus().get(3).getItems().get(0).getText());
    }
}
