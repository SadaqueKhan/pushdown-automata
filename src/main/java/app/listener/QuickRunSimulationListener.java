package app.listener;

import app.controller.SimulationController;
import app.model.ConfigurationModel;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class QuickRunSimulationListener implements EventHandler {
    private final SimulationController simulationController;

    public QuickRunSimulationListener(SimulationController simulationController) {
        this.simulationController = simulationController;
    }

    @Override
    public void handle(Event event) {

        String eventType = event.getEventType().toString();

        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof ToggleButton) {
                ToggleButton isToggleButton = (ToggleButton) event.getSource();
                if (isToggleButton.getText().equals("Algorithm")) {
                    simulationController.triggerAlgorithmView();
                } else {
                    simulationController.triggerPathsView();
                }

            }

        }

        if (eventType.equals("MOUSE_PRESSED") || eventType.equals("MOUSE_DRAGGED") || eventType.equals("MOUSE_RELEASED")) {
            MouseEvent mouseEvent = (MouseEvent) event;

            if (event.getSource() instanceof ListView) {
                ListView<ConfigurationModel> listView = (ListView) event.getSource();
                ObservableList<ConfigurationModel> selectedConfigurationsToHighlightList = listView.getSelectionModel().getSelectedItems();
                if (!(selectedConfigurationsToHighlightList.isEmpty())) {
                    ConfigurationModel selectedConfiguration = selectedConfigurationsToHighlightList.get(0);
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        if (mouseEvent.getClickCount() == 1) {
                            simulationController.updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                            simulationController.updateTapeViewForSelectedConfiguration(selectedConfiguration);
                            simulationController.updateStackViewForSelectedConfiguration(selectedConfiguration);
                        }
                        if (mouseEvent.getClickCount() == 2) {
                            if (event.getSource() instanceof ListView) {
                                simulationController.createSuccessSimulationStage(selectedConfiguration);
                            }
                        }
                    }

                }
            }
        }
    }
}
