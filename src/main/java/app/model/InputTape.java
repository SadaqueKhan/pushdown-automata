package app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputTape {

    private List<String> inputTape;
    private int head;

    public InputTape() {
        inputTape = new ArrayList<>();
        head = 0;
    }

    public String getAtHead() {
        if (isEmpty()) {
            return "\u03B5";
        }
        return inputTape.get(head);
    }

    public void readSymbol() {
        ++head;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void loadInput(String input) {
        inputTape.clear();
        inputTape = Arrays.asList(input.split(""));
    }

    public boolean isEmpty() {
        return inputTape.size() == head;
    }

    public String printCurrentState(int currentHead) {
        if (isEmpty()) {
            return "\u03B5";
        }

        if (currentHead == inputTape.size()) {
            return "\u03B5";
        }
        StringBuilder toReturn = new StringBuilder();

        for (int i = currentHead; i < inputTape.size(); ++i) {
            toReturn.append(inputTape.get(i));
        }

        return toReturn.toString();
    }

}
