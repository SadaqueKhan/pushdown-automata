package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.MainStageView;
import app.views.TransitionTableView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransitionTableController {

    private final MachineModel machineModel;
    private final MainStageController mainStageController;
    private DiagramController diagramController;

    private TransitionTableView transitionTableView;

    public TransitionTableController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {
        this.machineModel = machineModel;
        this.mainStageController = mainStageController;
    }


    public void addTransitionEntry() {

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
            diagramController.addStateToViewTransitionTableInputEventResponse(0.0, 0.0, userEntryCurrentStateId);
        }

        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExists(userEntryResultingStateId)) {
            resultingStateModel = machineModel.getStateModel(userEntryResultingStateId);
        } else {
            resultingStateModel = new StateModel(userEntryResultingStateId);
            machineModel.addStateModel(resultingStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(0.0, 0.0, userEntryResultingStateId);
        }


        //Create transition model
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

        //Add transition model to machinemodel
        machineModel.addTransitionModel(newTransitionModel);

        //Update diagram view and table view
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);
        diagramController.addTransitionToViewTransitionTableEventResponse(currentStateModel.getStateId(), resultingStateModel.getStateId(), newTransitionModel.toString());
    }


    public void load() {
        this.diagramController = mainStageController.getDiagramController();
        this.transitionTableView = new TransitionTableView(this);

        Scene scene = new Scene(transitionTableView, 1000, 1000);
        Stage stage = new Stage();
        stage.setTitle("Transition Table");
        stage.setScene(scene);
        stage.show();

    }
}
