package app.listener;
import app.presenter.SimulationStagePresenter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to step run simulation scene nodes.
 * </p>
 */
public class StepRunSimulationSceneListener implements EventHandler {
    private final SimulationStagePresenter simulationStagePresenter;
    /**
     * Constructor of the step run simulation listener, used to instantiate an instance of the listener.
     * @param simulationStagePresenter a reference to the presenter which responds to the events picked up by the
     * listener.
     */
    public StepRunSimulationSceneListener(SimulationStagePresenter simulationStagePresenter) {
        this.simulationStagePresenter = simulationStagePresenter;
    }
    /**
     * Method which routes user commands (events) to the presenter to act upon that data.
     * @param event which occurred on a node that the listener is attached to.
     */
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof Button) {
                Button isButton = (Button) event.getSource();
                String buttonText = isButton.getText();
                if (buttonText.equals("<<< Backward")) {
                    simulationStagePresenter.stepBackward();
                }
                if (buttonText.equals("Forward >>>")) {
                    simulationStagePresenter.stepForward();
                }
            }
        }
    }
}
