package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.views.DiagramView;
import app.views.MainStageView;
import app.views.StateView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class DiagramController {

    private final MainStageView mainStageView;
    private final MainStageController mainStageController;
    private final MachineModel machineModel;

    private final TransitionTableController transitionTableController;

    private final DiagramView diagramView;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;

    public DiagramController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {

        this.mainStageView = mainStageView;
        this.mainStageController = mainStageController;
        this.machineModel = machineModel;

        this.transitionTableController = mainStageController.getTransitionTableController();

        this.diagramView = new DiagramView(this, mainStageView);
    }

    //DiagramPaneGUIEventResponses
    public void addStateToViewMouseEventResponse(double x, double y) {
        StateModel newStateModel = new StateModel();

        machineModel.addStateModel(newStateModel);
        diagramView.addStateView(x, y, this, newStateModel.getStateId());
    }


//StateGUIEventResponses

    public void stateViewOnMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        double scale = diagramView.getScale();

        dragContextX = stateView.getBoundsInParent().getMinX() * scale - xPositionOfMouse;
        dragContextY = stateView.getBoundsInParent().getMinY() * scale - yPositionOfMouse;

    }


    public void stateViewOnMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        double scale = diagramView.getScale();

        double offsetX = xPositionOfMouse + dragContextX;
        double offsetY = yPositionOfMouse + dragContextY;

        offsetX /= scale;
        offsetY /= scale;

        if (!(stateView.getStateParents().isEmpty())) {
            //Target

            for (StateView stateView1 : stateView.getStateParents()) {

                if (stateView1.getCenterX() < offsetX) {

                    System.out.println("----");
                    System.out.println("TargetXONLEFT:" + offsetX);
                    System.out.println("SourceYONRIGHT:" + stateView1.getCenterX());
                    System.out.println("----");
                }

            }
        }

        stateView.setCenterX(offsetX);
        stateView.setCenterY(offsetY);
        stateView.relocate(offsetX, offsetY);

    }


    public void stateViewCreateContextMenuPopUp(StateView stateView) {

        StateModel stateModel = machineModel.getStateModel(stateView.getStateId());

        //TODO need to MVC this
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");

        toggleStandardStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stateView.setUpStandardStateUIComponents();
                stateModel.setType("standard");
            }
        });

        toggleStartStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stateView.setUpStartStateUIComponent();
                stateModel.setType("start");
            }
        });

        //TODO need to remove this listeners logic
        toggleFinalStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                stateView.setUpFinalStateUIComponent();
                stateModel.setType("final");
            }
        });


        //TODO need to remove this listeners logic
        createTransitionItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                transitionTableController.load();
            }
        });

        deleteStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                machineModel.removeStateModel(stateView.getStateId());
                diagramView.getChildren().remove(stateView);
            }
        });

        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);

    }


    //TransitionTableGUIEventResponses
    public void addStateToViewTransitionTableInputEventResponse(double x, double y, String userEntryCurrentStateId) {
        StateModel newStateModel = new StateModel();

        machineModel.addStateModel(newStateModel);
        diagramView.addStateView(x, y, this, newStateModel.getStateId());
    }


    public void addTransitionToViewTransitionTableEventResponse(String sourceStateID, String targetStateID, String transitionsID) {
        diagramView.addTransitionView(sourceStateID, targetStateID, transitionsID);
    }


}
