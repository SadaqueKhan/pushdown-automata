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

    private boolean isAcceptanceByFinalState = false;
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


    public HashSet<String> getInputAlphabetSet() {
        return inputAlphabetSet;
    }

    public HashSet<String> getStackAlphabetSet() {
        return stackAlphabetSet;
    }

    @XmlElement
    public HashSet<StateModel> getStateModelSet() {
        return stateModelSet;
    }

    @XmlElement
    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }


    public HashSet<TransitionModel> getRelatedTransitions(TransitionModel transitionModel) {
        HashSet<TransitionModel> relatedTransitionModelsToReturn = new HashSet<>();
        StateModel currentStateModelToCompare = transitionModel.getCurrentStateModel();
        StateModel resultingStateModelToCompare = transitionModel.getResultingStateModel();
        for (TransitionModel isRelatedTransitionModel : transitionModelSet) {
            StateModel currentStateModel = isRelatedTransitionModel.getCurrentStateModel();
            StateModel resultingStateModel = isRelatedTransitionModel.getResultingStateModel();
            if (currentStateModelToCompare.equals(currentStateModel) && resultingStateModelToCompare.equals(resultingStateModel)) {
                relatedTransitionModelsToReturn.add(isRelatedTransitionModel);
            }
        }
        return relatedTransitionModelsToReturn;
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

    public HashSet<TransitionModel> getEnteringTranstionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> enteringTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : transitionModelSet) {
            if (isExitingTransitionModel.getResultingStateModel().equals(stateModel)) {
                enteringTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return enteringTransitionFromStateModelToReturn;
    }

    public void setAcceptanceByFinalState(boolean acceptancebyFinalState) {
        isAcceptanceByEmptyStack = false;
        isAcceptanceByFinalState = acceptancebyFinalState;
    }

    public void setAcceptanceByEmptyStack(boolean acceptancebyEmptyStack) {
        isAcceptanceByFinalState = false;
        isAcceptanceByEmptyStack = acceptancebyEmptyStack;

    }
}
