package app.models;

import java.util.ArrayList;

public class Definition {
    private ArrayList<ControlState> controlStates = new ArrayList<>();
    private ArrayList<Transition> transitions = new ArrayList<>();
    private ControlState initialControlState;

    public Definition(ArrayList<ControlState> controlStates, ArrayList<Transition> transitions, ControlState initialControlState) {
        this.controlStates = controlStates;
        this.transitions = transitions;
        this.initialControlState = initialControlState;
    }

    public void assignInitialControlState(ControlState controlState) {
        initialControlState = controlState;
    }

    public void addControlState(ControlState controlState) {
        controlStates.add(controlState);
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
    }

    public void removeControlState(ControlState controlState) {
        controlStates.remove(controlState);
    }

    public void removeTransition(Transition transition) {
        transitions.remove(transition);
    }

    public ControlState getInitialControlState() {
        return initialControlState;
    }

    public ArrayList<ControlState> getControlStates() {
        return controlStates;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }
}
