package app.model;

import java.util.ArrayList;

public class Stack {

    private ArrayList<String> stack;

    public Stack() {
        stack = new ArrayList<>();
    }

    public void push(String toPush) {
        stack.add(toPush);
    }

    public void pop() {
        if (!(stack.isEmpty())) {
            stack.remove(stack.size() - 1);
        }
    }

    public String peak() {
        if (!(stack.isEmpty())) {
            return stack.get(stack.size() - 1);
        }

        return "\u03B5";
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
