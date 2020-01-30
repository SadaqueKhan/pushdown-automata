package app.controllers;

import app.models.TransitionModel;
import app.views.TransitionTableView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransitionTableController {

    private final TransitionTableView transitionTableView;


    public TransitionTableController() {

        this.transitionTableView = new TransitionTableView(this);

        Scene scene = new Scene(transitionTableView, 500, 500);
        Stage stage = new Stage();
        stage.setTitle("Transition Table");
        stage.setScene(scene);
        stage.show();
    }

    
    public void addTransitionEntry() {


        //User input for a configuration
        String newCurrentState = transitionTableView.getCurrentStateTextField().getText();
        String newInputSymbol = transitionTableView.getInputSymbolTextField().getText();
        String newStackSymbolToPop = transitionTableView.getStackSymbolToPopTextField().getText();

        //User input for a action
        String newResultingState = transitionTableView.getResultingStateTextField().getText();
        String newStackSymbolToPush = transitionTableView.getStackSymbolToPushTextField().getText();

        //Add user input for configuration and action into the table
        TransitionModel newTransitionModel = new TransitionModel(newCurrentState, newInputSymbol, newStackSymbolToPop, newResultingState, newStackSymbolToPush);
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);


    }
}
