package app.presenter;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.MainScene;
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
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
public class MainsStagePresenterTest extends ApplicationTest {
    private MainStagePresenter mainStagePresenter;
    private MainScene mainScene;
    private Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.mainStagePresenter = new MainStagePresenter();
        stage.setAlwaysOnTop(true);
        mainStagePresenter.start(stage);
        this.stage = stage;
        this.mainScene = mainStagePresenter.getMainScene();
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
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderAnInputWordOntoTextField() {
        clickOn(mainScene.getInputTextField());
        write("0011");
        assertEquals("0011", mainScene.getInputTextField().getText());
    }
    @Test
    public void clickingTableTabShouldRenderTransitionScene() {
        WaitForAsyncUtils.waitForFxEvents();
        clickOn(mainScene.getToggleTransitionTableButton());
        assertTrue(mainScene.getInputTextField().isDisabled());
    }
    @Test
    public void clickingDiagramTabShouldRenderDiagramScene() {
        clickOn(mainScene.getToggleDiagramButton());
        assertFalse(mainScene.getInputTextField().isDisabled());
    }
    @Test
    public void enteringInputWordIntoTextFieldWithNoStartStateDefinedShouldRenderAlert() {
        StateModel stateModel = mainStagePresenter.getMachineModel().getStartStateModel();
        stateModel.setStartState(false);
        String inputWordString = "0011";
        clickOn(mainScene.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        assertNull(mainStagePresenter.getMachineModel().getStartStateModel());
        stateModel.setStartState(true);
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderNewTapeScene() {
        String inputWordString = "0011";
        clickOn(mainScene.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(inputWordString.length(), mainScene.getTapeScene().getTapeViewHBoxContainer().getChildren()
                .size());
    }
    @Test
    public void enteringSpaceCharacterIntoTextFieldShouldNotBeAddedToFinalInputWord() {
        String inputWordString = "0011";
        clickOn(mainScene.getInputTextField());
        write(inputWordString);
        write(" ");
        write("\u03B5");
        press(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(mainScene.getInputTextField().getText(), inputWordString);
    }
    @Test
    public void updatingTapeSceneShouldRenderNewTapeScene() throws Exception {
        String inputWordString = "0011";
        clickOn(mainScene.getInputTextField());
        write(inputWordString);
        press(KeyCode.ENTER);
        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(
                () -> {
                    mainStagePresenter.updateTapeScene(1);
                    HBox tapeViewVBoxContainer = mainScene.getTapeScene().getTapeViewHBoxContainer();
                    StackPane headPointerStackPane = (StackPane) tapeViewVBoxContainer.getChildren().get(0);
                    Text tapeItemText = (Text) headPointerStackPane.getChildren().get(1);
                    Polygon tapeItemPointPolygon = (Polygon) headPointerStackPane.getChildren().get(2);
                    WaitForAsyncUtils.waitForFxEvents();
                    assertTrue(tapeItemPointPolygon.isVisible());
                    assertEquals("0", tapeItemText.getText());
                }
        );
        Platform.runLater(
                () -> {
                    mainStagePresenter.updateTapeScene(3);
                    HBox tapeViewVBoxContainer = mainScene.getTapeScene().getTapeViewHBoxContainer();
                    StackPane headPointerStackPane = (StackPane) tapeViewVBoxContainer.getChildren().get(2);
                    Text tapeItemText = (Text) headPointerStackPane.getChildren().get(1);
                    Polygon tapeItemPointPolygon = (Polygon) headPointerStackPane.getChildren().get(2);
                    WaitForAsyncUtils.waitForFxEvents();
                    assertTrue(tapeItemPointPolygon.isVisible());
                    assertEquals("1", tapeItemText.getText());
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
                    assertEquals(5, mainScene.getStackScene().getStackViewVBoxContainer().getChildren()
                            .size());
                }
        );
        Platform.runLater(
                () -> {
                    stackContent.clear();
                    mainStagePresenter.updateStackScene(stackContent);
                    assertEquals(1, mainScene.getStackScene().getStackViewVBoxContainer().getChildren()
                            .size());
                }
        );
    }
    @Test
    public void enteringInputWordIntoTextFieldShouldRenderSimulationStage() {
        String inputWordString = "0011";
        clickOn(mainScene.getInputTextField());
        write(inputWordString);
        assertNull(mainStagePresenter.getSimulationStagePresenter());
        press(KeyCode.ENTER);
        assertNotNull(mainStagePresenter.getSimulationStagePresenter());
    }
    @Test
    public void pressingAcceptByFinalStateMenuItemShouldUpdateAcceptanceCriteria() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#acceptanceMenu").clickOn("#acceptanceByFinalStateMenuItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(mainScene.getAcceptanceByFinalStateMenuItem().isSelected());
    }
    @Test
    public void pressingAcceptByEmptyStackMenuItemShouldUpdateAcceptanceCriteria() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#acceptanceMenu").clickOn("#acceptanceByEmptyStackMenuItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(mainScene.getAcceptanceByEmptyStackMenuItem().isSelected());
    }
    @Test
    public void pressingSimulationQuickRunShouldUpdateSimulationTypeToQuickRun() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByQuickRunMenuItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(mainScene.getSimulationByQuickRunMenuItem().isSelected());
    }
    @Test
    public void pressingSimulationStepRunShouldUpdateSimulationTypeToStepRun() throws Exception {
        FxRobot robot = new FxRobot();
        robot.clickOn("#simulationMenu").clickOn("#simulationByStepRunMenuItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(mainScene.getSimulationByStepRunMenuItem().isSelected());
    }
}
