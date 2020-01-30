package app.listeners;

import app.controllers.DiagramController;
import app.views.StateView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DiagramListener implements EventHandler<MouseEvent> {

    private final DiagramController diagramController;

    public DiagramListener(DiagramController diagramController) {
        this.diagramController = diagramController;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {

            Node picked = event.getPickResult().getIntersectedNode();

            //Check if mouse is pressing on state node
            if (picked instanceof StateView) {
                //Do nothing
            } else {
                //Add the state to diagram
                double X = event.getX(); // remove pane's coordinate system here
                double Y = event.getY(); // remove pane's coordinate system here

                diagramController.addStateToView(X, Y);
//                this.addStateToView(X, Y);
//                this.addEdge("Q0", "Q1");
            }

        }
    }
}
