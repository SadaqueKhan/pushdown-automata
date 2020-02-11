package app.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;

public class MachineModel implements Serializable {

    private HashSet<StateModel> stateModelSet;
    private HashSet<TransitionModel> transitionModelSet;
    private StateModel startStateModel;

    public MachineModel() throws IOException {
        this.stateModelSet = new HashSet<StateModel>();
        this.transitionModelSet = new HashSet<TransitionModel>();
        this.startStateModel = findStartStateModel();
    }

    public void addStateModelToStateModelSet(StateModel newStateModel) {
        stateModelSet.add(newStateModel);

    }

    public void removeStateModelFromStateModelSet(String stateModelId) {
        StateModel stateModelToRemove = getStateModelFromStateModelSet(stateModelId);
        stateModelSet.remove(stateModelToRemove);
    }

    public void addTransitionModelToTransitionModelSet(TransitionModel newTransitionModel) {
        transitionModelSet.add(newTransitionModel);
    }


    public boolean stateExistsInStateModelSet(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return true;
            }
        }
        return false;
    }


    public StateModel getStateModelFromStateModelSet(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return stateModel;
            }
        }

        return null;
    }

    public StateModel findStartStateModel() {
        for (StateModel isStartStateModel : stateModelSet) {
            if (isStartStateModel.isStartState()) {
                return isStartStateModel;
            }
        }
        return null;
    }

    public StateModel getStartStateModel() {
        return startStateModel;
    }

    public void setStartStateModel(StateModel startStateModel) {
        this.startStateModel = startStateModel;
    }

    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }


}
