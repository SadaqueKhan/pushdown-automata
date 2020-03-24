package app.listener;
import app.presenter.TransitionTablePresenter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to transition table scene UI components.
 * </p>
 */
public class TransitionTableListener implements EventHandler {
    private final TransitionTablePresenter transitionTablePresenter;
    public TransitionTableListener(TransitionTablePresenter transitionTablePresenter) {
        this.transitionTablePresenter = transitionTablePresenter;
    }
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        if (eventType.equals("ACTION")) {
            Button isButton = (Button) event.getSource();
            String tempIsButton = isButton.getText();
            if (tempIsButton.equals("Submit")) {
                transitionTablePresenter.addUserTransitionModelEntryToTransitionTable();
            }
            if (tempIsButton.equals("Delete")) {
                transitionTablePresenter.deleteTransitionModelEntriesFromTransitionTable();
            }
        }
    }
}
