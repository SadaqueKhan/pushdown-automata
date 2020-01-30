package app.controllers;

import app.models.DiagramModel;
import app.models.StateModel;
import app.views.DiagramView;
import app.views.MainStageView;

public class DiagramController {


    private final MainStageView mainStageView;
    private final DiagramView diagramView;

    private final DiagramModel diagramModel;

    private final TransitionTableController transitionTableController;


    public DiagramController(MainStageView mainStageView, DiagramModel diagramModel, TransitionTableController transitionTableController) {

        this.mainStageView = mainStageView;
        this.diagramModel = diagramModel;

        this.transitionTableController = transitionTableController;

        this.diagramView = new DiagramView(this, mainStageView);

    }


    public void addStateToView(double x, double y) {

        StateController newStateController = new StateController(mainStageView, diagramView, transitionTableController);

        StateModel newStateModel = new StateModel();

        diagramModel.addStateModel(newStateModel);


        diagramView.addStateView(x, y, newStateController, newStateModel.getStateId());

    }
}
