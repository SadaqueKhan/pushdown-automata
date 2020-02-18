package app.listener;


import app.controller.MainStageController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class MainStageListener implements EventHandler {


    private final MainStageController mainStageController;


    public MainStageListener(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
    }


    @Override
    public void handle(Event event) {

        String eventType = event.getEventType().toString();

        if (eventType.equals("KEY_PRESSED")) {
            TextField isInputTextField = (TextField) event.getSource();
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getCode() == KeyCode.ENTER) {
                mainStageController.saveInputWord(isInputTextField.getText());
                mainStageController.setSimulationProgressBar(true);
                mainStageController.setUpTapeView(isInputTextField.getText());
                mainStageController.triggerSimulationView(isInputTextField.getText());
            }
        }

        if (eventType.equals("ACTION")) {
            if (event.getSource() instanceof ToggleButton) {
                ToggleButton isToggleButton = (ToggleButton) event.getSource();
                if (isToggleButton.getText().equals("Diagram")) {
                    mainStageController.triggerDiagramView();
                } else {
                    mainStageController.triggerTransitionTableView();
                }

            } else if (event.getSource() instanceof MenuItem) {
                MenuItem isMenuItem = (MenuItem) event.getSource();
                if (isMenuItem.getText().equals("Save")) {
                    mainStageController.saveMachine();
                } else if (isMenuItem.getText().equals("Load")) {
                    mainStageController.loadMachine();
                }
            }
        }
    }
}