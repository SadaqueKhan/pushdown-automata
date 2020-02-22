package app.model;

public class Main {

    private static final String EMPTY = "\u03B5";
    private static MachineModel machineModel;

    public static void main(String[] args) {
        machineModel = new MachineModel();

        StateModel stateModel1 = new StateModel();
        stateModel1.setStartState(true);
        stateModel1.setFinalState(true);
        machineModel.addStateModelToStateModelSet(stateModel1);
//
//        StateModel stateModel2 = new StateModel();
//        stateModel2.setFinalState(false);
//        machineModel.addStateModelToStateModelSet(stateModel2);
//
//        StateModel stateModel3 = new StateModel();
//        stateModel3.setFinalState(false);
//        machineModel.addStateModelToStateModelSet(stateModel3);
//
//        StateModel stateModel4 = new StateModel();
//        stateModel4.setFinalState(false);
//        machineModel.addStateModelToStateModelSet(stateModel4);
//
//        StateModel stateModel5 = new StateModel();
//        stateModel5.setFinalState(true);
//        machineModel.addStateModelToStateModelSet(stateModel5);
//
        addTransition(stateModel1, "A", EMPTY, stateModel1, "1");
        addTransition(stateModel1, EMPTY, EMPTY, stateModel1, "1");
//        addTransition(stateModel1, "A", EMPTY, stateModel3, "1");
//        addTransition(stateModel1, "A", EMPTY, stateModel4, "1");
//        addTransition(stateModel4, "A", EMPTY, stateModel5, "1");


        SimulationModel simulationModel = new SimulationModel(machineModel, "A");

        int num = simulationModel.run();

        System.out.println(num);


    }

    private static void addTransition(StateModel stateModel1, String a, String empty, StateModel stateModel2, String s) {

        TransitionModel transitionModel1A = new TransitionModel(stateModel1, a, empty, stateModel2, s);
        machineModel.addTransitionModelToTransitionModelSet(transitionModel1A);

    }
}
