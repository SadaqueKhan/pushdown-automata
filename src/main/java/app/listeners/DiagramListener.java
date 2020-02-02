package app.listeners;

import app.controllers.DiagramController;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class DiagramListener implements EventHandler<MouseEvent> {

    private final DiagramController diagramController;

    public DiagramListener(DiagramController diagramController) {
        this.diagramController = diagramController;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {

            Node picked = event.getPickResult().getIntersectedNode();

            System.out.println(picked.toString());
            //Check if mouse is pressing on state node
            if (picked instanceof Circle) {
                //Do nothing
                System.out.println("Testing");
            } else {
                //Add the state to diagram
                double X = event.getX(); // remove pane's coordinate system here
                double Y = event.getY(); // remove pane's coordinate system here

                diagramController.addStateToView(X, Y);

            }

        }
    }
}