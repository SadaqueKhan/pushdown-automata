package app.model;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
public class StackModelTest {
    @Test
    public void pushingAStackSymbolShouldResultInUpdatingTheStack() {
        StackModel stackModel = new StackModel();
        stackModel.push("s");
        assertEquals("s", stackModel.peak());
    }
    @Test
    public void poppingAStackSymbolShouldResultInUpdatingTheStack() {
        StackModel stackModel = new StackModel();
        stackModel.push("s");
        stackModel.push("y");
        stackModel.pop();
        assertEquals("s", stackModel.peak());
    }
    @Test
    public void checkingForAnEmptyStackShouldReturnTrue() {
        StackModel stackModel = new StackModel();
        assertTrue(stackModel.isEmpty());
    }
    @Test
    public void peakingShouldReturnAStackSymbol() {
        StackModel stackModel = new StackModel();
        stackModel.push("s");
        // Check when there does exist an element at the top of stack.
        assertEquals("s", stackModel.peak());
        // Check when there does not exist an element at the top of stack.
        stackModel.pop();
        assertEquals("ε", stackModel.peak());
    }
    @Test
    public void settingStackContentShouldContainNewElementsInTheStack() {
        StackModel stackModel = new StackModel();
        stackModel.push("s");
        ArrayList<String> stack = new ArrayList<>();
        stack.add("a");
        stack.add("b");
        stackModel.setContent(stack);
        for (String stackSymbols : stackModel.getContent()) {
            assertNotEquals("s", stackSymbols);
        }
    }
}
