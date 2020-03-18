package app.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        StateModel stateModel = new StateModel();
        assertTrue(stateModel.getStateId().startsWith("Q"));
    }


    @Test
    public void requestingXCoordinateShouldReturnSpecifiedXCoordinateOfStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setxCoordinateOnDiagram(10.0);
        assertEquals("X coordinate of ", 10.0, stateModel.getxCoordinateOnDiagram(), 10.0);
    }

    @Test
    public void requestingYCoordinateShouldReturnSpecifiedYCoordinateOfStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setyCoordinateOnDiagram(10.0);
        assertEquals("X coordinate of ", 10.0, stateModel.getyCoordinateOnDiagram(), 10.0);
    }

    @Test
    public void twoStatesWithSameIdShouldBeEqual() {
        StateModel state1 = new StateModel("Q0");
        StateModel state2 = new StateModel("Q0");
        assertEquals(state1, state2);
        assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    public void requestingIdOfStateModelShouldReturnSpecifiedIdOfStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setStateId("Q1");
        assertEquals("Q1", stateModel.getStateId());
    }


}