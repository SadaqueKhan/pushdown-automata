package app.controller;

import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.MainStageView;
import app.view.TransitionTableView;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashSet;

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

        this.transitionTableView = new TransitionTableView(mainStageView, this);
    }

    public void loadTransitionTableOntoStage(DiagramController diagramController) {
        this.diagramController = diagramController;
        mainStageView.getContainerForCenterNodes().getChildren().add(transitionTableView.getTransitionTableContainer());
    }

    public void loadTransitionTableView() {
        for (TransitionModel transitionModelToLoad : machineModel.getTransitionModelSet()) {
            transitionTableView.getTransitionTable().getItems().add(transitionModelToLoad);
        }
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();
    }

    public void addUserTransitionModelEntryToTransitionTable() {
        //User input for a configuration
        String userEntryCurrentStateID = transitionTableView.getCurrentStateComboBox().getValue();
        String userEntryInputSymbol = transitionTableView.getInputSymbolComboBox().getValue();
        String userEntryStackSymbolToPop = transitionTableView.getStackSymbolToPopComboBox().getValue();

        //User input for a action
        String userEntryResultingStateID = transitionTableView.getResultingStateComboBox().getValue();
        String userEntryStackSymbolToPush = transitionTableView.getStackSymbolToPushComboBox().getValue();

        if ((userEntryCurrentStateID == null || userEntryCurrentStateID.equals("")) || (userEntryInputSymbol == null || userEntryInputSymbol.equals("")) || (userEntryStackSymbolToPop == null || userEntryStackSymbolToPop.equals("")) ||
                (userEntryResultingStateID == null || userEntryResultingStateID.equals("")) || (userEntryStackSymbolToPush == null || userEntryStackSymbolToPush.equals(""))) {
            Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                    "All fields must be filled out to create a transition.", ButtonType.OK);
            invalidActionAlert.setHeaderText("Information");
            invalidActionAlert.setTitle("Invalid Action");
            invalidActionAlert.show();
            return;
        }

        //Update alphabets for machine
        machineModel.getInputAlphabetSet().add(userEntryInputSymbol);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPop);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPush);
        //Update transtion table combobox
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();

        // Create placeholders for state models
        StateModel currentStateModel = machineModel.getStateModelFromStateModelSet(userEntryCurrentStateID);

        // Check to see if current state id exists,    // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (currentStateModel == null) {
            currentStateModel = new StateModel(userEntryCurrentStateID);
            machineModel.addStateModelToStateModelSet(currentStateModel);
            diagramController.addStateViewOntoDiagramView(currentStateModel);
        }

        StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingStateID);
        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (resultingStateModel == null) {
            resultingStateModel = new StateModel(userEntryResultingStateID);
            machineModel.addStateModelToStateModelSet(resultingStateModel);
            diagramController.addStateViewOntoDiagramView(resultingStateModel);
        }

        //Create transition model placeholder
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

        //Check to see if the transition already exists for the current state model
        for (TransitionModel transitionModel : machineModel.getExitingTranstionsFromStateModel(currentStateModel)) {
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

        //Add transition model to machinemodel
        machineModel.addTransitionModelToTransitionModelSet(newTransitionModel);

        //Update table view
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();

        //Add transitionview onto diagram view
        if (userEntryCurrentStateID.equals(userEntryResultingStateID)) {
            diagramController.addReflexiveTransitionToDiagramView(newTransitionModel);
        } else {
            diagramController.addDirectionalTransitionToView(newTransitionModel);
        }
    }


    public void addTransitionModelEntryToTransitionTable(TransitionModel transitionModelToBeAdded) {
        //Update table view
        transitionTableView.getTransitionTable().getItems().add(transitionModelToBeAdded);
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();
    }

    public void deleteTransitionModelEntriesFromTransitionTable() {
        // Retrieve selected rows
        ObservableList<TransitionModel> selectedRows = transitionTableView.getTransitionTable().getSelectionModel().getSelectedItems();

        HashSet<TransitionModel> removeTransitionSet = new HashSet<>();
        removeTransitionSet.addAll(selectedRows);

        //Update machine model
        machineModel.removeTransitionModelsFromTransitionModelSet(removeTransitionSet);

        //Update transition table view
        transitionTableView.getTransitionTable().getItems().removeAll(removeTransitionSet);

        //Update diagram view
        diagramController.deleteTransitionView(removeTransitionSet);
    }

    public void deleteTransitionsLinkedToDeletedStateFromTransitionTable(StateModel stateModelToDelete) {
        transitionTableView.getTransitionTable().getItems().removeAll(machineModel.getExitingTranstionsFromStateModel(stateModelToDelete));
        transitionTableView.getTransitionTable().getItems().removeAll(machineModel.getEnteringTransitionsFromStateModel(stateModelToDelete));
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