package app.model;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
public class TransitionModelTest {
    @Test
    public void requestingCurrentStateElementOfATransitionShouldReturnCurrentState() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        assertEquals("Q0", transitionModel.getCurrentStateModel().getStateId());
    }
    @Test
    public void settingCurrentStateElementOfATransitionShouldSucceed() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        StateModel newCurrentStateModel = new StateModel("Q3");
        transitionModel.setCurrentStateModel(newCurrentStateModel);
        assertEquals("Q3", transitionModel.getCurrentStateModel().getStateId());
    }
    @Test
    public void requestingInputSymbolOfATransitionShouldReturnInputSymbol() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        assertEquals("A", transitionModel.getInputSymbol());
    }
    @Test
    public void settingInputSymbolOfATransitionShouldSucceed() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        transitionModel.setInputSymbol("B");
        assertEquals("B", transitionModel.getInputSymbol());
    }
    @Test
    public void requestingStackSymbolToPopOfATransitionShouldReturnStackSymbolToPop() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        assertEquals("A", transitionModel.getStackSymbolToPop());
    }
    @Test
    public void settingStackSymbolToPopOfATransitionShouldSucceed() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        transitionModel.setStackSymbolToPop("B");
        assertEquals("B", transitionModel.getStackSymbolToPop());
    }
    @Test
    public void requestingStackSymbolToPushOfATransitionShouldReturnStackSymbolToPush() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        assertEquals("A", transitionModel.getStackSymbolToPush());
    }
    @Test
    public void settingStackSymbolToPushOfATransitionShouldSucceed() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        transitionModel.setStackSymbolToPush("B");
        assertEquals("B", transitionModel.getStackSymbolToPush());
    }
    @Test
    public void requestingResultingStateElementOfATransitionShouldReturnResultingState() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        assertEquals("Q1", transitionModel.getResultingStateModel().getStateId());
    }
    @Test
    public void settingResultingStateElementOfATransitionShouldSucceed() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        StateModel newResultingStateModel = new StateModel("Q2");
        transitionModel.setResultingStateModel(newResultingStateModel);
        assertEquals("Q2", transitionModel.getResultingStateModel().getStateId());
    }
    @Test
    public void requestingATransitionWithNoConstructorShouldBePrevented() {
        TransitionModel transitionModel = new TransitionModel();
        assertNull(transitionModel.getCurrentStateModel());
        assertNull(transitionModel.getInputSymbol());
        assertNull(transitionModel.getStackSymbolToPop());
        assertNull(transitionModel.getResultingStateModel());
        assertNull(transitionModel.getStackSymbolToPush());
    }
    @Test
    public void requestingXCoordinateShouldReturnSpecifiedXCoordinateOfStateModel() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        transitionModel.setXCoordinateOnDiagram(10.0);
        assertEquals("X coordinate of ", 10.0, transitionModel.getXCoordinateOnDiagram(), 10.0);
    }
    @Test
    public void requestingYCoordinateShouldReturnSpecifiedYCoordinateOfStateModel() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "A", resultingStateModel, "A");
        transitionModel.setYCoordinateOnDiagram(10.0);
        assertEquals("Y coordinate of ", 10.0, transitionModel.getYCoordinateOnDiagram(), 10.0);
    }
    @Test
    public void toStringShouldReturnTransitionAttributes() {
        StateModel currentStateModel = new StateModel("Q0");
        StateModel resultingStateModel = new StateModel("Q1");
        TransitionModel transitionModel = new TransitionModel(currentStateModel, "A", "B", resultingStateModel, "C");
        String[] terms = {currentStateModel.getStateId(), "A", "B", resultingStateModel.getStateId(), "C"};
        for (String term : terms) {
            assertThat(transitionModel.toString(), containsString(term));
        }
    }
}
