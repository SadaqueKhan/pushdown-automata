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
    /**
     * Constructor of the transition table listener, used to instantiate an instance of the listener.
     * @param transitionTableScenePresenter a reference to the presenter which responds to the events picked up by
     * the listener.
     */
    public TransitionTableListener(TransitionTableScenePresenter transitionTableScenePresenter) {
        this.transitionTableScenePresenter = transitionTableScenePresenter;
    }
    /**
     * Method which routes user commands (events) to the presenter to act upon that data.
     * @param event which occurred on a node that the listener is attached to.
     */
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
