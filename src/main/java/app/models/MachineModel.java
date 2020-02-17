package app.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

@XmlRootElement
public class MachineModel {
    private HashSet<String> inputAlphabetSet;
    private HashSet<String> stackAlphabetSet;

    private HashSet<StateModel> stateModelSet;
    private HashSet<TransitionModel> transitionModelSet;

    public MachineModel() {
        this.inputAlphabetSet = new HashSet<>();
        inputAlphabetSet.add("\u03B5");
        this.stackAlphabetSet = new HashSet<>();
        stackAlphabetSet.add("\u03B5");
        this.stateModelSet = new HashSet<>();
        this.transitionModelSet = new HashSet<>();
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

    public boolean stateExistsInStateModelSet(String stateId) {
        for (StateModel stateModel : stateModelSet) {
            if (stateId.equals(stateModel.getStateId())) {
                return true;
            }
        }
        return false;
    }


    public HashSet<String> getInputAlphabetSet() {
        return inputAlphabetSet;
    }

    public HashSet<String> getStackAlphabetSet() {
        return stackAlphabetSet;
    }

    @XmlElement
    public HashSet<StateModel> getStateModelSet() {
        return stateModelSet;
    }

    @XmlElement
    public HashSet<TransitionModel> getTransitionModelSet() {
        return transitionModelSet;
    }

}
