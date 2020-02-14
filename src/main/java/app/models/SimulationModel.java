package app.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SimulationModel {

    private ArrayList<TransitionModel> pathList;

    public SimulationModel(MachineModel machineModel, String inputWord) {

        System.out.println("Helloworld" + inputWord);

        for (StateModel stateModel : machineModel.getStateModelSet()) {
            System.out.println("StateModel: " + stateModel.getStateId());
        }

        for (TransitionModel transitionModel : machineModel.getTransitionModelSet()) {
            System.out.println("TransitionModel: " + transitionModel.toString());
        }


        simulateAcceptanceByFinalState1(machineModel, inputWord);
    }

    private void simulateAcceptanceByFinalState1(MachineModel machineModel, String userInputWord) {

        pathList = new ArrayList<>();

        Stack<String> stack = new Stack<>();
        stack.push("");

        //Split the input word into symbols
        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));
        StateModel startStateModel = machineModel.getStartStateModel();


        int numberOfSymbolsRead = 0;


        for (TransitionModel startTransition : startStateModel.getExitingTransitionModelsSet()) {
            //check if valid transition exists


            if (startTransition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) && startTransition.getStackSymbolToPop().equals(stack.peek())) {

                //Move to transition
                pathList.add(startTransition);
                //Update number of symbols read
                ++numberOfSymbolsRead;
                //Update stack
                updateStack(stack, startTransition.getStackSymbolToPush());
                printAllPathsUtil(startTransition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
            } else if (startTransition.getInputSymbol().equals("") && startTransition.getStackSymbolToPop().equals(stack.peek())) {
                //Move to transition
                pathList.add(startTransition);
                //Update stack
                updateStack(stack, startTransition.getStackSymbolToPush());
                printAllPathsUtil(startTransition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
            }

            pathList.clear();

            numberOfSymbolsRead = 0;

            stack.clear();
            stack.add("");
        }


    }


    private void printAllPathsUtil(TransitionModel currentTransition, ArrayList<TransitionModel> pathList, int numberOfSymbolsRead, List<String> splitUserInputArrayList, Stack<String> stack) {

        if ((numberOfSymbolsRead == splitUserInputArrayList.size())) {
            if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), currentTransition.getResultingStateModel())) {

                System.out.println(currentTransition.getResultingStateModel());
                System.out.println(pathList);
                return;
            }
            System.out.println("FAILED");
            return;
        }


        for (TransitionModel nextTransition : currentTransition.getResultingStateModel().getExitingTransitionModelsSet()) {
            if ((nextTransition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) || nextTransition.getInputSymbol().equals("")) && nextTransition.getStackSymbolToPop().equals(stack.peek())) {
                pathList.add(nextTransition);
                updateStack(stack, nextTransition.getStackSymbolToPush());
                ++numberOfSymbolsRead;
                printAllPathsUtil(nextTransition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
                --numberOfSymbolsRead;

            } else if (nextTransition.getInputSymbol().equals("") && nextTransition.getStackSymbolToPop().equals(stack.peek())) {
                pathList.add(nextTransition);
                updateStack(stack, nextTransition.getStackSymbolToPush());
                printAllPathsUtil(nextTransition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
            }
        }
    }


    private boolean checkAcceptance(int numberOfSymbolsRead, int size, StateModel resultingStateModel) {
        return (numberOfSymbolsRead == size) && (resultingStateModel.isFinalState());
    }


    private void updateStack(Stack<String> stack, String toPush) {
        stack.pop();
        stack.push(toPush);
    }

    public ArrayList<String> getSuccessfulPathList() {
        ArrayList<String> successfulPathList = new ArrayList<>();

        for (TransitionModel transitionModel : pathList) {
            successfulPathList.add(transitionModel.toString());
        }
        return successfulPathList;
    }
}
