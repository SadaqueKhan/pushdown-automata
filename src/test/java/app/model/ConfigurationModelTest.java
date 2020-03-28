package app.model;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
public class ConfigurationModelTest {
    private static MachineModel machineModel;
    private static SimulationModel simulationModel;
    @BeforeClass
    public static void instantiateMachinModelInstance() {
        machineModel = new MachineModel();
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
    public void resetMachineModelInstance() {
        machineModel.getStateModelSet().clear();
        machineModel.getTransitionModelSet().clear();
        machineModel.getStackAlphabetSet().clear();
        machineModel.getInputAlphabetSet().clear();
    }
    @Test
    public void parentConfigurationOfRootConfigurationShouldBeNull() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertNull(simulationModel.getCurrentConfig().getParentConfiguration());
    }
    @Test
    public void rootConfigurationDepthIdShouldBeZero() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertEquals(0, simulationModel.getCurrentConfig().getDepth());
    }
    @Test
    public void rootConfigurationHeadPositionOnTapeShouldBeZero() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertEquals(0, simulationModel.getCurrentConfig().getHeadPosition());
    }
    @Test
    public void rootConfigurationHeadStackContentShouldBeEmpty() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertTrue(simulationModel.getCurrentConfig().getStackContent().isEmpty());
    }
    @Test
    public void rootConfigurationShouldNotBeSuccessConfig() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertFalse(simulationModel.getCurrentConfig().isSuccessConfig());
    }
    @Test
    public void rootConfigurationShouldNotBeFailConfig() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertFalse(simulationModel.getCurrentConfig().isFailConfig());
    }
    @Test
    public void rootConfigurationShouldNotBeStuckConfig() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertFalse(simulationModel.getCurrentConfig().isStuckConfig());
    }
}
