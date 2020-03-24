package app.listener;

import app.model.ConfigurationModel;
import app.presenter.SimulationPresenter;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class QuickRunSimulationListener implements EventHandler {
    private final SimulationPresenter simulationPresenter;

    public QuickRunSimulationListener(SimulationPresenter simulationPresenter) {
        this.simulationPresenter = simulationPresenter;
    }

    @Override
    public void handle(Event event) {

        String eventType = event.getEventType().toString();

        // Toggle between algorithm scene and paths scene
        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof ToggleButton) {
                ToggleButton isToggleButton = (ToggleButton) event.getSource();
                if (isToggleButton.getText().equals("Algorithm")) {
                    simulationPresenter.triggerAlgorithmScene();
                } else {
                    simulationPresenter.triggerPathsScene();
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
                            simulationPresenter.updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                            simulationPresenter.updateTapeViewForSelectedConfiguration(selectedConfiguration);
                            simulationPresenter.updateStackViewForSelectedConfiguration(selectedConfiguration);
                        }
                        if (mouseEvent.getClickCount() == 2) {
                            if (event.getSource() instanceof ListView) {
                                simulationPresenter.createSuccessSimulationStage(selectedConfiguration);
                            }
                        }
                    }

                }
            }
        }
    }
}
