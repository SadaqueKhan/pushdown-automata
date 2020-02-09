package app.views;

import app.controllers.DiagramController;
import app.listeners.DiagramListener;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class DiagramView extends Pane {

    //Reference to other stuff external files
    private final MainStageView mainStageView;

    private final DiagramController diagramController;

    private Map<String, StateView> stateMap;

    private ZoomableScrollPane scrollPane;

    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-background-color: aliceblue,\n" +
            "linear-gradient(from 0.5px 0.0px to 10.5px  0.0px, repeat, black 5%, transparent 5%),\n" +
            "linear-gradient(from 0.0px 0.5px to  0.0px 10.5px, repeat, black 5%, transparent 5%)";

    public DiagramView(DiagramController diagramController, MainStageView mainStageView) {

        // Reference to the main application container
        this.mainStageView = mainStageView;

        this.diagramController = diagramController;

        setUpUIComponents();
        setUpUIListeners();

    }

    public void loadToMainStage() {
        mainStageView.getContainerForCenterNodes().getChildren().add(this);
    }


    private void setUpUIComponents() {
        // <--- Graph Stuff -->

        scrollPane = new ZoomableScrollPane(this);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        stateMap = new HashMap<>(); // <id,cell>

        this.setStyle(cssLayout);

        this.setMinSize(200, 500);

        loadToMainStage();
    }

    private void setUpUIListeners() {
        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);
    }

    public void addStateView(double x, double y, DiagramController diagramController, String stateID) {

        StateView stateView = new StateView(x, y, diagramController, stateID);

        this.getChildren().add(stateView);

        stateMap.put(stateID, stateView);
    }

    public void addTransitionView(String sourceID, String targetID, String transitionsID) {
        //Get state from map using state ID
        StateView sourceCell = stateMap.get(sourceID);
        StateView targetCell = stateMap.get(targetID);

        //Create TransitionView
        TransitionView transitionView = new TransitionView(sourceCell, targetCell, transitionsID);

        this.getChildren().add(transitionView);
    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }


    @Override
    public String toString() {
        return "DiagramView";
    }


}