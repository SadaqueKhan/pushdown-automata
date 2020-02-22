package app.listener;

import app.controller.DiagramController;
import app.view.DiagramView;
import app.view.StateView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
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

        //absolute horizontal x position of the event.
        double xPositionOfMouse = event.getScreenX();

        //absolute vertical y position of the event.
        double yPositionOfMouse = event.getScreenY();

        if (picked instanceof DiagramView) {
            if (event.getSource() instanceof DiagramView) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    //Add the state to diagram
                    double X = event.getX(); // remove pane's coordinate system here
                    double Y = event.getY(); // remove pane's coordinate system here
                    diagramController.addStateToViewMouseEventResponse(X, Y);
                }
            }
        }

        if (picked instanceof Circle || picked instanceof Text || picked instanceof Arc) {
            if (event.getSource() instanceof StateView) {
                StateView stateView = (StateView) event.getSource();
                String eventType = event.getEventType().toString();
                if (eventType.equals("MOUSE_PRESSED")) {
                    diagramController.stateViewOnMousePressed(stateView, xPositionOfMouse, yPositionOfMouse);
                    // Popup dialog
                    if (event.isPopupTrigger()) {
                        diagramController.stateViewContextMenuPopUp(stateView);
                    }
                }

                if (eventType.equals("MOUSE_DRAGGED")) {
                    diagramController.stateViewOnMouseDragged(stateView, xPositionOfMouse, yPositionOfMouse);
                }

                if (eventType.equals("MOUSE_RELEASED")) {
                    diagramController.stateViewOnMouseReleased(stateView);
                }
            }
        }
    }
}


