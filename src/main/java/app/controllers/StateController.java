package app.controllers;

import app.views.DiagramView;
import app.views.MainStageView;
import app.views.StateView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class StateController {

    private final MainStageView mainStageView;
    private final DiagramView diagramView;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;

    public StateController(MainStageView mainStageView, DiagramView diagramView) {

        this.mainStageView = mainStageView;
        this.diagramView = diagramView;

    }


    public void onMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {

        //TODO Remove this coupling
        double scale = diagramView.getScale();

        dragContextX = stateView.getBoundsInParent().getMinX() * scale - xPositionOfMouse;
        dragContextY = stateView.getBoundsInParent().getMinY() * scale - yPositionOfMouse;

    }


    public void onMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {



        //TODO Remove this coupling
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
        MenuItem createTransition = new MenuItem("Add transition");
        MenuItem deleteItem = new MenuItem("Delete");


        //TODO need to remove this listeners logic
        createTransition.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                TransitionTableController transitionTableController = new TransitionTableController();
            }
        });


        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                diagramView.getChildren().remove(stateView);
            }
        });


        contextMenu.getItems().add(createTransition);
        contextMenu.getItems().add(deleteItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);


    }
}
