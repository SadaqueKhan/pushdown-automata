package app.model;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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
    public void machineModelShouldContainOnlyOneInitialState() {
        ArrayList<StateModel> numOfInitialState = new ArrayList<>();

        StateModel initialState1 = new StateModel("Q0");
        initialState1.setStartState(true);
        machineModel.getStateModelSet().add(initialState1);
        StateModel initialState2 = new StateModel("Q1");
        machineModel.getStateModelSet().add(initialState2);

        for (StateModel stateModel : machineModel.getStateModelSet()) {
            if (stateModel.isStartState()) {
                numOfInitialState.add(stateModel);
            }
        }
        assertEquals(1, numOfInitialState.size());
    }

    @Test
    public void addingDuplicateStatesShouldBePrevented() {
        StateModel stateModel1 = new StateModel("Q0");
        StateModel stateModel2 = new StateModel("Q0");

        machineModel.getStateModelSet().add(stateModel1);
        machineModel.getStateModelSet().add(stateModel2);

        assertEquals(1, machineModel.getStateModelSet().size());
    }

    @Test
    public void addingDuplicateTransitionsShouldBePrevented() {
        StateModel currentStateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(currentStateModel);
        StateModel resultingStateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(resultingStateModel);


        TransitionModel transition1 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        TransitionModel transition2 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");

        machineModel.getTransitionModelSet().add(transition1);
        machineModel.getTransitionModelSet().add(transition2);

        assertEquals(1, machineModel.getTransitionModelSet().size());
    }






}
