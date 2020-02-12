package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.MainStageView;
import app.views.TransitionTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.HashSet;
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

    public void loadTransitionTable(DiagramController diagramController) {
        this.diagramController = diagramController;
        transitionTableView.loadToMainStage();
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
        if (machineModel.stateExistsInStateModelSet(userEntryCurrentStateId)) {
            currentStateModel = machineModel.getStateModelFromStateModelSet(userEntryCurrentStateId);
        } else {
            currentStateModel = new StateModel(userEntryCurrentStateId);
            machineModel.addStateModelToStateModelSet(currentStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(x1, y1, userEntryCurrentStateId);
        }


        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExistsInStateModelSet(userEntryResultingStateId)) {
            resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingStateId);
        } else {
            resultingStateModel = new StateModel(userEntryResultingStateId);
            machineModel.addStateModelToStateModelSet(resultingStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(x2, y2, userEntryResultingStateId);
        }


        //Create transition model placeholder
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

        //Check to see if the transition already exists
        for (TransitionModel transitionModel : currentStateModel.getTransitionModelsAttachedToStateModelSet()) {
            if (transitionModel.equals(newTransitionModel)) {
                // if transition exists alert the user and don't do anything further
                Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                        "Transition '" + newTransitionModel + "' already exists.", ButtonType.OK);
                invalidActionAlert.setHeaderText("Information");
                invalidActionAlert.setTitle("Invalid Action");
                invalidActionAlert.show();
                return;
            }
        }
        //Attach transition model to current state model
        currentStateModel.attachTransitionToStateModel(newTransitionModel);
        //Add transition model to machinemodel
        machineModel.addTransitionModelToTransitionModelSet(newTransitionModel);

        //Update table view
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);

        //Update diagram view
        HashSet<TransitionModel> transitionsLinkingToResultingStateSet = currentStateModel.getTransitionLinkedToStateX(resultingStateModel);
        if (userEntryCurrentStateId.equals(userEntryResultingStateId)) {
            diagramController.addReflexiveTransitionToViewTransitionTableEventRequest(currentStateModel.getStateId(), resultingStateModel.getStateId(), transitionsLinkingToResultingStateSet);
        } else {
            diagramController.addDirectionalTransitionToViewTransitionTableEventRequest(currentStateModel.getStateId(), resultingStateModel.getStateId(), transitionsLinkingToResultingStateSet);
        }
    }

    public void deleteTransitionEntries() {

        // Retrieve selected rows
        ObservableList<TransitionModel> selectedRows = transitionTableView.getTransitionTable().getSelectionModel().getSelectedItems();

        HashSet<StateModel> changedStateModelsSet = new HashSet<StateModel>();

        //Update effected state models
        for (TransitionModel transitionModel : selectedRows) {
            //flag state that has been changed
            changedStateModelsSet.add(transitionModel.getCurrentStateModel());

            transitionModel.getCurrentStateModel().getTransitionModelsAttachedToStateModelSet().remove(transitionModel);
            if (transitionModel.getCurrentStateModel().getTransitionModelsAttachedToStateModelSet().isEmpty()) {
                // If state model no longer has transition remove the state model from the machine model
                machineModel.removeStateModelFromStateModelSet(transitionModel.getCurrentStateModel().getStateId());
            }
        }

        //Update machine model
        machineModel.getTransitionModelSet().removeAll(selectedRows);


        //Update transition table view
        transitionTableView.getTransitionTable().getItems().removeAll(selectedRows);

        //Update diagram view
        diagramController.deleteTransitionTransitionTableEventRequest(changedStateModelsSet);

    }

    public void deleteStateEntry(String stateId) {
        //Remove from machine model
        machineModel.removeStateModelFromStateModelSet(stateId);

        ObservableList<TransitionModel> allEntriesInPeopleRecordTable;
        allEntriesInPeopleRecordTable = transitionTableView.getTransitionTable().getItems();

        //See what this prints
        for (TransitionModel transitionModel : allEntriesInPeopleRecordTable) {
            if (transitionModel.getCurrentStateModel().getStateId().equals(stateId)) {
                allEntriesInPeopleRecordTable.remove(transitionModel);
            }

        }
    }
}