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
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: solid;\n";

    public DiagramView(DiagramController diagramController, MainStageView mainStageView) {

        // Reference to the main application container
        this.mainStageView = mainStageView;

        this.diagramController = diagramController;


        setUpUIComponents();
        setUpUIListeners();

    }


    private void setUpUIComponents() {
        // <--- Graph Stuff -->

        scrollPane = new ZoomableScrollPane(this);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        stateMap = new HashMap<>(); // <id,cell>

        this.setStyle(cssLayout);
        mainStageView.setCenter(this);

    }

    private void setUpUIListeners() {

        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);

        this.setOnMousePressed(diagramListener);

    }

    public void addStateView(double x, double y, DiagramController diagramController, String stateId) {

        StateView stateView = new StateView(x, y, diagramController, stateId);

        this.getChildren().add(stateView);

        stateMap.put(stateId, stateView);
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