package app.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StateModelTest {

    @Test
    public void acceptingStateShouldReturnTrueForIsStartStateModel() {
        StateModel acceptingStateModel = new StateModel("Q0");
        acceptingStateModel.setStartState(true);
        assertTrue(acceptingStateModel.isStartState());
    }

    @Test
    public void acceptingStateShouldReturnTrueForIsFinalState() {
        StateModel acceptingStateModel = new StateModel("Q0");
        acceptingStateModel.setFinalState(true);
        assertTrue(acceptingStateModel.isFinalState());
    }

    @Test
    public void acceptingStateShouldReturnTrueForIsStartStateAndIsFinalState() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setStartState(true);
        stateModel.setFinalState(true);
        assertTrue(stateModel.isStartState());
        assertTrue(stateModel.isFinalState());
    }

    @Test
    public void stateWithNoIdSpecifiedShouldReturnAnIdStartingWithQ() {
        String label = "Q3";
        StateModel stateModel = new StateModel();
        assertTrue(stateModel.getStateId().startsWith("Q"));
    }


}
