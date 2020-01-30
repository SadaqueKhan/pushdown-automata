package app.controllers;

import app.views.DiagramView;
import app.views.MainStageView;
import app.views.StateView;

public class DiagramController {


    private final MainStageView mainStageView;
    private final DiagramView diagramView;


    public DiagramController(MainStageView mainStageView) {

        this.mainStageView = mainStageView;

        this.diagramView = new DiagramView(this, mainStageView);

    }

    public void addStateView(double x, double y) {

        StateController stateController = new StateController(mainStageView);
        StateView stateView = new StateView(x, y, stateController);
//
//        stateViewSet.add(stateView);
//
//        stateMap.put(stateView.getStateId(), stateView);
        diagramView.getChildren().add(stateView);
    }
}
