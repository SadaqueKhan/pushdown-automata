package app.listeners;

import app.controllers.TransitionTableController;
import app.views.TransitionTableView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class TransitionTableListener implements EventHandler<ActionEvent> {


    private final TransitionTableController transitionTableController;
    private final TransitionTableView transitionTableView;

    public TransitionTableListener(TransitionTableView transitionTableView) {

        //need to remove this
        this.transitionTableView = transitionTableView;

        this.transitionTableController = new TransitionTableController();

    }

    @Override
    public void handle(ActionEvent event) {

        Button isButton = (Button) event.getSource();

        String tempIsButton = isButton.getText();

        if (tempIsButton.equals("Submit")) {
            transitionTableController.addTransitionEntry(transitionTableView);
        }

    }
}
