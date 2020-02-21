package app.listener;

import app.controller.SimulationController;
import app.model.Configuration;
import app.model.TransitionModel;
import javafx.collections.ObservableList;
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
            ListView<Configuration> listView = (ListView) event.getSource();
            if (!(listView.getSelectionModel().getSelectedItems().isEmpty())) {
                ObservableList<Configuration> selectedTransitionsToHighlightList = listView.getSelectionModel().getSelectedItems();
                Configuration configuration = selectedTransitionsToHighlightList.get(0);
                TransitionModel transitionModel = configuration.getTransitionModelTakenToReachCurrentConfiguration();
                simulationController.highlightSelectedConfigurationOntoDiagramView(transitionModel);
            }

        }
    }
}
