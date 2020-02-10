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


        String userInputWord = "12";

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
        TransitionModel transitionModelA1 = new TransitionModel(stateModelA, "", "", stateModelB, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA1);
        TransitionModel transitionModelA2 = new TransitionModel(stateModelA, "", "", stateModelC, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA2);
        TransitionModel transitionModelA3 = new TransitionModel(stateModelA, "", "", stateModelA, "X");
        stateModelA.attachTransitionToStateModel(transitionModelA3);

        //B transitions
        TransitionModel transitionModel3 = new TransitionModel(stateModelB, "1", "X", stateModelC, "Y");
        stateModelB.attachTransitionToStateModel(transitionModel3);

        //C transitions
        TransitionModel transitionModel4 = new TransitionModel(stateModelC, "2", "Y", stateModelA, "Y");
        stateModelC.attachTransitionToStateModel(transitionModel4);


        machineModel.addTransitionModelToTransitionModelSet(transitionModelA1);
        machineModel.addTransitionModelToTransitionModelSet(transitionModelA2);
        machineModel.addTransitionModelToTransitionModelSet(transitionModelA3);
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

            pathList.remove(startTransition);
            numberOfSymbolsRead = 0;

            stack.clear();
            stack.add("");
        }

    }


    private static void printAllPathsUtil(TransitionModel currentTransition, ArrayList<TransitionModel> pathList, int numberOfSymbolsRead, List<String> splitUserInputArrayList, Stack<String> stack) {


        if ((numberOfSymbolsRead == splitUserInputArrayList.size())) {

            if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), currentTransition.getResultingStateModel())) {
                System.out.println(pathList);
                return;
            }
            System.out.println("FAILED");
            return;
        }


        for (TransitionModel transition : currentTransition.getResultingStateModel().getTransitionModelsAttachedToStateSet()) {
            if ((transition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) || transition.getInputSymbol().equals("")) && transition.getStackSymbolToPop().equals(stack.peek())) {
                pathList.add(transition);
                updateStack(stack, transition.getStackSymbolToPush());
                ++numberOfSymbolsRead;
                printAllPathsUtil(transition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
            } else if (transition.getInputSymbol().equals("") && transition.getStackSymbolToPop().equals(stack.peek())) {
                pathList.add(transition);
                updateStack(stack, transition.getStackSymbolToPush());
                printAllPathsUtil(transition, pathList, numberOfSymbolsRead, splitUserInputArrayList, stack);
            }
        }
    }


//    private static String simulateAcceptanceByFinalState(String userInputWord, MachineModel machineModel) {
//
//        Stack<String> stack = new Stack<>();
//        stack.push("");
//
//        //Split the input word into symbols
//        List<String> splitUserInputArrayList = Arrays.asList(userInputWord.split(""));
//
//        int numberOfSymbolsRead = 0;
//
//        StateModel startStateModel = machineModel.getStartStateModel();
//
//        StringBuilder stringOfTransitions = new StringBuilder();
//
//
//        for (TransitionModel transition : startStateModel.getTransitionModelsAttachedToStateSet()) {
//            //check if valid transition exists
//            if ((transition.getInputSymbol().equals(splitUserInputArrayList.get(numberOfSymbolsRead)) || transition.getInputSymbol().equals("")) && transition.getStackSymbolToPop().equals(stack.peek())) {
//                //Move to transition
//                // Store this transition in a string
//                stringOfTransitions.append(transition.toString()).append("\n");
//                //Update number of symbols read
//                ++numberOfSymbolsRead;
//                //Update stack
//                updateStack(stack, transition.getStackSymbolToPush());
//                if (checkAcceptance(numberOfSymbolsRead, splitUserInputArrayList.size(), transition.getResultingStateModel())) {
//                    return String.valueOf(stringOfTransitions);
//                } else {
//
//                    return anotherTransition(transition, stringOfTransitions, numberOfSymbolsRead, splitUserInputArrayList, stack);
//                }
//            }
//        }
//
//        return String.valueOf(stringOfTransitions);
//    }


    public static String anotherTransition(TransitionModel computedTransition, StringBuilder stringOfTransitions, int numberOfSymbolsRead, List<String> splitUserInputArrayList, Stack<String> stack) {


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

}

