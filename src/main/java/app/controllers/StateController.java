package app.controllers;

import app.views.MainStageView;
import app.views.StateView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class StateController {

    private final MainStageView mainStageView;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;

    public StateController(MainStageView mainStageView) {

        this.mainStageView = mainStageView;

    }

    public void onMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {

        //TODO Remove this coupling
        double scale = mainStageView.getDiagram().getScale();

        dragContextX = stateView.getBoundsInParent().getMinX() * scale - xPositionOfMouse;
        dragContextY = stateView.getBoundsInParent().getMinY() * scale - yPositionOfMouse;

    }


    public void onMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {

        //TODO Remove this coupling
        double scale = mainStageView.getDiagram().getScale();

        double offsetX = xPositionOfMouse + dragContextX;
        double offsetY = yPositionOfMouse + dragContextY;

        offsetX /= scale;
        offsetY /= scale;

        stateView.relocate(offsetX, offsetY);
    }


    public void createPopup(StateView stateView) {

        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                mainStageView.getDiagram().getChildren().remove(stateView);
            }
        });
        contextMenu.getItems().add(deleteItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);


    }
}
