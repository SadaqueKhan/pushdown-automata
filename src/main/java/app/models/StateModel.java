package app.models;

import java.util.HashSet;

public class StateModel {

    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;
    private HashSet<TransitionModel> transitionModelsPointingAwayFromStateModelSet = new HashSet<>();
    private HashSet<StateModel> isResultingStateModelsToSet = new HashSet<>();

    public StateModel(String stateID) {
        this.stateId = stateID;
    }


    public HashSet<StateModel> getIsResultingStateModelsToSet() {
        return isResultingStateModelsToSet;
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
        transitionModelsPointingAwayFromStateModelSet.add(newTransitionModel);
    }

    public HashSet<TransitionModel> getTransitionModelsPointingAwayFromStateModelSet() {
        return transitionModelsPointingAwayFromStateModelSet;
    }


    public HashSet<TransitionModel> getTransitionLinkedToStateX(StateModel resultingStateModel) {

        HashSet<TransitionModel> linkedTransitions = new HashSet<>();
        for (TransitionModel transitionModel : transitionModelsPointingAwayFromStateModelSet) {
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
