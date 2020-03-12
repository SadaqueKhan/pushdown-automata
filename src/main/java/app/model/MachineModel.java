package app.model;

import javax.xml.bind.annotation.XmlElement;
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


    @XmlElement
    public HashSet<StateModel> getStateModelSet() {
        return stateModelSet;
    }

    @XmlElement
    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }


    public HashSet<String> getInputAlphabetSet() {
        return inputAlphabetSet;
    }

    public HashSet<String> getStackAlphabetSet() {
        return stackAlphabetSet;
    }


    public HashSet<TransitionModel> getExitingTranstionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> exitingTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : transitionModelSet) {
            if (isExitingTransitionModel.getCurrentStateModel().equals(stateModel)) {
                exitingTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return exitingTransitionFromStateModelToReturn;
    }

    public HashSet<TransitionModel> getEnteringTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> enteringTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isEnteringTransitionModel : transitionModelSet) {
            if (isEnteringTransitionModel.getResultingStateModel().equals(stateModel)) {
                enteringTransitionFromStateModelToReturn.add(isEnteringTransitionModel);
            }
        }
        return enteringTransitionFromStateModelToReturn;
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
