package app.listener;

import app.presenter.DiagramController;
import app.view.DiagramView;
import app.view.StateView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


public class DiagramListener implements EventHandler {

    private final DiagramController diagramController;

    public DiagramListener(DiagramController diagramController) {
        this.diagramController = diagramController;
    }

    @Override
    public void handle(Event event) {

        String eventType = event.getEventType().toString();

        MouseEvent mouseEvent = (MouseEvent) event;
        //Node selected
        Node picked = mouseEvent.getPickResult().getIntersectedNode();

        //absolute horizontal x position of the event.
        double xPositionOfMouse = mouseEvent.getScreenX();

        //absolute vertical y position of the event.
        double yPositionOfMouse = mouseEvent.getScreenY();


        if (picked instanceof DiagramView) {
            if (event.getSource() instanceof DiagramView) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    //Add the state to diagram
                    double X = mouseEvent.getX(); // remove pane's coordinate system here
                    double Y = mouseEvent.getY(); // remove pane's coordinate system here
                    diagramController.addStateViewOntoDiagramViewDynamicRender(X, Y);
                }
            }
        }

        if (picked instanceof Circle || picked instanceof Text || picked instanceof Arc) {
            if (event.getSource() instanceof StateView) {
                StateView stateView = (StateView) event.getSource();
                //       String eventType = event.getEventType().toString();
                if (eventType.equals("MOUSE_PRESSED")) {
                    diagramController.stateViewOnMousePressed(stateView, xPositionOfMouse, yPositionOfMouse);
                    // Popup dialog
                    if (mouseEvent.isPopupTrigger()) {
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


