package app.models;

public class StateModel {

    private static int num = 0;
    private String type;

    private String stateId;


    public StateModel() {
        this.stateId = "Q" + (num++);
        this.type = "standard";
    }

    public StateModel(String stateID, String type) {
        this.stateId = stateID;
        this.type = type;
    }


    public String getStateId() {
        return stateId;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return stateId;
    }
}

