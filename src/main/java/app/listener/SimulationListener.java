package app.listener;

import app.controller.SimulationController;
import app.model.Configuration;
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
                ObservableList<Configuration> selectedConfigurationsToHighlightList = listView.getSelectionModel().getSelectedItems();
                Configuration selectedConfiguration = selectedConfigurationsToHighlightList.get(0);
                simulationController.updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                simulationController.updateTransitionTableViewForSelectedConfiguration(selectedConfiguration);
                simulationController.updateTapeViewForSelectedConfiguration(selectedConfiguration);
                simulationController.updateStackViewForSelectedConfiguration(selectedConfiguration);
            }
        }
    }
}
