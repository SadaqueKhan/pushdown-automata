package app.listener;
import app.presenter.TransitionTableScenePresenter;
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
    private final TransitionTableScenePresenter transitionTableScenePresenter;
    public TransitionTableListener(TransitionTableScenePresenter transitionTableScenePresenter) {
        this.transitionTableScenePresenter = transitionTableScenePresenter;
    }
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        if (eventType.equals("ACTION")) {
            Button isButton = (Button) event.getSource();
            String tempIsButton = isButton.getText();
            if (tempIsButton.equals("Submit")) {
                transitionTableScenePresenter.addUserTransitionModelEntryToTransitionTable();
            }
            if (tempIsButton.equals("Delete")) {
                transitionTableScenePresenter.deleteTransitionModelEntriesFromTransitionTable();
            }
        }
    }
}
