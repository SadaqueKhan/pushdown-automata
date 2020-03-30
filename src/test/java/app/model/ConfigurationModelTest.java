package app.model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
public class ConfigurationModelTest {
    private static MachineModel machineModel;
    @Before
    public void instantiateMachineModelInstance() {
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
    public void rootConfigurationBranchIdShouldBeOne() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        assertEquals(1, simulationModel.getCurrentConfig().getBranch());
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
    @Test
    public void rootConfigurationShouldPathListShouldNotExceedOne() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        ArrayList<ConfigurationModel> path = simulationModel.getCurrentConfig().getPath();
        assertEquals(1, path.size());
    }
    @Test
    public void anyOtherConfigurationInAComputationTreeOtherThenRootPathListShouldExceedOne() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        simulationModel.createTree();
        ConfigurationModel anyOtherConfigurationExceptRootConfiguration = simulationModel.getComputationArrayList()
                .stream().filter(config -> (config.getParentConfiguration() != null)).findFirst().orElse(null);
        ArrayList<ConfigurationModel> path = anyOtherConfigurationExceptRootConfiguration.getPath();
        assertTrue(1 < path.size());
    }
    @Test
    public void requestingObjectToStringShouldContainElementsThatMakeUpARootConfiguration() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        ConfigurationModel configurationModel = simulationModel.getCurrentConfig();
        String[] attributes = {configurationModel.getCurrentStateModel().getStateId(), "0011", "\u03B5"};
        for (String term : attributes) {
            assertThat(configurationModel.toString(), containsString(term));
        }
    }
    @Test
    public void requestingToStringOfObjectShouldReturnTapeElementPartOfConfigurationStringAsEmpty() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        simulationModel.createTree();
        ConfigurationModel configurationModelWithEmptyTape = simulationModel.getComputationArrayList()
                .stream().filter(ConfigurationModel::isSuccessConfig).findFirst().orElse(null);
        assertThat(configurationModelWithEmptyTape.toString(), containsString("\u03B5"));
    }
    @Test
    public void comparingObjectToStringShouldNotMatch() {
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        ConfigurationModel rootConfigurationModel = simulationModel.getCurrentConfig();
        simulationModel.createTree();
        ConfigurationModel anyOtherConfigurationExceptRootConfiguration = simulationModel.getComputationArrayList()
                .stream().filter(config -> (config != rootConfigurationModel)).findFirst().orElse(null);
        assertNotEquals(anyOtherConfigurationExceptRootConfiguration.toString(), rootConfigurationModel.toString());
    }
    @Test
    public void infiniteConfigShouldExistWithMachineWithCycle() {
        machineModel.getStateModelSet().clear();
        machineModel.getTransitionModelSet().clear();
        machineModel.getStackAlphabetSet().clear();
        machineModel.getInputAlphabetSet().clear();
        StateModel stateModelQ0 = new StateModel();
        stateModelQ0.setStartState(true);
        stateModelQ0.setFinalState(true);
        machineModel.addStateModelToStateModelSet(stateModelQ0);
        TransitionModel transitionModel1 = new TransitionModel(stateModelQ0, "\u03B5", "\u03B5", stateModelQ0, "\u03B5");
        machineModel.addTransitionModelToTransitionModelSet(transitionModel1);
        SimulationModel simulationModel = new SimulationModel(machineModel, "0011");
        simulationModel.createTree();
        ConfigurationModel checkConfigIsInfinite = simulationModel.getComputationArrayList()
                .stream().filter(config -> (config.isInfiniteConfig())).findFirst().orElse(null);
        assertTrue(checkConfigIsInfinite.isInfiniteConfig());
    }
}
