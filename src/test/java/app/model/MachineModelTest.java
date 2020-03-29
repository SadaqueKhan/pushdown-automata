package app.model;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;
public class MachineModelTest {
    private static MachineModel machineModel;
    @BeforeClass
    public static void instantiateMachinModelInstance() {
        machineModel = new MachineModel();
    }
    @After
    public void resetMachineModelInstance() {
        machineModel.getStateModelSet().clear();
        machineModel.getTransitionModelSet().clear();
        machineModel.getStackAlphabetSet().clear();
        machineModel.getInputAlphabetSet().clear();
    }
    @Test
    public void addingDuplicateStatesToTheMachineShouldBePrevented() {
        HashSet<StateModel> stateModelSet = new HashSet<>();
        machineModel.setStateModelSet(stateModelSet);
        StateModel stateModel1 = new StateModel("Q0");
        StateModel stateModel2 = new StateModel("Q0");
        machineModel.addStateModelToStateModelSet(stateModel1);
        machineModel.addStateModelToStateModelSet(stateModel2);
        assertEquals(1, machineModel.getStateModelSet().size());
    }
    @Test
    public void removingStatesShouldUpdateMachineStateSet() {
        StateModel stateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(stateModel);
        machineModel.removeStateModelFromStateModelSet(stateModel);
        assertEquals(0, machineModel.getStateModelSet().size());
    }
    @Test
    public void addingDuplicateTransitionsToTheMachineShouldBePrevented() {
        StateModel currentStateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(currentStateModel);
        StateModel resultingStateModel = new StateModel("Q1");
        machineModel.getStateModelSet().add(resultingStateModel);
        HashSet<TransitionModel> transitionModelSet = new HashSet<>();
        machineModel.setTransitionModelSet(transitionModelSet);
        TransitionModel transition1 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        TransitionModel transition2 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        machineModel.getTransitionModelSet().add(transition1);
        machineModel.addTransitionModelToTransitionModelSet(transition2);
        assertEquals(1, machineModel.getTransitionModelSet().size());
    }
    @Test
    public void removingTransitionsShouldUpdateMachineTransitionSet() {
        TransitionModel transition1 = new TransitionModel(new StateModel("Q0"), "A", "A", new StateModel("Q1"),
                "A");
        TransitionModel transition2 = new TransitionModel(new StateModel("Q1"), "A", "A", new StateModel("Q1"),
                "B");
        machineModel.getTransitionModelSet().add(transition1);
        machineModel.getTransitionModelSet().add(transition2);
        HashSet<TransitionModel> transitionModelSet = new HashSet<>();
        transitionModelSet.add(transition1);
        machineModel.removeTransitionModelsFromTransitionModelSet(transitionModelSet);
        assertEquals(1, machineModel.getTransitionModelSet().size());
    }
    @Test
    public void addingDuplicateInputSymbolToTheMachineShouldBePrevented() {
        HashSet<String> inputAlphabetSet = new HashSet<>();
        machineModel.setInputAlphabetSet(inputAlphabetSet);
        assertTrue(machineModel.getInputAlphabetSet().add("1"));
        assertTrue(machineModel.getInputAlphabetSet().add("2"));
        assertFalse(machineModel.getInputAlphabetSet().add("1"));
        assertFalse(machineModel.getInputAlphabetSet().add("2"));
    }
    @Test
    public void addingDuplicateStackSymbolShouldBePrevented() {
        HashSet<String> stackAlphabetSet = new HashSet<>();
        machineModel.setStackAlphabetSet(stackAlphabetSet);
        machineModel.getStackAlphabetSet().add("1");
        machineModel.getStackAlphabetSet().add("1");
        assertEquals(1, machineModel.getStackAlphabetSet().size());
    }
    @Test
    public void retrievalOfANonExistentStateShouldBePrevented() {
        StateModel stateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(stateModel);
        assertNull(machineModel.getStateModelFromStateModelSet("Q1"));
    }
    @Test
    public void retrievalOfAnExistingStateShouldSucceed() {
        StateModel stateModel = new StateModel("Q0");
        machineModel.addStateModelToStateModelSet(stateModel);
        StateModel stateModelToCheck = machineModel.getStateModelFromStateModelSet("Q0");
        assertEquals(stateModel, stateModelToCheck);
    }
    @Test
    public void requestingAcceptingCriteriaOfMachineShouldReturnAcceptByFinalState() {
        machineModel.setAcceptanceByFinalState(true);
        machineModel.setAcceptanceByEmptyStack(false);
        assertTrue(machineModel.isAcceptanceByFinalState());
        assertFalse(machineModel.isAcceptanceByEmptyStack());
    }
    @Test
    public void requestingAcceptingCriteriaOfMachineShouldReturnAcceptByEmptyStack() {
        machineModel.setAcceptanceByEmptyStack(true);
        machineModel.setAcceptanceByFinalState(false);
        assertTrue(machineModel.isAcceptanceByEmptyStack());
        assertFalse(machineModel.isAcceptanceByFinalState());
    }
    @Test
    public void requestingInitialStateShouldReturnTheInitialState() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setStartState(true);
        machineModel.addStateModelToStateModelSet(stateModel);
        StateModel stateModelToCheck = machineModel.getStartStateModel();
        assertTrue(stateModelToCheck.isStartState());
        assertEquals(stateModel, stateModelToCheck);
    }
    @Test
    public void requestingNonExistentInitialStateShouldReturnNull() {
        StateModel stateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(stateModel);
        assertEquals(null, machineModel.getStartStateModel());
    }
}
