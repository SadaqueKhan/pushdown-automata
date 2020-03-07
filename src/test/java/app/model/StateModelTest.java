package app.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StateModelTest {

    @Test
    public void shouldWork() {
        StateModel state = new StateModel();
        assertTrue(state.isFinalState());
        assertFalse(state.isStartState());
    }
}
