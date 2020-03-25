package app.listener;
import app.presenter.MainStagePresenter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to main stage UI components.
 * </p>
 */
public class MainStageListener implements EventHandler {
    private final MainStagePresenter mainStagePresenter;
    /**
     * Constructor of the main stage listener, used to instantiate an instance of the listener.
     * @param mainStagePresenter a reference to the presenter which responds to the events picked up by the
     * listener.
     */
    public MainStageListener(MainStagePresenter mainStagePresenter) {
        this.mainStagePresenter = mainStagePresenter;
    }
    /**
     * Method which routes user commands (events) to the presenter to act upon that data.
     * @param event which occurred on a node that the listener is attached to.
     */
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        if (eventType.equals("KEY_PRESSED")) {
            TextField isInputTextField = (TextField) event.getSource();
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getCode() == KeyCode.ENTER) {
                mainStagePresenter.saveInputWord(isInputTextField.getText());
                mainStagePresenter.loadSimulationStage(isInputTextField.getText());
            }
        }
        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof ToggleButton) {
                ToggleButton isToggleButton = (ToggleButton) event.getSource();
                if (isToggleButton.getText().equals("Diagram")) {
                    mainStagePresenter.loadDiagramScene();
                } else {
                    mainStagePresenter.loadTransitionTableScene();
                }
            } else if (event.getSource() instanceof MenuItem) {
                MenuItem isMenuItem = (MenuItem) event.getSource();
                if (isMenuItem.getText().equals("Save")) {
                    mainStagePresenter.saveMachine();
                } else if (isMenuItem.getText().equals("Load")) {
                    mainStagePresenter.loadMachine();
                } else if (isMenuItem.getText().equals("By Final State")) {
                    mainStagePresenter.setAcceptanceCriteriaToFinalState();
                } else if (isMenuItem.getText().equals("By Empty Stack")) {
                    mainStagePresenter.setAcceptanceCriteriaToEmptyStack();
                } else if (isMenuItem.getText().equals("By Quick Run")) {
                    mainStagePresenter.setSimulationToQuickRun();
                } else if (isMenuItem.getText().equals("By Step Run")) {
                    mainStagePresenter.setSimulationToStepRun();
                } else if (isMenuItem.getText().equals("Guide")) {
                    mainStagePresenter.launchWiki();
                }
            }
        }
    }
}
