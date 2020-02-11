package app.listeners;

import app.controllers.TransitionTableController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class TransitionTableListener implements EventHandler<ActionEvent> {

    private final TransitionTableController transitionTableController;

    public TransitionTableListener(TransitionTableController transitionTableController) {

        this.transitionTableController = transitionTableController;

    }

    @Override
    public void handle(ActionEvent event) {

        Button isButton = (Button) event.getSource();

        String tempIsButton = isButton.getText();

        if (tempIsButton.equals("Submit")) {
            transitionTableController.addTransitionEntry();
        }

    }
}
