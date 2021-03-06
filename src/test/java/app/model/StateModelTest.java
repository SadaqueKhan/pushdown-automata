package app.model;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
public class StateModelTest {
    @Test
    public void stateShouldReturnTrueForIsStartStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setStartState(true);
        assertTrue(stateModel.isStartState());
    }
    @Test
    public void stateShouldReturnTrueForIsFinalState() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setFinalState(true);
        assertTrue(stateModel.isFinalState());
    }
    @Test
    public void stateShouldReturnTrueForIsStartStateAndIsFinalState() {
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
        stateModel.setXCoordinateOnDiagram(10.0);
        assertEquals("X coordinate of ", 10.0, stateModel.getXCoordinateOnDiagram(), 10.0);
    }
    @Test
    public void requestingYCoordinateShouldReturnSpecifiedYCoordinateOfStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setYCoordinateOnDiagram(10.0);
        assertEquals("Y coordinate of ", 10.0, stateModel.getYCoordinateOnDiagram(), 10.0);
    }
    @Test
    public void twoStatesWithSameIdShouldBeEqual() {
        StateModel stateModel1 = new StateModel("Q0");
        StateModel stateModel2 = new StateModel("Q0");
        assertEquals(stateModel1, stateModel2);
        assertEquals(stateModel1.hashCode(), stateModel2.hashCode());
    }
    @Test
    public void requestingIdOfStateModelShouldReturnSpecifiedIdOfStateModel() {
        StateModel stateModel = new StateModel("Q0");
        stateModel.setStateId("Q1");
        assertEquals("Q1", stateModel.getStateId());
    }
    @Test
    public void requestingObjectToStringShouldStateTheIdOfAState() {
        StateModel stateModel = new StateModel("Q0");
        assertThat(stateModel.toString(), containsString("Q0"));
    }
}
