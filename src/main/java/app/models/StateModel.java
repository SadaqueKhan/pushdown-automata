package app.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;


@XmlRootElement
public class StateModel {

    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;
    private HashSet<TransitionModel> exitingTransitionModelsSet = new HashSet<>(); // (this (current) -> Y)
    private HashSet<TransitionModel> enteringTransitionModelsSet = new HashSet<>(); // (Y -> this (resulting))

    public StateModel() {
    }

    public StateModel(String stateID) {
        super();
        this.stateId = stateID;
    }


    public HashSet<TransitionModel> getEnteringTransitionModelsSet() {
        return enteringTransitionModelsSet;
    }

    @XmlAttribute
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
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


    public HashSet<TransitionModel> getExitingTransitionModelsSet() {
        return exitingTransitionModelsSet;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateModel)) return false;

        StateModel that = (StateModel) o;

        if (isStandardState() != that.isStandardState()) return false;
        if (isStartState() != that.isStartState()) return false;
        if (isFinalState() != that.isFinalState()) return false;
        if (getStateId() != null ? !getStateId().equals(that.getStateId()) : that.getStateId() != null) return false;
        if (getExitingTransitionModelsSet() != null ? !getExitingTransitionModelsSet().equals(that.getExitingTransitionModelsSet()) : that.getExitingTransitionModelsSet() != null)
            return false;
        return getEnteringTransitionModelsSet() != null ? getEnteringTransitionModelsSet().equals(that.getEnteringTransitionModelsSet()) : that.getEnteringTransitionModelsSet() == null;
    }

    @Override
    public int hashCode() {
        int result = getStateId() != null ? getStateId().hashCode() : 0;
        result = 31 * result + (isStandardState() ? 1 : 0);
        result = 31 * result + (isStartState() ? 1 : 0);
        result = 31 * result + (isFinalState() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return stateId;
    }
}
