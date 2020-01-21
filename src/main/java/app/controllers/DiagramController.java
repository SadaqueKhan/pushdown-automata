package app.controllers;

import app.views.MainStageView;
import app.views.StateView;

public class DiagramController {

    private final MainStageView mainStageView;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;

    public DiagramController(MainStageView mainStageView) {


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


}
