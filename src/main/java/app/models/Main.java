package app.models;

import java.util.ArrayList;

public class Main {

    static final String EMPTY = "\u03B5";

    public static void main(String[] args) {

        ControlState a = new ControlState(true, false, "a");
        ControlState b = new ControlState(false, true, "B");

        ArrayList<ControlState> controlStates = new ArrayList<>();
        controlStates.add(a);
        controlStates.add(b);

        Transition aToa = new Transition(a, "b", EMPTY, a, "h");
        Transition aToa1 = new Transition(a, "b", EMPTY, a, "h");
        Transition aToa2 = new Transition(a, "b", EMPTY, a, "h");

        Transition aTob = new Transition(a, "b", EMPTY, b, "h");


        ArrayList<Transition> transitions = new ArrayList<>();
        transitions.add(aToa);
        transitions.add(aToa1);
        transitions.add(aToa2);
        transitions.add(aTob);
        String input1 = "b";

        PushDownAutomaton pda1 = new PushDownAutomaton();

        Definition def1 = new Definition(controlStates, transitions, a);
        def1.addControlState(a);
        def1.addControlState(b);
        def1.assignInitialControlState(a);

        pda1.loadDefinition(def1);
        pda1.loadInput(input1);

        System.out.println(pda1.run());


    }
}
