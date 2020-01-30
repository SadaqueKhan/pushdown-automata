package app.models;

public class StateModel {

    private static int num = 0;

    private String stateId;

    public StateModel() {
        this.stateId = "Q" + (num++);
    }

    public StateModel(String userEntryStateID) {
        this.stateId = userEntryStateID;
    }


    public String getStateId() {
        return stateId;
    }

    @Override
    public String toString() {
        return stateId;
    }
}

