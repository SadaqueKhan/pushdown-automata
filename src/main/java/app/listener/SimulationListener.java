package app.listener;

import app.controller.SimulationController;
import app.model.TransitionModel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

public class SimulationListener implements EventHandler {

    private final SimulationController simulationController;

    public SimulationListener(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() instanceof ListView) {
            ListView<TransitionModel> listView = (ListView) event.getSource();
//            simulationController.highlightDiagram(listView);
        }
    }
}
