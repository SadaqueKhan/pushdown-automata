package app.model;

import java.util.HashSet;

public class State {


    private HashSet<Transition> setOfTransitions;
    private HashSet<State> setOfStates;

    public State(HashSet<State> setOfStates, HashSet<Transition> setOfTransitions) {

        this.setOfStates = setOfStates;
        this.setOfTransitions = setOfTransitions;

    }
}

