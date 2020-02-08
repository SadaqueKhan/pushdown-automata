package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.MainStageView;
import app.views.TransitionTableView;

import java.util.Random;

public class TransitionTableController {

    private final MachineModel machineModel;
    private final MainStageView mainStageView;
    private final MainStageController mainStageController;

    private DiagramController diagramController;

    private TransitionTableView transitionTableView;

    public TransitionTableController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {
        this.machineModel = machineModel;
        this.mainStageController = mainStageController;
        this.mainStageView = mainStageView;

        this.diagramController = mainStageController.getDiagramController();

        this.transitionTableView = new TransitionTableView(mainStageView, this);
    }


    public void addTransitionEntry() {

        Random rnd = new Random();

        double x1 = rnd.nextDouble() * 500;
        double y1 = rnd.nextDouble() * 500;

        double x2 = rnd.nextDouble() * 500;
        double y2 = rnd.nextDouble() * 500;

        //User input for a configuration
        String userEntryCurrentStateId = transitionTableView.getCurrentStateTextField().getText();
        String userEntryInputSymbol = transitionTableView.getInputSymbolTextField().getText();
        String userEntryStackSymbolToPop = transitionTableView.getStackSymbolToPopTextField().getText();

        //User input for a action
        String userEntryResultingStateId = transitionTableView.getResultingStateTextField().getText();
        String userEntryStackSymbolToPush = transitionTableView.getStackSymbolToPushTextField().getText();

        // Create placeholders for state models
        StateModel currentStateModel;
        StateModel resultingStateModel;

        // Check to see if current state id exists,    // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExists(userEntryCurrentStateId)) {
            currentStateModel = machineModel.getStateModel(userEntryCurrentStateId);
        } else {
            currentStateModel = new StateModel(userEntryCurrentStateId);
            machineModel.addStateModel(currentStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(x1, y1, userEntryCurrentStateId);
        }

        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExists(userEntryResultingStateId)) {
            resultingStateModel = machineModel.getStateModel(userEntryResultingStateId);
        } else {
            resultingStateModel = new StateModel(userEntryResultingStateId);
            machineModel.addStateModel(resultingStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(x2, y2, userEntryResultingStateId);
        }


        //Create transition model
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

        //Add transition model to machinemodel
        machineModel.addTransitionModel(newTransitionModel);

        //Update diagram view and table view
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);
        diagramController.addTransitionToViewTransitionTableEventResponse(currentStateModel.getStateId(), resultingStateModel.getStateId(), newTransitionModel.toString());
    }

}
