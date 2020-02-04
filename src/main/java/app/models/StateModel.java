package app.models;

public class StateModel {

    private static int num = 0;
    private String stateId;

    private boolean isStandardState = true;
    private boolean isStartState = false;
    private boolean isFinalState = false;


    public StateModel() {
        this.stateId = "Q" + (num++);
    }

    public StateModel(String stateID) {
        this.stateId = stateID;
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

    @Override
    public String toString() {
        return stateId;
    }
}

