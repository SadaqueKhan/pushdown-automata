package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.MainStageView;
import app.views.TransitionTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

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
        //User input for a configuration
        String userEntryCurrentStateId = transitionTableView.getCurrentStateComboBox().getValue();
        String userEntryInputSymbol = transitionTableView.getInputSymbolComboBox().getValue();
        String userEntryStackSymbolToPop = transitionTableView.getStackSymbolToPopComboBox().getValue();

        //User input for a action
        String userEntryResultingStateId = transitionTableView.getResultingStateComboBox().getValue();
        String userEntryStackSymbolToPush = transitionTableView.getStackSymbolToPushComboBox().getValue();

        //Update alphabets for machine
        machineModel.getInputAlphabetSet().add(userEntryInputSymbol);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPop);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPush);
        //Update transtion table combobox
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();

        // Create placeholders for state models
        StateModel currentStateModel;
        StateModel resultingStateModel;

        // Check to see if current state id exists,    // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExistsInStateModelSet(userEntryCurrentStateId)) {
            currentStateModel = machineModel.getStateModelFromStateModelSet(userEntryCurrentStateId);
        } else {
            currentStateModel = new StateModel(userEntryCurrentStateId);
            machineModel.addStateModelToStateModelSet(currentStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(ThreadLocalRandom.current().nextInt(0, 1275 + 1), ThreadLocalRandom.current().nextInt(0, 450 + 1), userEntryCurrentStateId);
        }
        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (machineModel.stateExistsInStateModelSet(userEntryResultingStateId)) {
            resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingStateId);
        } else {
            resultingStateModel = new StateModel(userEntryResultingStateId);
            machineModel.addStateModelToStateModelSet(resultingStateModel);
            diagramController.addStateToViewTransitionTableInputEventResponse(ThreadLocalRandom.current().nextInt(0, 1275 + 1), ThreadLocalRandom.current().nextInt(0, 450 + 1), userEntryResultingStateId);
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
        //Create exiting transition model from current state model
        currentStateModel.getExitingTransitionModelsSet().add(newTransitionModel);
        //Create entering transition model from resulting state model
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
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();

        //Add transitionview onto diagram view
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
        updateAvailableStateListForCombobox();
    }

    public void updateAvailableStateListForCombobox() {
        ArrayList<String> availableStateList = new ArrayList<>();
        for (StateModel stateModel : machineModel.getStateModelSet()) {
            availableStateList.add(stateModel.getStateId());
        }
        transitionTableView.getCurrentStateComboBox().getItems().clear();
        transitionTableView.getCurrentStateComboBox().getItems().addAll(availableStateList);
        transitionTableView.getResultingStateComboBox().getItems().clear();
        transitionTableView.getResultingStateComboBox().getItems().addAll(availableStateList);
    }

    public void updateInputAlphabetForComboxBox() {
        transitionTableView.getInputSymbolComboBox().getItems().clear();
        transitionTableView.getInputSymbolComboBox().getItems().addAll(machineModel.getInputAlphabetSet());
    }

    public void updateStackAlphabetForComboxBox() {
        transitionTableView.getStackSymbolToPopComboBox().getItems().clear();
        transitionTableView.getStackSymbolToPopComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
        transitionTableView.getStackSymbolToPushComboBox().getItems().clear();
        transitionTableView.getStackSymbolToPushComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
    }
}