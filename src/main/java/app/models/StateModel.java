package app.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class StateModel {

    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;

    public StateModel() {
    }

    public StateModel(String stateID) {
        super();
        this.stateId = stateID;
    }

    @XmlAttribute
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateModel)) return false;

        StateModel that = (StateModel) o;

        return getStateId().equals(that.getStateId());
    }

    @Override
    public int hashCode() {
        return getStateId().hashCode();
    }

    @Override
    public String toString() {
        return stateId;
    }
}
