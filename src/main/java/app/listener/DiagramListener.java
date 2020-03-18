package app.listener;

import app.presenter.DiagramPresenter;
import app.view.DiagramScene;
import app.view.StateNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


public class DiagramListener implements EventHandler {

    private final DiagramPresenter diagramPresenter;

    public DiagramListener(DiagramPresenter diagramPresenter) {
        this.diagramPresenter = diagramPresenter;
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


        if (picked instanceof DiagramScene) {
            if (event.getSource() instanceof DiagramScene) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    //Add the state to diagram
                    double X = mouseEvent.getX(); // remove pane's coordinate system here
                    double Y = mouseEvent.getY(); // remove pane's coordinate system here
                    diagramPresenter.addStateViewOntoDiagramViewDynamicRender(X, Y);
                }
            }
        }

        if (picked instanceof Circle || picked instanceof Text || picked instanceof Arc) {
            if (event.getSource() instanceof StateNode) {
                StateNode stateNode = (StateNode) event.getSource();
                //       String eventType = event.getEventType().toString();
                if (eventType.equals("MOUSE_PRESSED")) {
                    diagramPresenter.stateViewOnMousePressed(stateNode, xPositionOfMouse, yPositionOfMouse);
                    // Popup dialog
                    if (mouseEvent.isPopupTrigger()) {
                        diagramPresenter.stateViewContextMenuPopUp(stateNode);
                    }
                }

                if (eventType.equals("MOUSE_DRAGGED")) {
                    diagramPresenter.stateViewOnMouseDragged(stateNode, xPositionOfMouse, yPositionOfMouse);
                }

                if (eventType.equals("MOUSE_RELEASED")) {
                    diagramPresenter.stateViewOnMouseReleased(stateNode);
                }
            }
        }
    }
}


