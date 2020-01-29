package app.controllers;

import app.models.TransitionModel;
import app.views.TransitionTableView;

public class TransitionTableController {
    public void addTransitionEntry(TransitionTableView transitionTableView) {


        // Configuration user input
        String newCurrentState = transitionTableView.getCurrentStateTextField().getText();
        String newInputSymbol = transitionTableView.getInputSymbolTextField().getText();
        String newStackSymbolToPop = transitionTableView.getStackSymbolToPopTextField().getText();

        //Action user input
        String newResultingState = transitionTableView.getResultingStateTextField().getText();
        String newStackSymbolToPush = transitionTableView.getStackSymbolToPushTextField().getText();


        TransitionModel newTransitionModel = new TransitionModel(newCurrentState, newInputSymbol, newStackSymbolToPop, newResultingState, newStackSymbolToPush);
        transitionTableView.getTransitionTable().getItems().add(newTransitionModel);


    }
}
