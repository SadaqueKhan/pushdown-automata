package app.models;

import java.util.ArrayList;

public class Stack {

    private int top;
    private ArrayList<String> stack;

    public Stack() {
        top = -1;
        stack = new ArrayList<>();
    }

    public void push(String toPush) {
        stack.add(toPush);
        ++top;
    }

    public void pop() {
        if (top != -1) {
            stack.remove(top);
            --top;
        }
    }

    public String peak() {
        return top == -1 ? "\u03B5" : stack.get(top);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public ArrayList<String> getContent() {
        return stack;
    }

    public void setContent(ArrayList<String> stack) {
        this.stack = stack;
    }


}
