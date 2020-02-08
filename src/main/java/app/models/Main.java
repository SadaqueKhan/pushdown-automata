package app.models;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class Main {

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


        String check = checkAcceptance(userInputWord, machineModel, stack);

        System.out.println(check);

    }

    private static String checkAcceptance(String userInputWord, MachineModel machineModel, Stack<String> stack) {

        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));
        HashSet<TransitionModel> transitionModelSet = machineModel.getTransitionModelSet();

        TransitionModel startTransition = searchForStartState(transitionModelSet);

        TransitionModel transitionRelatedToState = searchForStartState(transitionModelSet);


        StateModel newCurrenStateModel;


        if (startTransition == null) {
            return "No start state defined";
        } else {
            for (int i = 0; i < splitUserInputArrayList.size(); i++) {
                if (startTransition.getInputSymbol().equals(splitUserInputArrayList.get(i)) && startTransition.getStackSymbolToPop().equals("")) {
                    newCurrenStateModel = startTransition.getResultingStateModel();
                    stack.push(startTransition.getStackSymbolToPush());
                    transitionModelSet.remove(startTransition);
                    splitUserInputArrayList.remove(i);

                    startTransition = sea


                    for (int j = 0; j < splitUserInputArrayList.size(); j++) {
                        if ()
                    }
                }

            }
            return "";
        }
    }

    private static TransitionModel searchForStartState(HashSet<TransitionModel> transitionModelSet) {
        for (TransitionModel transitionIsStart : transitionModelSet) {
            StateModel stateModel = transitionIsStart.getCurrentStateModel();
            if (stateModel.isStartState()) {
                return transitionIsStart;
            }
        }
        return null;
    }


}
