package app.models;

import java.util.HashSet;

public class StateModel {

    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;
    private HashSet<TransitionModel> transitionModelsAttachedToStateModelSet = new HashSet<>();

    public StateModel(String stateID) {
        this.stateId = stateID;
    }

    public void attachTransitionToStateModel(TransitionModel transitionModel) {
        transitionModelsAttachedToStateModelSet.add(transitionModel);
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
        transitionModelsAttachedToStateModelSet.add(newTransitionModel);
    }

    public HashSet<TransitionModel> getTransitionModelsAttachedToStateModelSet() {
        return transitionModelsAttachedToStateModelSet;
    }

    @Override
    public String toString() {
        return stateId;
    }

    public HashSet<TransitionModel> getTransitionLinkedToStateX(StateModel resultingStateModel) {

        HashSet<TransitionModel> linkedTransitions = new HashSet<>();
        for (TransitionModel transitionModel : transitionModelsAttachedToStateModelSet) {
            if (resultingStateModel == transitionModel.getResultingStateModel()) {
                linkedTransitions.add(transitionModel);
            }
        }
        return linkedTransitions;
    }
}
