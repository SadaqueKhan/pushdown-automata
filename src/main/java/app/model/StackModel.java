package app.model;

import java.util.ArrayList;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a transition, consisting of elements commonly found to define a stack in push down automata theory.
 * </p>
 */
public class StackModel {

    private ArrayList<String> stack;

    public StackModel() {
        stack = new ArrayList<>();
    }

    public void push(String toPush) {
        stack.add(toPush);
    }

    public void pop() {
        if (!(isEmpty())) {
            stack.remove(stack.size() - 1);
        }
    }

    public String peak() {
        if (isEmpty()) {
            return "\u03B5";
        }
        return stack.get(stack.size() - 1);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public ArrayList<String> getContent() {
        return stack;
    }

    public void setContent(ArrayList<String> stack) {
        this.stack.clear();
        this.stack.addAll(stack);
    }


}
