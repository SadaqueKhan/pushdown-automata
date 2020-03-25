package app.model;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;
public class TransitionModelTest {
    @BeforeClass
    public static void instantiateTransitionInstance() {
        transition = new MachineModel();
    }
    @Test
    public void requestingCurrentStateElementOfATransitionShouldReturnSpecifiedCurrentState() {
        TransitionModel stateModel = new StateModel("Q0");
        StateModel currentStateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(currentStateModel);
        StateModel resultingStateModel = new StateModel("Q0");
        machineModel.getStateModelSet().add(resultingStateModel);
        HashSet<TransitionModel> transitionModelSet = new HashSet<>();
        machineModel.setTransitionModelSet(transitionModelSet);
        TransitionModel transition1 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        TransitionModel transition2 = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        stateModel.setXCoordinateOnDiagram(10.0);
        assertEquals("X coordinate of ", 10.0, stateModel.getXCoordinateOnDiagram(), 10.0);
    }
}
