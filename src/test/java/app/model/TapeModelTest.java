package app.model;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
public class TapeModelTest {
    @Test
    public void readingSymbolFromTheTapeShouldResultInTheHeadUpdating() {
        TapeModel tapeModel = new TapeModel();
        tapeModel.loadInput("test");
        tapeModel.readSymbol();
        assertEquals(1, tapeModel.getHead());
    }
    @Test
    public void retrievingAInputSymbolFromTheTapeShouldResultInAInputSymbolToBeReturned() {
        TapeModel tapeModel = new TapeModel();
        tapeModel.loadInput("test");
        assertEquals("t", tapeModel.getAtHead());
        tapeModel.readSymbol();
        assertEquals("e", tapeModel.getAtHead());
        tapeModel.readSymbol();
        assertEquals("s", tapeModel.getAtHead());
        tapeModel.readSymbol();
        assertEquals("t", tapeModel.getAtHead());
        tapeModel.readSymbol();
        assertEquals("ε", tapeModel.getAtHead());
    }
    @Test
    public void settingHeadShouldReturnANewElementAtTheHead() {
        TapeModel tapeModel = new TapeModel();
        tapeModel.loadInput("test");
        assertEquals("t", tapeModel.getAtHead());
        tapeModel.setHead(2);
        assertEquals("s", tapeModel.getAtHead());
    }
    @Test
    public void loadingTapeContentWithTheInputWordShouldContainInputSymbolsInTheTape() {
        String inputWord = "test";
        TapeModel tapeModel = new TapeModel();
        tapeModel.loadInput(inputWord);
        assertNotEquals(0, tapeModel.tapeSize());
        assertFalse(tapeModel.isEmpty());
        for (String inputSymbol : tapeModel.getInputTape()) {
            assertThat(inputWord, containsString(inputSymbol));
        }
    }
}
