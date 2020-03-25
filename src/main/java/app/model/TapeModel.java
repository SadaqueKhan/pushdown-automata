package app.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Implementation of the tape using a array list. Consisting of elements and methods commonly found to define a
 * tape in push down automata theory.
 * </p>
 */
class TapeModel {
    private List<String> inputTape;
    private int head;
    /**
     * Constructor that initialises a tape model.
     */
    TapeModel() {
        inputTape = new ArrayList<>();
        head = 0;
    }
    /**
     * Gets the input symbol in the input tape list at the corresponding index given by the head pointer
     * @return the input symbol item found at index of the value given by the head pointer or epsilon if the tape is
     * empty.
     */
    String getAtHead() {
        if (isEmpty()) {
            return "\u03B5";
        }
        return inputTape.get(head);
    }
    /**
     * Check if the tape is empty.
     * @return <tt>true</tt> if the tape is empty.
     */
    boolean isEmpty() {
        return inputTape.size() == head;
    }
    /**
     * Method used to read the symbol at the current head position on the tape, by incrementing the head pointer.
     */
    void readSymbol() {
        ++head;
    }
    /**
     * Get the value of the head pointer.
     * @return the value of the head pointer.
     */
    int getHead() {
        return head;
    }
    /**
     * Set the value of the head pointer.
     * @param head the value of the head for which is to be set.
     */
    void setHead(int head) {
        this.head = head;
    }
    /**
     * Method which returns the size of the input tape.
     * @return the size of the input tape.
     */
    int tapeSize() {
        return inputTape.size();
    }
    /**
     * Gets the this tape list.
     * @return the tape list.
     */
    List<String> getInputTape() {
        return inputTape;
    }
    /**
     * Method loads each of the input symbols from the string input word into the list data structure imitating a
     * tape in the pushdown automata theory.
     * @param input the string input word to be stored in the tape list data structure.
     */
    void loadInput(String input) {
        inputTape.clear();
        inputTape = Arrays.asList(input.split(""));
    }
}
