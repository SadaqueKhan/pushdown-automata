package app.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

@XmlRootElement
public class MachineModel {
    private HashSet<String> inputAlphabetSet;
    private HashSet<String> stackAlphabetSet;

    private HashSet<StateModel> stateModelSet;
    private HashSet<TransitionModel> transitionModelSet;

    private boolean isAcceptanceByFinalState = true;
    private boolean isAcceptanceByEmptyStack = false;

    public MachineModel() {
        this.inputAlphabetSet = new HashSet<>();
        inputAlphabetSet.add("\u03B5");
        this.stackAlphabetSet = new HashSet<>();
        stackAlphabetSet.add("\u03B5");
        this.stateModelSet = new HashSet<>();
        this.transitionModelSet = new HashSet<>();
    }

    public void addStateModelToStateModelSet(StateModel newStateModel) {
        stateModelSet.add(newStateModel);

    }

    public void removeStateModelFromStateModelSet(StateModel stateModelToRemove) {
        stateModelSet.remove(stateModelToRemove);
    }

    public void addTransitionModelToTransitionModelSet(TransitionModel newTransitionModel) {
        transitionModelSet.add(newTransitionModel);
    }

    public void removeTransitionModelsFromTransitionModelSet(HashSet<TransitionModel> setOfTransitionsToRemove) {
        transitionModelSet.removeAll(setOfTransitionsToRemove);
    }

    public StateModel getStateModelFromStateModelSet(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return stateModel;
            }
        }
        return null;
    }

    public StateModel findStartStateModel() {
        for (StateModel isStartStateModel : stateModelSet) {
            if (isStartStateModel.isStartState()) {
                return isStartStateModel;
            }
        }
        return null;
    }


    public HashSet<StateModel> getStateModelSet() {
        return stateModelSet;
    }

    public void setStateModelSet(HashSet<StateModel> stateModelSet) {
        this.stateModelSet = stateModelSet;
    }
    
    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }

    public void setTransitionModelSet(HashSet<TransitionModel> transitionModelSet) {
        this.transitionModelSet = transitionModelSet;
    }

    public HashSet<String> getInputAlphabetSet() {
        return inputAlphabetSet;
    }

    public void setInputAlphabetSet(HashSet<String> inputAlphabetSet) {
        this.inputAlphabetSet = inputAlphabetSet;
    }

    public HashSet<String> getStackAlphabetSet() {
        return stackAlphabetSet;
    }

    public void setStackAlphabetSet(HashSet<String> stackAlphabetSet) {
        this.stackAlphabetSet = stackAlphabetSet;
    }


    public boolean isAcceptanceByFinalState() {
        return isAcceptanceByFinalState;
    }

    public void setAcceptanceByFinalState(boolean acceptancebyFinalState) {
        isAcceptanceByFinalState = acceptancebyFinalState;
    }

    public boolean isAcceptanceByEmptyStack() {
        return isAcceptanceByEmptyStack;
    }

    public void setAcceptanceByEmptyStack(boolean acceptancebyEmptyStack) {
        isAcceptanceByEmptyStack = acceptancebyEmptyStack;
    }
}
