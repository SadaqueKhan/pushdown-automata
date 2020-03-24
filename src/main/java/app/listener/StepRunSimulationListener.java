package app.listener;

import app.presenter.SimulationPresenter;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class StepRunSimulationListener implements EventHandler {
    private final SimulationPresenter simulationPresenter;

    public StepRunSimulationListener(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
    }

    @Override
    public void handle(Event event) {
        Button isButton = (Button) event.getSource();

        String buttonText = isButton.getText();

        if (buttonText.equals("<<< Back")) {
            simulationPresenter.stepBack();
        }

        if (buttonText.equals("Forward >>>")) {
            simulationPresenter.stepForward();
        }
    }
}
