package app.controllers;

import app.models.DiagramModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.DiagramView;
import app.views.TransitionTableView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransitionTableController {

    private final DiagramModel diagramModel;
    private final DiagramView diagramView;
    private TransitionTableView transitionTableView;


    public TransitionTableController(DiagramModel diagramModel, DiagramView diagramView) {

        this.diagramModel = diagramModel;
        this.diagramView = diagramView;

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
        if (diagramModel.stateExists(userEntryCurrentStateId)) {
            currentStateModel = diagramModel.getStateModel(userEntryCurrentStateId);
        } else {
            currentStateModel = new StateModel(userEntryCurrentStateId);
            diagramModel.addStateModel(currentStateModel);

            StateController newStateController = new StateController(diagramModel, diagramView, this);
            diagramView.addStateView(0.0, 0.0, newStateController, userEntryCurrentStateId);
        }

        // Check to see if resulting state id exists, if it does retrieve it otherwise create a new state with the specified details.
        if (diagramModel.stateExists(userEntryResultingStateId)) {
            resultingStateModel = diagramModel.getStateModel(userEntryResultingStateId);
        } else {
            resultingStateModel = new StateModel(userEntryResultingStateId);
            diagramModel.addStateModel(resultingStateModel);

            StateController newStateController = new StateController(diagramModel, diagramView, this);
            diagramView.addStateView(0.0, 0.0, newStateController, userEntryResultingStateId);
        }


        //Add user input for configuration and action into the table
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);

        diagramModel.addTransitionModel(newTransitionModel);
        diagramView.addTransitionView(currentStateModel.getStateId(), resultingStateModel.getStateId(), newTransitionModel.toString());


    }


    public void load() {
        this.transitionTableView = new TransitionTableView(this);

        Scene scene = new Scene(transitionTableView, 1000, 1000);
        Stage stage = new Stage();
        stage.setTitle("Transition Table");
        stage.setScene(scene);
        stage.show();

    }
}
