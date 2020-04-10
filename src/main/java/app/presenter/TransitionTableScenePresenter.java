package app.presenter;
import app.model.MachineModel;
import app.model.StateModel;
import app.model.TransitionModel;
import app.view.MainScene;
import app.view.TransitionTableScene;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.HashSet;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Transition table scene presenter retrieves data from repositories (the model), and formats it for display in the
 * transition table scene.
 * </p>
 */
public class TransitionTableScenePresenter {
    private final MachineModel machineModel;
    private final MainScene mainScene;
    private final TransitionTableScene transitionTableScene;
    private DiagramScenePresenter diagramScenePresenter;
    /**
     * Constructor of the transition table presenter, used to instantiate an instance of the presenter.
     * @param mainScene for which the transition table scene is rendered on.
     * @param machineModel the model containing the data about the pushdown automaton machine.
     */
    public TransitionTableScenePresenter(MainScene mainScene, MachineModel machineModel) {
        this.machineModel = machineModel;
        this.mainScene = mainScene;
        this.transitionTableScene = new TransitionTableScene(this);
    }
    /**
     * Reloads the transition table scene back onto the main stage when selected via the tab found on in the main stage.
     * @param diagramScenePresenter the presenter which needs to be notified about events on the transition table scene.
     */
    void loadTransitionTableSceneOntoMainStage(DiagramScenePresenter diagramScenePresenter) {
        this.diagramScenePresenter = diagramScenePresenter;
        mainScene.getContainerForCenterNodes().getChildren().add(transitionTableScene.getTransitionTableContainer());
    }
    /**
     * Loads transition data onto the transition table scene.
     */
    public void loadTransitionTableView() {
        // Load data found on the transition table UI component in the transition table scene.
        for (TransitionModel transitionModelToLoad : machineModel.getTransitionModelSet()) {
            transitionTableScene.getTransitionTable().getItems().add(transitionModelToLoad);
        }
        // Load data found in the combo box UI component in the transition table scene.
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboBox();
        updateStackAlphabetForComboBox();
    }
    /**
     * Handles updates to combo boxes which list states.
     */
    void updateAvailableStateListForCombobox() {
        ArrayList<String> availableStateList = new ArrayList<>();
        for (StateModel stateModel : machineModel.getStateModelSet()) {
            availableStateList.add(stateModel.getStateId());
        }
        transitionTableScene.getCurrentStateComboBox().getItems().clear();
        transitionTableScene.getCurrentStateComboBox().getItems().addAll(availableStateList);
        transitionTableScene.getResultingStateComboBox().getItems().clear();
        transitionTableScene.getResultingStateComboBox().getItems().addAll(availableStateList);
    }
    /**
     * Handles updates to combo boxes which list input alphabet symbols.
     */
    void updateInputAlphabetForComboBox() {
        transitionTableScene.getInputSymbolComboBox().getItems().clear();
        transitionTableScene.getInputSymbolComboBox().getItems().addAll(machineModel.getInputAlphabetSet());
    }
    /**
     * Handles updates to combo boxes which list stack alphabet symbols.
     */
    void updateStackAlphabetForComboBox() {
        transitionTableScene.getStackSymbolToPopComboBox().getItems().clear();
        transitionTableScene.getStackSymbolToPopComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
        transitionTableScene.getStackSymbolToPushComboBox().getItems().clear();
        transitionTableScene.getStackSymbolToPushComboBox().getItems().addAll(machineModel.getStackAlphabetSet());
    }
    /**
     * Handles creating a new transition entry in the transition table, updating the models found in the application,
     * and notifying the new transition diagram presenter.
     */
    public void addUserTransitionModelEntryToTransitionTable() {
        //User input for a configuration part of input needed to create a transition.
        String userEntryCurrentStateID = transitionTableScene.getCurrentStateComboBox().getValue();
        String userEntryInputSymbol = transitionTableScene.getInputSymbolComboBox().getValue();
        String userEntryStackSymbolToPop = transitionTableScene.getStackSymbolToPopComboBox().getValue();
        //User input for a action part of input needed to create a transition.
        String userEntryResultingStateID = transitionTableScene.getResultingStateComboBox().getValue();
        String userEntryStackSymbolToPush = transitionTableScene.getStackSymbolToPushComboBox().getValue();
        // Validate whether all input field ahve been filled.
        if ((userEntryCurrentStateID == null || userEntryCurrentStateID.equals("")) || (userEntryInputSymbol == null || userEntryInputSymbol.equals("")) || (userEntryStackSymbolToPop == null || userEntryStackSymbolToPop.equals("")) ||
                (userEntryResultingStateID == null || userEntryResultingStateID.equals("")) || (userEntryStackSymbolToPush == null || userEntryStackSymbolToPush.equals(""))) {
            Alert invalidActionAlert = new Alert(Alert.AlertType.NONE,
                    "All fields must be filled out to create a transition.", ButtonType.OK);
            invalidActionAlert.setHeaderText("Information");
            invalidActionAlert.setTitle("Invalid Action");
            invalidActionAlert.show();
            return;
        }
        //Update input alphabet set and stack alphabet set found in the machine model.
        machineModel.getInputAlphabetSet().add(userEntryInputSymbol);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPop);
        machineModel.getStackAlphabetSet().add(userEntryStackSymbolToPush);
        //Update transition table combo boxes
        updateInputAlphabetForComboBox();
        updateStackAlphabetForComboBox();
        // Retrieve current state model from the state model set found in the machine model.
        StateModel currentStateModel = machineModel.getStateModelFromStateModelSet(userEntryCurrentStateID);
        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state model with
        // the specified details and notify the diagram presenter to update it UI accordingly.
        if (currentStateModel == null) {
            currentStateModel = new StateModel(userEntryCurrentStateID);
            machineModel.addStateModelToStateModelSet(currentStateModel);
            diagramScenePresenter.addStateViewOntoDiagramView(currentStateModel);
        }
        // Retrieve current resulting model from the state model set found in the machine model.
        StateModel resultingStateModel = machineModel.getStateModelFromStateModelSet(userEntryResultingStateID);
        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state model with
        // the specified details and notify the diagram presenter to update it UI accordingly.
        if (resultingStateModel == null) {
            resultingStateModel = new StateModel(userEntryResultingStateID);
            machineModel.addStateModelToStateModelSet(resultingStateModel);
            diagramScenePresenter.addStateViewOntoDiagramView(resultingStateModel);
        }
        //Create transition model placeholder.
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);
        //Check to see if the transition model already exists for the current state model.
        for (TransitionModel transitionModel : getExitingTransitionsFromStateModel(currentStateModel)) {
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
        //Add transition model into the transition model set found in the machine model.
        machineModel.addTransitionModelToTransitionModelSet(newTransitionModel);
        //Update transition table UI component found in transition table scene.
        transitionTableScene.getTransitionTable().getItems().add(newTransitionModel);
        //Update combo boxes UI component found in transition table scene.
        updateAvailableStateListForCombobox();
        //Notify diagram presenter to add a new transition node on the diagram scene.
        if (userEntryCurrentStateID.equals(userEntryResultingStateID)) {
            diagramScenePresenter.addReflexiveTransitionToDiagramView(newTransitionModel);
        } else {
            diagramScenePresenter.addDirectionalTransitionToView(newTransitionModel);
        }
    }
    /**
     * Retrieves all exiting transitions from a given state.
     * @param stateModel the state for which all exiting transitions need to identified upon.
     * @return the {@code HashSet<TransitionModel>} which represents the set of all transitions from a given state.
     */
    private HashSet<TransitionModel> getExitingTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> exitingTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isExitingTransitionModel : machineModel.getTransitionModelSet()) {
            if (isExitingTransitionModel.getCurrentStateModel().equals(stateModel)) {
                exitingTransitionFromStateModelToReturn.add(isExitingTransitionModel);
            }
        }
        return exitingTransitionFromStateModelToReturn;
    }
    /**
     * Handles updating the transition table scene given the creation of a new transition model in the repository of
     * models.
     * @param newTransitionModel the transition model used to update the transition table scene UI components.
     */
    void addTransitionModelEntryToTransitionTable(TransitionModel newTransitionModel) {
        //Update transition table UI component.
        transitionTableScene.getTransitionTable().getItems().add(newTransitionModel);
        updateAvailableStateListForCombobox();
        updateInputAlphabetForComboBox();
        updateStackAlphabetForComboBox();
    }
    /**
     * Handles deletion of selected transitions from the transition table UI component found in the transition table
     * scene.
     */
    public void deleteTransitionModelEntriesFromTransitionTable() {
        // Retrieve selected rows.
        ObservableList<TransitionModel> selectedRows = transitionTableScene.getTransitionTable().getSelectionModel().getSelectedItems();
        HashSet<TransitionModel> removeTransitionSet = new HashSet<>();
        removeTransitionSet.addAll(selectedRows);
        //Update transition table UI component.
        transitionTableScene.getTransitionTable().getItems().removeAll(removeTransitionSet);
        //Notify diagram presenter to delete selected transitions node on the diagram scene.
        diagramScenePresenter.deleteTransitionView(removeTransitionSet);
        //Update transition model set found in the machine model.
        machineModel.removeTransitionModelsFromTransitionModelSet(removeTransitionSet);
    }
    /**
     * Handles bulk deletion of transitions when a state has been requested to be deleted by the user.
     * @param stateModelToDelete requested to be deleted.
     */
    void deleteTransitionsLinkedToDeletedStateFromTransitionTable(StateModel stateModelToDelete) {
        HashSet<TransitionModel> exitingTransitionsFromStateModel = getExitingTransitionsFromStateModel
                (stateModelToDelete);
        HashSet<TransitionModel> enteringTransitionsFromStateModel = getEnteringTransitionsFromStateModel
                (stateModelToDelete);
        transitionTableScene.getTransitionTable().getItems().removeAll(exitingTransitionsFromStateModel);
        transitionTableScene.getTransitionTable().getItems().removeAll(enteringTransitionsFromStateModel);
        updateAvailableStateListForCombobox();
    }
    /**
     * @param stateModel used to determine all entering transition model from a given state model.
     * @return {@code HashSet<TransitionModel>} of entering transition models from a given state model.
     */
    public HashSet<TransitionModel> getEnteringTransitionsFromStateModel(StateModel stateModel) {
        HashSet<TransitionModel> enteringTransitionFromStateModelToReturn = new HashSet<>();
        for (TransitionModel isEnteringTransitionModel : machineModel.getTransitionModelSet()) {
            if (isEnteringTransitionModel.getResultingStateModel().equals(stateModel)) {
                enteringTransitionFromStateModelToReturn.add(isEnteringTransitionModel);
            }
        }
        return enteringTransitionFromStateModelToReturn;
    }
    public TransitionTableScene getTransitionTableScene() {
        return transitionTableScene;
    }
}