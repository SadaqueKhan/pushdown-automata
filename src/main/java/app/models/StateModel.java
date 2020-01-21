package app.models;

import java.util.HashSet;

public class StateModel {


    private HashSet<TransitionModel> setOfTransitionModels;
    private HashSet<StateModel> setOfStateModels;

    public StateModel(HashSet<StateModel> setOfStateModels, HashSet<TransitionModel> setOfTransitionModels) {

        this.setOfStateModels = setOfStateModels;
        this.setOfTransitionModels = setOfTransitionModels;

    }
}

