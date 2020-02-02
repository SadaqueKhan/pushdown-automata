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


    public DiagramController(MainStageView mainStageView, DiagramModel diagramModel) {

        this.mainStageView = mainStageView;
        this.diagramModel = diagramModel;


        this.diagramView = new DiagramView(this, mainStageView);
        this.transitionTableController = new TransitionTableController(diagramModel, diagramView);

    }


    public void addStateToView(double x, double y) {

        StateController newStateController = new StateController(diagramModel, diagramView, transitionTableController);

        StateModel newStateModel = new StateModel();

        diagramModel.addStateModel(newStateModel);


        diagramView.addStateView(x, y, newStateController, newStateModel.getStateId());


    }
}
