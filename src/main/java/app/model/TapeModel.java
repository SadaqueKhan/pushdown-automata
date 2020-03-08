package app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TapeModel {

    private List<String> inputTape;
    private int head;

    public TapeModel() {
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

    public boolean isEmpty() {
        return inputTape.size() == head;
    }

    public int tapeSize() {
        return inputTape.size();
    }

    public List<String> getInputTape() {
        return inputTape;
    }

    public void loadInput(String input) {
        inputTape.clear();
        inputTape = Arrays.asList(input.split(""));
    }
}
