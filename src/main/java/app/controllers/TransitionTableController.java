package app.controllers;

import app.models.DiagramModel;
import app.models.StateModel;
import app.models.TransitionModel;
import app.views.TransitionTableView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransitionTableController {

    private final DiagramModel diagramModel;
    private TransitionTableView transitionTableView;


    public TransitionTableController(DiagramModel diagramModel) {


        this.diagramModel = diagramModel;

    }


    public void addTransitionEntry() {

        //User input for a configuration
        String userEntryCurrentStateId = transitionTableView.getCurrentStateTextField().getText();
        String userEntryInputSymbol = transitionTableView.getInputSymbolTextField().getText();
        String userEntryStackSymbolToPop = transitionTableView.getStackSymbolToPopTextField().getText();

        //User input for a action
        String userEntryResultingStateId = transitionTableView.getResultingStateTextField().getText();
        String userEntryStackSymbolToPush = transitionTableView.getStackSymbolToPushTextField().getText();


        StateModel currentStateModel = diagramModel.checkIfStateExists(userEntryCurrentStateId);
        StateModel resultingStateModel = diagramModel.checkIfStateExists(userEntryResultingStateId);


        //Add user input for configuration and action into the table
        TransitionModel newTransitionModel = new TransitionModel(currentStateModel, userEntryInputSymbol, userEntryStackSymbolToPop, resultingStateModel, userEntryStackSymbolToPush);
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);


    }

    public void load() {
        this.transitionTableView = new TransitionTableView(this);

        Scene scene = new Scene(transitionTableView, 500, 500);
        Stage stage = new Stage();
        stage.setTitle("Transition Table");
        stage.setScene(scene);
        stage.show();

    }
}
