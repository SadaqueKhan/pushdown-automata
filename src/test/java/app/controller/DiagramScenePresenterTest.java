package app.controller;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.presenter.DiagramScenePresenter;
import app.presenter.MainStagePresenter;
import app.view.MainStage;
import app.view.StateNode;
import app.view.TransitionNode;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
public class DiagramScenePresenterTest extends ApplicationTest {
    private DiagramScenePresenter diagramScenePresenter;
    private MachineModel machineModel;
    private Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        MainStagePresenter mainStagePresenter = new MainStagePresenter();
        stage.setAlwaysOnTop(true);
        mainStagePresenter.start(stage);
        this.stage = stage;
        MainStage mainStage = mainStagePresenter.getMainStage();
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
        this.diagramScenePresenter = mainStagePresenter.getDiagramScenePresenter();
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
    public void deleteStateNodeShouldUpdateMachineModel() {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel stateModel = machineModel.getStateModelSet().stream().findFirst().orElse(null);
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#deleteStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertNull(machineModel.getStateModelFromStateModelSet(stateModel.getStateId()));
    }
    @Test
    public void addStateNodeShouldUpdateDiagramScene() {
        int nodesOnDiagramScenePriorToDelete = diagramScenePresenter.getDiagramScene().getChildren().size();
        FxRobot robot = new FxRobot();
        robot.clickOn(diagramScenePresenter.getDiagramScene());
        assertTrue(nodesOnDiagramScenePriorToDelete < diagramScenePresenter.getDiagramScene().getChildren().size());
    }
    @Test
    public void toggleStateToStartStateShouldUpdateMachineModel() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel startStateModel = machineModel.getStartStateModel();
        StateNode startStateNode = stateMap.get(startStateModel);
        startStateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(startStateNode).clickOn("#toggleStartStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(startStateModel.isStartState());
        robot.rightClickOn(startStateNode).clickOn("#toggleStartStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(startStateModel.isStartState());
    }
    @Test
    public void toggleStateToStartStateWhenStartStateAlreadyExistsShouldRenderAlert() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel notStartStateModel = machineModel.getStateModelSet().stream().filter(stateModel -> !stateModel.isStartState()).findFirst().orElse(null);
        StateNode notStartStateNode = stateMap.get(notStartStateModel);
        notStartStateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(notStartStateNode).clickOn("#toggleStartStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(stage.isFocused());
        robot.press(KeyCode.ESCAPE);
    }
    @Test
    public void toggleStateToFinalStateShouldUpdateMachineModel() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel stateModel = machineModel.getStateModelSet().stream().findFirst().orElse(null);
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#toggleFinalStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(stateModel.isFinalState());
        robot.rightClickOn(stateNode).clickOn("#toggleFinalStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(true, stateModel.isFinalState());
    }
    @Test
    public void toggleStateToStandardStateShouldUpdateMachineModel() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel stateModel = machineModel.getStateModelSet().stream().findFirst().orElse(null);
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#toggleStandardStateItem");
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(stateModel.isStartState());
        WaitForAsyncUtils.waitForFxEvents();
        assertFalse(stateModel.isFinalState());
    }
    @Test
    public void dynamicTransitionRenderingShouldUpdateMachineModelIfTransitionNotPresent() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        int sizeOfTransitionSetForMachineModelPriorToAddingTransition = machineModel.getTransitionModelSet().size();
        StateModel stateModel = machineModel.getStateModelSet().stream().findFirst().orElse(null);
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#createTransitionItem");
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
    public void dynamicTransitionShouldNotUpdateMachineModelIfTransitionPresent() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        int sizeOfTransitionSetPriorToAddingAtTransition = machineModel.getTransitionModelSet().size();
        TransitionModel transitionModel = machineModel.getTransitionModelSet().stream().findFirst().orElse(null);
        StateModel stateModel = transitionModel.getCurrentStateModel();
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#createTransitionItem");
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
    public void dynamicTransitionShouldNotUpdateMachineModelIfAllFieldsAreNotFilled() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        int sizeOfTransitionSetPriorToAddingAtTransition = machineModel.getTransitionModelSet().size();
        TransitionModel transitionModel = machineModel.getTransitionModelSet().stream().findFirst().orElse(null);
        StateModel stateModel = transitionModel.getCurrentStateModel();
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        FxRobot robot = new FxRobot();
        robot.rightClickOn(stateNode).clickOn("#createTransitionItem");
        robot.clickOn("#inputSymbolComboBox").write("");
        robot.clickOn("#stackSymbolToPopComboBox").write("");
        robot.clickOn("#resultingStateComboBox").write("");
        robot.clickOn("#stackSymbolToPushComboBox").write("");
        robot.clickOn("#submitTransitionButton");
        robot.press(KeyCode.ESCAPE);
        WaitForAsyncUtils.waitForFxEvents();
        int sizeOfTransitionSetForMachineModelAfterAddingTransition = machineModel.getTransitionModelSet().size();
        assertTrue(sizeOfTransitionSetPriorToAddingAtTransition == sizeOfTransitionSetForMachineModelAfterAddingTransition);
    }
    @Test
    public void draggingStateNodeShouldUpdateStateModelCoordinate() throws Exception {
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateModel stateModel = machineModel.getStateModelSet().stream().findFirst().orElse(null);
        StateNode stateNode = stateMap.get(stateModel);
        stateNode.relocate(50, 50);
        double stateModelXCoordinateOnDiagramBeforeDragging = stateModel.getXCoordinateOnDiagram();
        FxRobot robot = new FxRobot();
        robot.drag(stateNode, MouseButton.PRIMARY).dropBy(100, 100);
        assertNotEquals(stateModelXCoordinateOnDiagramBeforeDragging, stateModel.getXCoordinateOnDiagram());
    }
    @Test
    public void draggingDirectedTransitionsShouldUpdateTransitionModelCoordinate() throws Exception {
        TransitionModel transitionModel = machineModel.getTransitionModelSet().stream().findFirst().orElse(null);
        HashSet<Node> transitionViewSet = diagramScenePresenter.retrieveDirectionalTransitionView(transitionModel);
        VBox transitionListVBox = null;
        for (Node node : transitionViewSet) {
            if (node instanceof TransitionNode) {
                TransitionNode transitionNodeToUpdate = (TransitionNode) node;
                transitionListVBox = transitionNodeToUpdate.getTransitionsListVBox();
            }
        }
        transitionListVBox.relocate(50, 50);
        double transitionModelXCoordinateOnDiagramBeforeDragging = transitionModel.getXCoordinateOnDiagram();
        FxRobot robot = new FxRobot();
        robot.drag(transitionListVBox, MouseButton.PRIMARY).dropBy(100, 100);
        assertNotEquals(transitionModelXCoordinateOnDiagramBeforeDragging, transitionModel.getXCoordinateOnDiagram());
    }
    @Test
    public void draggingReflexiveTransitionsShouldUpdateTransitionModelCoordinate() throws Exception {
        TransitionModel transitionModel = machineModel.getTransitionModelSet().stream().filter(transitionModelToFind -> transitionModelToFind.getCurrentStateModel().equals(transitionModelToFind.getResultingStateModel())).findFirst().orElse(null);
        Map<StateModel, StateNode> stateMap = diagramScenePresenter.getStateMap();
        StateNode stateNode = stateMap.get(transitionModel.getCurrentStateModel());
        VBox transitionListVBox = stateNode.getTransitionsListVBox();
        FxRobot robot = new FxRobot();
        transitionListVBox.relocate(50, 50);
        double transitionModelXCoordinateOnDiagramBeforeDragging = transitionModel.getXCoordinateOnDiagram();
        robot.drag(transitionListVBox, MouseButton.PRIMARY).dropBy(100, 100);
        assertNotEquals(transitionModelXCoordinateOnDiagramBeforeDragging, transitionModel.getXCoordinateOnDiagram());
    }
}
