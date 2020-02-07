package app.listeners;


import app.controllers.MainStageController;
import javafx.event.Event;
import javafx.event.EventHandler;
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
            }
        }

        if (eventType.equals("ACTION")) {
            ToggleButton isToggleButton = (ToggleButton) event.getSource();
            String triggeredToggleButtonName = isToggleButton.getText();

            if (triggeredToggleButtonName.equals("Diagram")) {
                mainStageController.triggerDiagramScene();
            } else {
                mainStageController.triggerTransitionTableScene();
            }

        }
    }
}