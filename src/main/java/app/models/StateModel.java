package app.models;

import java.util.HashSet;

public class StateModel {

    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;
    private HashSet<TransitionModel> exitingTransitionModelsSet = new HashSet<>(); // (this (current) -> Y)
    private HashSet<TransitionModel> enteringTransitionModelsSet = new HashSet<>(); // (Y -> this (resulting))

    public StateModel(String stateID) {
        this.stateId = stateID;
    }


    public HashSet<TransitionModel> getEnteringTransitionModelsSet() {
        return enteringTransitionModelsSet;
    }

    public String getStateId() {
        return stateId;
    }

    //Getters/Setters
    public boolean isStandardState() {
        return isStandardState;
    }

    public void setStandardState(boolean standardState) {
        isStandardState = standardState;
        isStartState = false;
        isFinalState = false;
    }

    public boolean isStartState() {
        return isStartState;
    }

    public void setStartState(boolean startState) {
        isStartState = startState;
        isStandardState = false;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public void setFinalState(boolean finalState) {
        isFinalState = finalState;
        isStandardState = false;
    }

    public void attachTransitionModelToStateModel(TransitionModel newTransitionModel) {
        exitingTransitionModelsSet.add(newTransitionModel);
    }

    public HashSet<TransitionModel> getExitingTransitionModelsSet() {
        return exitingTransitionModelsSet;
    }


    public HashSet<TransitionModel> getTransitionLinkedToStateX(StateModel resultingStateModel) {

        HashSet<TransitionModel> linkedTransitions = new HashSet<>();
        for (TransitionModel transitionModel : exitingTransitionModelsSet) {
            if (resultingStateModel == transitionModel.getResultingStateModel()) {
                linkedTransitions.add(transitionModel);
            }
        }
        return linkedTransitions;
    }

    @Override
    public String toString() {
        return stateId;
    }
}
