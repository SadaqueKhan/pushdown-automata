package app.presenter;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.MainScene;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
public class TransitionTableScenePresenterTest extends ApplicationTest {
    private TransitionTableScenePresenter transitionTableScenePresenter;
    private MachineModel machineModel;
    private Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        MainStagePresenter mainStagePresenter = new MainStagePresenter();
        stage.setAlwaysOnTop(true);
        mainStagePresenter.start(stage);
        this.stage = stage;
        MainScene mainScene = mainStagePresenter.getMainScene();
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
        this.transitionTableScenePresenter = mainStagePresenter.getTransitionTableScenePresenter();
        transitionTableScenePresenter.loadTransitionTableView();
        DiagramScenePresenter diagramScenePresenter = mainStagePresenter.getDiagramScenePresenter();
        diagramScenePresenter.loadStatesOntoDiagram();
        diagramScenePresenter.loadTransitionsOntoDiagram();
        mainStagePresenter.loadTransitionTableScene();
    }
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    @Test
    public void addingTransitionShouldUpdateMachineModelIfTransitionNotPresent() throws Exception {
        int sizeOfTransitionSetForMachineModelPriorToAddingTransition = machineModel.getTransitionModelSet().size();
        FxRobot robot = new FxRobot();
        robot.clickOn("#currentStateComboBox").write("Q23");
        robot.clickOn("#inputSymbolComboBox").write("A");
        robot.clickOn("#stackSymbolToPopComboBox").write("ABA");
        robot.clickOn("#resultingStateComboBox").write("Q20");
        robot.clickOn("#stackSymbolToPushComboBox").write("A");
        robot.clickOn("#submitTransitionButton");
        WaitForAsyncUtils.waitForFxEvents();
        int sizeOfTransitionSetForMachineModelAfterAddingTransition = machineModel.getTransitionModelSet().size();
        assertTrue(sizeOfTransitionSetForMachineModelPriorToAddingTransition < sizeOfTransitionSetForMachineModelAfterAddingTransition);
    }
    @Test
    public void addingTransitionShouldNotUpdateMachineModelIfTransitionPresent() throws Exception {
        int sizeOfTransitionSetPriorToAddingAtTransition = machineModel.getTransitionModelSet().size();
        TransitionModel transitionModel = machineModel.getTransitionModelSet().stream().findFirst().orElse(null);
        FxRobot robot = new FxRobot();
        robot.clickOn("#currentStateComboBox").write(transitionModel.getCurrentStateModel().getStateId());
        robot.clickOn("#inputSymbolComboBox").write(transitionModel.getInputSymbol());
        robot.clickOn("#stackSymbolToPopComboBox").write(transitionModel.getStackSymbolToPop());
        robot.clickOn("#resultingStateComboBox").write(transitionModel.getResultingStateModel().getStateId());
        robot.clickOn("#stackSymbolToPushComboBox").write(transitionModel.getStackSymbolToPush());
        robot.clickOn("#submitTransitionButton");
        robot.press(KeyCode.ESCAPE);
        WaitForAsyncUtils.waitForFxEvents();
        int sizeOfTransitionSetForMachineModelAfterAddingTransition = machineModel.getTransitionModelSet().size();
        assertTrue(sizeOfTransitionSetPriorToAddingAtTransition == sizeOfTransitionSetForMachineModelAfterAddingTransition);
    }
    @Test
    public void deleteTransitionShouldUpdateMachineModel() throws Exception {
        int sizeOfTransitionSetForMachineModelPriorToAddingTransition = machineModel.getTransitionModelSet().size();
        TableView tableView = transitionTableScenePresenter.getTransitionTableScene().getTransitionTable();
        tableView.getSelectionModel().selectFirst();
        FxRobot robot = new FxRobot();
        robot.clickOn("#deleteTransitionButton");
        int sizeOfTransitionSetForMachineModelAfterAddingTransition = machineModel.getTransitionModelSet().size();
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(sizeOfTransitionSetForMachineModelPriorToAddingTransition >
                sizeOfTransitionSetForMachineModelAfterAddingTransition);
    }
}
