package app.presenter;

import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.MainStage;
import app.view.TransitionTableScene;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashSet;

public class TransitionTablePresenter {

    private final MachineModel machineModel;
    private final MainStage mainStage;
    private final MainStagePresenter mainStagePresenter;

    private DiagramPresenter diagramPresenter;

    private TransitionTableScene transitionTableScene;

    public TransitionTablePresenter(MainStage mainStage, MainStagePresenter mainStagePresenter, MachineModel machineModel) {
        this.machineModel = machineModel;
        this.mainStagePresenter = mainStagePresenter;
        this.mainStage = mainStage;

        this.transitionTableScene = new TransitionTableScene(mainStage, this);
    }

    public void loadTransitionTableOntoStage(DiagramPresenter diagramPresenter) {
        this.diagramPresenter = diagramPresenter;
        mainStage.getContainerForCenterNodes().getChildren().add(transitionTableScene.getTransitionTableContainer());
    }

    public void loadTransitionTableView() {
        for (TransitionModel transitionModelToLoad : machineModel.getTransitionModelSet()) {
            transitionTableScene.getTransitionTable().getItems().add(transitionModelToLoad);
        }
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();
    }

    public void addUserTransitionModelEntryToTransitionTable() {
        //User input for a configuration
        String userEntryCurrentStateID = transitionTableScene.getCurrentStateComboBox().getValue();
        String userEntryInputSymbol = transitionTableScene.getInputSymbolComboBox().getValue();
        String userEntryStackSymbolToPop = transitionTableScene.getStackSymbolToPopComboBox().getValue();

        //User input for a action
        String userEntryResultingStateID = transitionTableScene.getResultingStateComboBox().getValue();
        String userEntryStackSymbolToPush = transitionTableScene.getStackSymbolToPushComboBox().getValue();

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
            diagramPresenter.addStateViewOntoDiagramView(currentStateModel);
        }

        StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingStateID);
        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (resultingStateModel == null) {
            resultingStateModel = new StateModel(userEntryResultingStateID);
            machineModel.addStateModelToStateModelSet(resultingStateModel);
            diagramPresenter.addStateViewOntoDiagramView(resultingStateModel);
        }

        //Create transition model placeholder
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);

        //Check to see if the transition already exists for the current state model
        for (TransitionModel transitionModel : getExitingTranstionsFromStateModel(currentStateModel)) {
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
        transitionTableScene.getTransitionTable().getItems().add(newTransitionModel);
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();

        //Add transitionview onto diagram view
        if (userEntryCurrentStateID.equals(userEntryResultingStateID)) {
            diagramPresenter.addReflexiveTransitionToDiagramView(newTransitionModel);
        } else {
            diagramPresenter.addDirectionalTransitionToView(newTransitionModel);
        }
    }


    public void addTransitionModelEntryToTransitionTable(TransitionModel transitionModelToBeAdded) {
        //Update table view
        transitionTableScene.getTransitionTable().getItems().add(transitionModelToBeAdded);
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboxBox();
        updateStackAlphabetForComboxBox();
    }

    public void deleteTransitionModelEntriesFromTransitionTable() {
        // Retrieve selected rows
        ObservableList<TransitionModel> selectedRows = transitionTableScene.getTransitionTable().getSelectionModel().getSelectedItems();

        HashSet<TransitionModel> removeTransitionSet = new HashSet<>();
        removeTransitionSet.addAll(selectedRows);

        //Update machine model
        machineModel.removeTransitionModelsFromTransitionModelSet(removeTransitionSet);

        //Update transition table view
        transitionTableScene.getTransitionTable().getItems().removeAll(removeTransitionSet);

        //Update diagram view
        diagramPresenter.deleteTransitionView(removeTransitionSet);
    }

    public void deleteTransitionsLinkedToDeletedStateFromTransitionTable(HashSet<TransitionModel> exitingTransitionModelsSet, HashSet<TransitionModel> enteringTransitionModelsSet) {
        transitionTableScene.getTransitionTable().getItems().removeAll(exitingTransitionModelsSet);
        transitionTableScene.getTransitionTable().getItems().removeAll(enteringTransitionModelsSet);
        updateAvailableStateListForCombobox();
    }
    public void updateAvailableStateListForCombobox() {
        ArrayList<String> availableStateList = new ArrayList<>();
        for (StateModel stateModel : machineModel.getStateModelSet()) {
            availableStateList.add(stateModel.getStateId());
        }
        transitionTableScene.getCurrentStateComboBox().getItems().clear();
        transitionTableScene.getCurrentStateComboBox().getItems().addAll(availableStateList);
        transitionTableScene.getResultingStateComboBox().getItems().clear();
        transitionTableScene.getResultingStateComboBox().getItems().addAll(availableStateList);
    }

    public void updateInputAlphabetForComboxBox() {
        transitionTableScene.getInputSymbolComboBox().getItems().clear();
        transitionTableScene.getInputSymbolComboBox().getItems().addAll(machineModel.getInputAlphabetSet());
    }

    public void updateStackAlphabetForComboxBox() {
        transitionTableScene.getStackSymbolToPopComboBox().getItems().clear();
        transitionTableScene.getStackSymbolToPopComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
        transitionTableScene.getStackSymbolToPushComboBox().getItems().clear();
        transitionTableScene.getStackSymbolToPushComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
    }

    public HashSet<TransitionModel> getExitingTranstionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> exitingTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : machineModel.getTransitionModelSet()) {
            if (isExitingTransitionModel.getCurrentStateModel().equals(stateModel)) {
                exitingTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return exitingTransitionFromStateModelToReturn;
    }

}