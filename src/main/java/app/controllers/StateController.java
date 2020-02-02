package app.controllers;

import app.models.DiagramModel;
import app.views.DiagramView;
import app.views.StateView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class StateController {

    private final DiagramModel diagramModel;
    private final DiagramView diagramView;

    private final TransitionTableController transitionTableController;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;

    public StateController(DiagramModel diagramModel, DiagramView diagramView, TransitionTableController transitionTableController) {
        this.diagramModel = diagramModel;
        this.diagramView = diagramView;
        this.transitionTableController = transitionTableController;
    }


    public void onMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        double scale = diagramView.getScale();

        dragContextX = stateView.getBoundsInParent().getMinX() * scale - xPositionOfMouse;
        dragContextY = stateView.getBoundsInParent().getMinY() * scale - yPositionOfMouse;

    }


    public void onMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        double scale = diagramView.getScale();

        double offsetX = xPositionOfMouse + dragContextX;
        double offsetY = yPositionOfMouse + dragContextY;

        offsetX /= scale;
        offsetY /= scale;

        stateView.relocate(offsetX, offsetY);
    }


    public void createPopup(StateView stateView) {
        //TODO need to MVC this
        ContextMenu contextMenu = new ContextMenu();
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");

        //TODO need to remove this listeners logic
        createTransitionItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                transitionTableController.load();
            }
        });

        deleteStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                diagramModel.removeStateModel(stateView.getStateId());
                diagramView.getChildren().remove(stateView);
            }
        });

        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);

    }
}
