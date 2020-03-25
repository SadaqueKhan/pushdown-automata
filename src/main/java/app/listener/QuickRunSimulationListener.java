package app.listener;
import app.model.ConfigurationModel;
import app.presenter.SimulationStagePresenter;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to quick run simulation scene UI components.
 * </p>
 */
public class QuickRunSimulationListener implements EventHandler {
    private final SimulationStagePresenter simulationStagePresenter;
    /**
     * Constructor of the quick run simulation listener, used to instantiate an instance of the listener.
     * @param simulationStagePresenter a reference to the presenter which responds to the events picked up by the
     * listener.
     */
    public QuickRunSimulationListener(SimulationStagePresenter simulationStagePresenter) {
        this.simulationStagePresenter = simulationStagePresenter;
    }
    /**
     * Method which routes user commands (events) to the presenter to act upon that data.
     * @param event which occurred on a node that the listener is attached to.
     */
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        // Toggle between algorithm scene and paths scene
        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof ToggleButton) {
                ToggleButton isToggleButton = (ToggleButton) event.getSource();
                if (isToggleButton.getText().equals("Algorithm")) {
                    simulationStagePresenter.loadAlgorithmScene();
                } else {
                    simulationStagePresenter.loadPathsScene();
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
                            simulationStagePresenter.updateDiagramViewForSelectedConfiguration(selectedConfiguration);
                            simulationStagePresenter.updateTapeViewForSelectedConfiguration(selectedConfiguration);
                            simulationStagePresenter.updateStackViewForSelectedConfiguration(selectedConfiguration);
                        }
                        if (mouseEvent.getClickCount() == 2) {
                            if (event.getSource() instanceof ListView) {
                                simulationStagePresenter.createIndependentPathSimulationStage(selectedConfiguration);
                            }
                        }
                    }
                }
            }
        }
    }
}
