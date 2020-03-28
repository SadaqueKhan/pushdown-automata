package app.model;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a machine, consisting of elements commonly found to define a machine in push down automata theory
 * </p>
 */
@XmlRootElement
public class MachineModel {
    private HashSet<String> inputAlphabetSet;
    private HashSet<String> stackAlphabetSet;
    private HashSet<StateModel> stateModelSet;
    private HashSet<TransitionModel> transitionModelSet;
    private boolean isAcceptanceByFinalState = true;
    private boolean isAcceptanceByEmptyStack = false;
    /**
     * Constructor that initialises a machine model by the specifying the elements commonly found to define a
     * machine in a pushdown automata theory.
     */
    public MachineModel() {
        this.inputAlphabetSet = new HashSet<>();
        inputAlphabetSet.add("\u03B5");
        this.stackAlphabetSet = new HashSet<>();
        stackAlphabetSet.add("\u03B5");
        this.stateModelSet = new HashSet<>();
        this.transitionModelSet = new HashSet<>();
    }
    /**
     * Adds the given state into the state set for this machine.
     * @param newStateModel to be added to the state set for this machine.
     */
    public void addStateModelToStateModelSet(StateModel newStateModel) {
        stateModelSet.add(newStateModel);
    }
    /**
     * Removes the given state from state set for this machine.
     * @param stateModelToRemove to remove from the state set for this machine.
     */
    public void removeStateModelFromStateModelSet(StateModel stateModelToRemove) {
        stateModelSet.remove(stateModelToRemove);
    }
    /**
     * Adds the given transition into the state set for this machine.
     * @param newTransitionModel to be added to the transition set for this machine.
     */
    public void addTransitionModelToTransitionModelSet(TransitionModel newTransitionModel) {
        transitionModelSet.add(newTransitionModel);
    }
    /**
     * Removes the given transition from state set for this machine.
     * @param setOfTransitionsToRemove to remove from the transition set for this machine.
     */
    public void removeTransitionModelsFromTransitionModelSet(HashSet<TransitionModel> setOfTransitionsToRemove) {
        transitionModelSet.removeAll(setOfTransitionsToRemove);
    }
    /**
     * Retrieves the state for a given id in the state set.
     * @param stateId of the state to retrieved from the state set.
     * @return the instance of the state with the specified id found in the state set.
     */
    public StateModel getStateModelFromStateModelSet(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return stateModel;
            }
        }
        return null;
    }
    /**
     * Gets this machines initial state.
     * @return this machines initial {@code StateModel}  or {@code null}
     */
    public StateModel getStartStateModel() {
        for (StateModel isStartStateModel : stateModelSet) {
            if (isStartStateModel.isStartState()) {
                return isStartStateModel;
            }
        }
        return null;
    }
    /**
     * Gets all the states that are a part of this machine.
     * @return {@code HashSet<StateModel>} of this machines states.
     */
    public HashSet<StateModel> getStateModelSet() {
        return stateModelSet;
    }
    /**
     * Sets the state set for this machine to given set.
     * @param stateModelSet used to set the state set for this machine.
     */
    public void setStateModelSet(HashSet<StateModel> stateModelSet) {
        this.stateModelSet = stateModelSet;
    }
    /**
     * Gets all the transitions that are a part of this machine.
     * @return {@code HashSet<TransitionModel>} of this machines transitions.
     */
    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }
    /**
     * Sets the transition set for this machine to given set.
     * @param transitionModelSet used to set the transition set for this machine.
     */
    public void setTransitionModelSet(HashSet<TransitionModel> transitionModelSet) {
        this.transitionModelSet = transitionModelSet;
    }
    /**
     * Gets all the input symbols that are a part of this machine.
     * @return {@code HashSet<String>} of this machines states.
     */
    @XmlAttribute
    public HashSet<String> getInputAlphabetSet() {
        return inputAlphabetSet;
    }
    /**
     * Sets the input alphabet set for this machine to given set.
     * @param inputAlphabetSet used to set the input alphabet set for this machine.
     */
    public void setInputAlphabetSet(HashSet<String> inputAlphabetSet) {
        this.inputAlphabetSet = inputAlphabetSet;
    }
    /**
     * Gets all the stack symbols that are a part of this machine.
     * @return {@code HashSet<String>} of this machines states.
     */
    @XmlAttribute
    public HashSet<String> getStackAlphabetSet() {
        return stackAlphabetSet;
    }
    /**
     * Sets the stack alphabet set for this machine to given set.
     * @param stackAlphabetSet used to set the stack alphabet set for this machine.
     */
    public void setStackAlphabetSet(HashSet<String> stackAlphabetSet) {
        this.stackAlphabetSet = stackAlphabetSet;
    }
    /**
     * Check if acceptance criteria for this machine is accept by final state.
     * @return <tt>true</tt> if the criteria is accept by final state, otherwise false.
     */
    @XmlAttribute
    public boolean isAcceptanceByFinalState() {
        return isAcceptanceByFinalState;
    }
    /**
     * Set the acceptance criteria of this machine to accept by final state or to not accept by final state.
     * @param acceptanceByFinalState used to set the acceptance criteria to accept final state or to not accept by
     * final state.
     */
    public void setAcceptanceByFinalState(boolean acceptanceByFinalState) {
        isAcceptanceByFinalState = acceptanceByFinalState;
    }
    /**
     * Check if acceptance criteria for this machine is accept by empty stack.
     * @return <tt>true</tt> if the criteria is accept by empty stack, otherwise false.
     */
    @XmlAttribute
    public boolean isAcceptanceByEmptyStack() {
        return isAcceptanceByEmptyStack;
    }
    /**
     * Set the acceptance criteria of this machine to accept by empty stack or to not accept by empty stack.
     * @param acceptanceByEmptyStack used to set the acceptance criteria to accept by empty stack or not to not
     * accept by empty stack.
     */
    public void setAcceptanceByEmptyStack(boolean acceptanceByEmptyStack) {
        isAcceptanceByEmptyStack = acceptanceByEmptyStack;
    }
}
