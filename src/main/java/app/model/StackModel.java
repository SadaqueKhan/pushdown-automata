package app.model;
import java.util.ArrayList;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Implementation of the stack using a array list.
 * </p>
 */
class StackModel {
    private ArrayList<String> stack;
    StackModel() {
        stack = new ArrayList<>();
    }
    /**
     * Inserts an input symbol at the top of the stack.
     * This method runs in O(1) time.
     * @param toPush input symbol to be inserted.
     */
    void push(String toPush) {
        stack.add(toPush);
    }
    /**
     * Removes the top element from the stack.
     * This method runs in O(1) time.
     */
    void pop() {
        if (!(isEmpty())) {
            stack.remove(stack.size() - 1);
        }
    }
    /**
     * Testes whether the stack is empty.
     * This method runs in O(1) time.
     * @return <tt>true</tt>  if the stack is empty, false otherwise.
     */
    boolean isEmpty() {
        return stack.isEmpty();
    }
    /**
     * Index of the top element of the stack in the array.
     */
    String peak() {
        if (isEmpty()) {
            return "\u03B5";
        }
        return stack.get(stack.size() - 1);
    }
    /**
     * Gets the stack array list.
     * @return the stack array list.
     */
    ArrayList<String> getContent() {
        return stack;
    }
    /**
     * Sets the stack array list for this stack.
     * @param stack array list to be set for this stack array list.
     */
    void setContent(ArrayList<String> stack) {
        this.stack.clear();
        this.stack.addAll(stack);
    }
}
