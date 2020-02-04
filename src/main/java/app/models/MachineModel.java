package app.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

public class MachineModel implements Serializable {

    private HashSet<StateModel> stateModelSet;
    private HashSet<TransitionModel> transitionModelSet;

    public MachineModel() throws IOException {
        this.stateModelSet = new HashSet<StateModel>();
        this.transitionModelSet = new HashSet<TransitionModel>();
    }

    public void addStateModel(StateModel newStateModel) {
        stateModelSet.add(newStateModel);

    }

    public void removeStateModel(String stateModelId) {
        StateModel stateModelToRemove = getStateModel(stateModelId);
        stateModelSet.remove(stateModelToRemove);
    }

    public void addTransitionModel(TransitionModel newTransitionModel) {
        transitionModelSet.add(newTransitionModel);
    }


    public boolean stateExists(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return true;
            }
        }
        return false;
    }


    public StateModel getStateModel(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return stateModel;
            }
        }

        return null;
    }


}
