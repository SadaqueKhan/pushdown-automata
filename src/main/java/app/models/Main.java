package app.models;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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


        String check = checkAcceptance(userInputWord, machineModel, stack);

        System.out.println(check);

    }

    private static String checkAcceptance(String userInputWord, MachineModel machineModel, Stack<String> stack) {

        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));
        inputTapeSize = splitUserInputArrayList.size();
        HashSet<TransitionModel> transitionModelSet = machineModel.getTransitionModelSet();
        HashSet<StateModel> stateModelSet = machineModel.getStateModelSet();


        while (inputTapeSize != inputSymbolRead) {
            for (int i = 0; i < stateModelSet.size(); i++) {
                for (TransitionModel transitionModelToSearch1 : applicableTransitionToSearch(transitionModelSet, splitUserInputArrayList)) {
                    for (transitionModelToSearch1)
                }
            }

        }


        return "";


    }

    private static HashSet<TransitionModel> applicableTransitionToSearch(HashSet<TransitionModel> transitionModelSet, List<String> splitUserInputArrayList) {

        StateModel stateModel = transitionModelToSearch1.getResultingStateModel();

        inputSymbolRead++;

        HashSet<TransitionModel> toReturnSet = new HashSet<>();

        for (TransitionModel transitionModel : transitionModelSet) {
            if (startFlag && transitionModel.getCurrentStateModel().isStartState() && transitionModel.getInputSymbol().equals(splitUserInputArrayList.get(inputSymbolRead)) && transitionModel.getStackSymbolToPop().equals("")) {
                toReturnSet.add(transitionModel);
                startFlag = false;
            } else {
                if (transitionModel.getCurrentStateModel().getStateId().equals(stateModel.getStateId()) && splitUserInputArrayList.get(inputSymbolRead).equals(transitionModel.getInputSymbol())) {
                    toReturnSet.add(transitionModel);
                }
            }


        }
        return toReturnSet;

    }


    private static HashSet<TransitionModel> searchForApplicableTransitions(HashSet<TransitionModel> startTransition, List<String> splitUserInputArrayList) {
        String inputSymbolToRead = splitUserInputArrayList.get(inputSymbolRead);
        inputSymbolRead++;

        HashSet<TransitionModel> toReturnSet = new HashSet<>();
        for (TransitionModel transitionModel : startTransition) {

        }
        return toReturnSet;
    }


}
