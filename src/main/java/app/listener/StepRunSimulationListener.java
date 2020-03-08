package app.listener;

import app.controller.SimulationController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class StepRunSimulationListener implements EventHandler {
    private final SimulationController simulationController;

    public StepRunSimulationListener(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Override
    public void handle(Event event) {
        Button isButton = (Button) event.getSource();

        String buttonText = isButton.getText();

        if (buttonText.equals("Back")) {
            simulationController.stepBack();
        }

        if (buttonText.equals("Forward")) {
            simulationController.stepForward();
        }
    }
}
