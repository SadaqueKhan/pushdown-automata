package app.listeners;

import app.controllers.DiagramController;
import app.views.StateView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class DiagramListener implements EventHandler<MouseEvent> {

    private final DiagramController diagramController;

    public DiagramListener(DiagramController diagramController) {
        this.diagramController = diagramController;
    }

    @Override
    public void handle(MouseEvent event) {
        //Node selected
        Node picked = event.getPickResult().getIntersectedNode();

        if (picked instanceof Circle || picked instanceof Text || picked instanceof Arc) {
            if (event.getSource() instanceof StateView) {
                StateView stateView = (StateView) event.getSource();
                String eventType = event.getEventType().toString();
                if (eventType.equals("MOUSE_PRESSED")) {
                    // Popup dialog
                    if (event.isPopupTrigger()) {
                        diagramController.stateViewContextMenuPopUp(stateView);
                    }
                }

            }
        }
    }
}



