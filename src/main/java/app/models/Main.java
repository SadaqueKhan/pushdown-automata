package app.models;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        MachineModel machineModel = new MachineModel();
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

        TransitionModel transitionModel1 = new TransitionModel(stateModelA, "A", "X", stateModelB, "Y");
        TransitionModel transitionModel2 = new TransitionModel(stateModelB, "B", "X", stateModelC, "Y");

        machineModel.addTransitionModel(transitionModel1);
        machineModel.addTransitionModel(transitionModel2);


        String check = checkAcceptance(userInputWord, machineModel);

        if (check.equals("SUCCESS")) {
            System.out.println("SUCCESS");
        } else if (check.equals("STUCK")) {
            System.out.println("STUCK");
        } else if (check.equals("FAIL")) {
            System.out.println("FAIL");
        } else {
            System.out.println("Transition processes: " + check);
        }


    }

    private static String checkAcceptance(String userInputWord, MachineModel machineModel) {


        return "";
    }
}
