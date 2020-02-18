package app.listener;

import app.controller.TransitionTableController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class TransitionTableListener implements EventHandler {

    private final TransitionTableController transitionTableController;

    public TransitionTableListener(TransitionTableController transitionTableController) {

        this.transitionTableController = transitionTableController;

    }

    @Override
    public void handle(Event event) {

        String eventType = event.getEventType().toString();

        if (eventType.equals("ACTION")) {
            Button isButton = (Button) event.getSource();

            String tempIsButton = isButton.getText();

            if (tempIsButton.equals("Submit")) {
                transitionTableController.addTransitionEntry();
            }
            if (tempIsButton.equals("Delete")) {
                transitionTableController.deleteTransitionModelEntriesFromTransitionTable();
            }

        }
    }
}
