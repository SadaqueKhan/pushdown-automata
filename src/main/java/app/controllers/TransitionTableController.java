package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.MainStageView;
import app.views.TransitionTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.controlsfx.control.textfield.TextFields;

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

        double x1 = rnd.nextDouble() * 200;
        double y1 = rnd.nextDouble() * 200;

        double x2 = rnd.nextDouble() * 200;
        double y2 = rnd.nextDouble() * 200;

        //User input for a configuration
        String userEntryCurrentStateId = transitionTableView.getCurrentStateComboBox().G)
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

        //Check to see if the transition already exists for the current state model
        for (TransitionModel transitionModel : currentStateModel.getExitingTransitionModelsSet()) {
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
        currentStateModel.getExitingTransitionModelsSet().add(newTransitionModel);
        resultingStateModel.getEnteringTransitionModelsSet().add(newTransitionModel);

        // Create a list of related transition within the transition model with itself included
        newTransitionModel.getRelatedTransitionModels().add(newTransitionModel);
        for (TransitionModel transitionModel : currentStateModel.getExitingTransitionModelsSet()) {
            if (transitionModel.getResultingStateModel().equals(newTransitionModel.getResultingStateModel())) {
                newTransitionModel.getRelatedTransitionModels().add(transitionModel);
            }
        }

        //Add transition model to machinemodel
        machineModel.addTransitionModelToTransitionModelSet(newTransitionModel);

        //Update table view
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);

        //Update diagram view
        if (userEntryCurrentStateId.equals(userEntryResultingStateId)) {
            diagramController.addReflexiveTransitionToViewTransitionTableEventRequest(currentStateModel.getStateId(), resultingStateModel.getStateId(), newTransitionModel.getRelatedTransitionModels());
        } else {
            diagramController.addDirectionalTransitionToViewTransitionTableEventRequest(currentStateModel.getStateId(), resultingStateModel.getStateId(), newTransitionModel.getRelatedTransitionModels());
        }
    }

    public void deleteTransitionEntries() {

        // Retrieve selected rows
        ObservableList<TransitionModel> selectedRows = transitionTableView.getTransitionTable().getSelectionModel().getSelectedItems();

        HashSet<TransitionModel> removeTransitionSet = new HashSet<>();
        removeTransitionSet.addAll(selectedRows);

//        HashSet<StateModel>

        //Update all affected state models
        for (TransitionModel transitionModelToRemove : removeTransitionSet) {
            //remove linking from current state to resulting state
            transitionModelToRemove.getCurrentStateModel().getExitingTransitionModelsSet().remove(transitionModelToRemove);
            //remove linking from resulting state to current state
            transitionModelToRemove.getResultingStateModel().getEnteringTransitionModelsSet().remove(transitionModelToRemove);
        }

        // Update all affect transition models
        for (TransitionModel transitionModelToRemove : removeTransitionSet) {
            for (TransitionModel transitionModelToUpdate : transitionModelToRemove.getRelatedTransitionModels()) {
                transitionModelToUpdate.getRelatedTransitionModels().remove(transitionModelToRemove);
            }
        }

        //Update machine model
        machineModel.getTransitionModelSet().removeAll(removeTransitionSet);

        //Update transition table view
        transitionTableView.getTransitionTable().getItems().removeAll(removeTransitionSet);


        //Update diagram view
        diagramController.deleteTransitionTransitionTableEventRequest(removeTransitionSet);

    }

    public void deleteStateEntryTransitionTableRequest(String stateId, HashSet<TransitionModel> exitingTransitionModelsSet, HashSet<TransitionModel> enteringTransitionModelsSet) {
        transitionTableView.getTransitionTable().getItems().removeAll(exitingTransitionModelsSet);
        transitionTableView.getTransitionTable().getItems().removeAll(enteringTransitionModelsSet);
    }

    public void updateCurrentStateComboxBox() {
        for (StateModel stateModel : machineModel.getStateModelSet()) {
            transitionTableView.getCurrentAvailableStatesSet().add(stateModel.getStateId());
        }

        if (transitionTableView.getAutoCompletionBinding() != null) {
            transitionTableView.getAutoCompletionBinding().dispose();
        }
        transitionTableView.setAutoCompletionBinding(TextFields.bindAutoCompletion(transitionTableView.getCurrentStateComboBox().getEditor(), transitionTableView.getCurrentAvailableStatesSet()));
    }
}