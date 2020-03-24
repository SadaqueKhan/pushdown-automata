package app.listener;
import app.presenter.SimulationStagePresenter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to step run simulation scene UI components.
 * </p>
 */
public class StepRunSimulationListener implements EventHandler {
    private final SimulationStagePresenter simulationStagePresenter;
    public StepRunSimulationListener(SimulationStagePresenter simulationStagePresenter) {
        this.simulationStagePresenter = simulationStagePresenter;
    }
    @Override
    public void handle(Event event) {
        Button isButton = (Button) event.getSource();
        String buttonText = isButton.getText();
        if (buttonText.equals("<<< Back")) {
            simulationStagePresenter.stepBack();
        }
        if (buttonText.equals("Forward >>>")) {
            simulationStagePresenter.stepForward();
        }
    }
}
