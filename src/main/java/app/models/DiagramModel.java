package app.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

public class DiagramModel implements Serializable {

    private HashSet<StateModel> stateModelSet;

    public DiagramModel() throws IOException {
        this.stateModelSet = new HashSet<StateModel>();
    }

    public void addStateModel(StateModel newStateModel) {
        stateModelSet.add(newStateModel);

    }

    public StateModel checkIfStateExists(String userEntryStateID) {

        for (StateModel stateModel : stateModelSet) {

            if (userEntryStateID.equals(stateModel.getStateId())) {
                return stateModel;
            }

        }

        return new StateModel(userEntryStateID);
    }
}
