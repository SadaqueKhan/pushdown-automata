package app.controllers;

import app.models.TransitionModel;
import app.views.TransitionTableView;

public class TransitionTableController {
    public void addTransitionEntry(TransitionTableView transitionTableView) {


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
