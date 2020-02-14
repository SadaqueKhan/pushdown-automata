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
    public String toString() {
        return stateId;
    }
}
