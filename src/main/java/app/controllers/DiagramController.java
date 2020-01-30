package app.controllers;

import app.views.DiagramView;
import app.views.MainStageView;

public class DiagramController {


    private final MainStageView mainStageView;
    private final DiagramView diagramView;


    public DiagramController(MainStageView mainStageView) {

        this.mainStageView = mainStageView;

        this.diagramView = new DiagramView(this, mainStageView);

    }


    public void addStateToView(double x, double y) {

        StateController stateController = new StateController(mainStageView, diagramView);

        diagramView.addStateToView(x, y, stateController);

    }
}
