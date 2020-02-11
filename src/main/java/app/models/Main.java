package app.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Main {

    // No. of vertices in graph
    private static int v;

    // adjacency list
    private static ArrayList<Integer>[] adjList;
    private static ArrayList<TransitionModel> toRender = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        MachineModel machineModel = new MachineModel();


        String userInputWord = "123";

        StateModel stateModelA = new StateModel("A");
        stateModelA.setStartState(true);
        stateModelA.setFinalState(true);
        StateModel stateModelB = new StateModel("B");
        stateModelB.setStandardState(true);
        StateModel stateModelC = new StateModel("C");
        stateModelC.setFinalState(true);

        machineModel.addStateModelToStateModelSet(stateModelA);
        machineModel.addStateModelToStateModelSet(stateModelB);
        machineModel.addStateModelToStateModelSet(stateModelC);

        //A transitions
        TransitionModel transitionModelA1 = new TransitionModel(stateModelA, "1", "", stateModelA, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA1);
        TransitionModel transitionModelA2 = new TransitionModel(stateModelA, "2", "X", stateModelA, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA2);
        TransitionModel transitionModelA3 = new TransitionModel(stateModelA, "3", "X", stateModelA, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA3);
        TransitionModel transitionModelA4 = new TransitionModel(stateModelA, "1", "", stateModelB, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA4);

        //B transitions
        TransitionModel transitionModel3 = new TransitionModel(stateModelB, "2", "X", stateModelC, "Y");
        stateModelB.attachTransitionToStateModel(transitionModel3);

        //C transitions
        TransitionModel transitionModel4 = new TransitionModel(stateModelC, "3", "Y", stateModelA, "Y");
        stateModelC.attachTransitionToStateModel(transitionModel4);


        machineModel.addTransitionModelToTransitionModelSet(transitionModelA1);
        machineModel.addTransitionModelToTransitionModelSet(transitionModelA2);
        machineModel.addTransitionModelToTransitionModelSet(transitionModelA3);
        machineModel.addTransitionModelToTransitionModelSet(transitionModelA4);


        machineModel.addTransitionModelToTransitionModelSet(transitionModel3);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel4);

        machineModel.setStartStateModel(machineModel.findStartStateModel());


        simulateAcceptanceByFinalState1(userInputWord, machineModel);

    }


    private static void simulateAcceptanceByFinalState1(String userInputWord, MachineModel machineModel) {

        ArrayList<TransitionModel> pathList = new ArrayList<>();

        Stack<String> stack = new Stack<>();
        stack.push("");

        //Split the input word into symbols
        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));
        StateModel startStateModel = machineModel.getStartStateModel();


        int numberOfSymbolsRead = 0;


        for (TransitionModel startTransition : startStateModel.getTransitionModelsAttachedToStateSet()) {
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

            pathList.removeAll(pathList);

            numberOfSymbolsRead = 0;

            stack.clear();
            stack.add("");
        }


    }


    private static void printAllPathsUtil(TransitionModel currentTransition, ArrayList<TransitionModel> pathList, int numberOfSymbolsRead, List<String> splitUserInputArrayList, Stack<String> stack) {

        if ((numberOfSymbolsRead == splitUserInputArrayList.size())) {
            if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), currentTransition.getResultingStateModel())) {

                System.out.println(currentTransition.getResultingStateModel());
                System.out.println(pathList);
                return;
            }
            System.out.println("FAILED");
            return;
        }


        for (TransitionModel nextTransition : currentTransition.getResultingStateModel().getTransitionModelsAttachedToStateSet()) {
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


    private static boolean checkAcceptance(int numberOfSymbolsRead, int size, StateModel resultingStateModel) {
        return (numberOfSymbolsRead == size) && (resultingStateModel.isFinalState());
    }


    private static void updateStack(Stack<String> stack, String toPush) {
        stack.pop();
        stack.push(toPush);
    }

}

