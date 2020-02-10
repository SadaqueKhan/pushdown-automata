package app.models;

import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

public class Main {

    static int inputSymbolRead = 0;
    static int inputTapeSize = 0;
    static boolean startFlag = true;


    public static void main(String[] args) throws IOException {

        MachineModel machineModel = new MachineModel();
        Stack<String> stack = new Stack<>();

        String userInputWord = "ABC";

        StateModel stateModelA = new StateModel("A");
        stateModelA.setStartState(true);
        StateModel stateModelB = new StateModel("B");
        stateModelB.setStandardState(true);
        StateModel stateModelC = new StateModel("C");
        stateModelC.setFinalState(true);

        machineModel.addStateModel(stateModelA);
        machineModel.addStateModel(stateModelB);
        machineModel.addStateModel(stateModelC);

        TransitionModel transitionModel1 = new TransitionModel(stateModelA, "A", "", stateModelB, "Z0");
        TransitionModel transitionModel2 = new TransitionModel(stateModelB, "B", "X", stateModelC, "Y");

        machineModel.addTransitionModel(transitionModel1);
        machineModel.addTransitionModel(transitionModel2);


        HashSet<String> stringSet = new HashSet<String>();

        stringSet.add("a");
        stringSet.add("a");

        System.out.println(stringSet.size());


        String check = checkAcceptance(userInputWord, machineModel, stack);

        System.out.println(check);

    }

    private static String checkAcceptance(String userInputWord, MachineModel machineModel, Stack<String> stack) {
        return "";
    }


}
