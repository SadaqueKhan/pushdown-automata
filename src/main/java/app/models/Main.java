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

    public static void main(String[] args) throws IOException {

        MachineModel machineModel = new MachineModel();


        String userInputWord = "123";

        StateModel stateModelA = new StateModel("A");
        stateModelA.setStartState(true);
        StateModel stateModelB = new StateModel("B");
        stateModelB.setStandardState(true);
        StateModel stateModelC = new StateModel("C");
        stateModelC.setFinalState(true);

        machineModel.addStateModelToStateModelSet(stateModelA);
        machineModel.addStateModelToStateModelSet(stateModelB);
        machineModel.addStateModelToStateModelSet(stateModelC);

        TransitionModel transitionModel1 = new TransitionModel(stateModelA, "1", "", stateModelB, "X");
        stateModelA.attachTransitionToStateModel(transitionModel1);
        TransitionModel transitionModel2 = new TransitionModel(stateModelB, "2", "X", stateModelA, "Y");
        stateModelB.attachTransitionToStateModel(transitionModel2);
        TransitionModel transitionModel3 = new TransitionModel(stateModelB, "", "Y", stateModelC, "Y");
        stateModelB.attachTransitionToStateModel(transitionModel3);
        TransitionModel transitionModel4 = new TransitionModel(stateModelA, "3", "Y", stateModelB, "X");
        stateModelA.attachTransitionToStateModel(transitionModel4);
        TransitionModel transitionModel5 = new TransitionModel(stateModelB, "", "X", stateModelC, "X");
        stateModelB.attachTransitionToStateModel(transitionModel5);

        machineModel.addTransitionModelToTransitionModelSet(transitionModel1);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel2);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel3);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel4);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel5);

        machineModel.setStartStateModel(machineModel.findStartStateModel());


        String check = simulateAcceptanceByFinalState(userInputWord, machineModel);

        System.out.println(check);

    }

    private static String simulateAcceptanceByFinalState(String userInputWord, MachineModel machineModel) {

        Stack<String> stack = new Stack<>();
        stack.push("");

        //Split the input word into symbols
        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));

        int numberOfSymbolsRead = 0;

        StateModel startStateModel = machineModel.getStartStateModel();

        StringBuilder stringOfTransitions = new StringBuilder();


        for (TransitionModel transition : startStateModel.getTransitionModelsAttachedToStateSet()) {
            //check if valid transition exists
            if ((transition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) || transition.getInputSymbol().equals("")) && transition.getStackSymbolToPop().equals(stack.peek())) {
                //Move to transition
                // Store this transition in a string
                stringOfTransitions.append(transition.toString()).append("\n");
                //Update number of symbols read
                ++numberOfSymbolsRead;
                //Update stack
                updateStack(stack, transition.getStackSymbolToPush());
                if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), transition.getResultingStateModel())) {
                    return String.valueOf(stringOfTransitions);
                } else {

                    return anotherTransition(transition, stringOfTransitions, numberOfSymbolsRead, splitUserInputArrayList, stack);
                }
            }
        }

        return String.valueOf(stringOfTransitions);
    }


    public static String anotherTransition(TransitionModel computedTransition, StringBuilder stringOfTransitions, int numberOfSymbolsRead, List<String> splitUserInputArrayList, Stack<String> stack) {

        System.out.println("Another Transition: " + computedTransition);
        for (TransitionModel nextTransition : computedTransition.getResultingStateModel().getTransitionModelsAttachedToStateSet()) {
            if ((nextTransition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) || nextTransition.getInputSymbol().equals("")) && nextTransition.getStackSymbolToPop().equals(stack.peek())) {
                stringOfTransitions.append(nextTransition.toString()).append("\n");
                ++numberOfSymbolsRead;
                updateStack(stack, nextTransition.getStackSymbolToPush());
                //Check if all input word is read and that the resulting state is a final state
                if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), nextTransition.getResultingStateModel())) {
                    //then return string of transitions
                    return String.valueOf(stringOfTransitions);
                } else {
                    System.out.println("here");
                    anotherTransition(nextTransition, stringOfTransitions, numberOfSymbolsRead, splitUserInputArrayList, stack);
                }
            }

        }
        return String.valueOf(stringOfTransitions);
    }

    private static boolean checkAcceptance(int numberOfSymbolsRead, int size, StateModel resultingStateModel) {
        return (numberOfSymbolsRead == size) && (resultingStateModel.isFinalState());
    }


    private static void updateStack(Stack<String> stack, String toPush) {
        stack.pop();
        stack.push(toPush);
    }

    private static void printAllPaths(int s, int d) {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<>();

        //add source to path[]
        pathList.add(s);

        //Call recursive utility
        printAllPathsUtil(s, d, isVisited, pathList);
    }

    private static void printAllPathsUtil(Integer u, Integer d,
                                          boolean[] isVisited,
                                          List<Integer> localPathList) {

        // Mark the current node
        isVisited[u] = true;

        if (u.equals(d)) {
            System.out.println(localPathList);
            // if match found then no need to traverse more till depth
            isVisited[u] = false;
            return;
        }

        // Recur for all the vertices
        // adjacent to current vertex
        for (Integer i : adjList[u]) {
            if (!isVisited[i]) {
                // store current node
                // in path[]
                localPathList.add(i);
                printAllPathsUtil(i, d, isVisited, localPathList);

                // remove current node
                // in path[]
                localPathList.remove(i);
            }
        }

        // Mark the current node
        isVisited[u] = false;
    }
}

