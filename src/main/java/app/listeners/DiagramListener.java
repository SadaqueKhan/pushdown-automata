package app.listeners;

import app.controllers.DiagramController;
import app.views.StateView;
import javafx.event.EventHandler;

public class DiagramListener implements EventHandler<javafx.scene.input.MouseEvent> {


    private final DiagramController diagramController;

    public DiagramListener(DiagramController diagramController) {

        this.diagramController = diagramController;
    }


    //Handle click event on the diagram
    @Override
    public void handle(javafx.scene.input.MouseEvent event) {

        //TODO Make a more refined guess of the event type using the state i.e. give state an id
        String eventType = event.getEventType().toString();

        //TODO Add an exception to this incase the event source is not a state
        StateView stateView = (StateView) event.getSource();


        //absolute horizontal x position of the event.
        double xPositionOfMouse = event.getScreenX();

        //absolute vertical y position of the event.
        double yPositionOfMouse = event.getScreenY();


        if (eventType.equals("MOUSE_PRESSED")) {
            diagramController.onMousePressed(stateView, xPositionOfMouse, yPositionOfMouse);
        }

        if (eventType.equals("MOUSE_DRAGGED")) {
            diagramController.onMouseDragged(stateView, xPositionOfMouse, yPositionOfMouse);
        }


    }
}

