package app.views;

import app.controllers.DiagramController;
import app.controllers.StateController;
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


        setUpComponents();
        setUpListeners();

    }


    private void setUpComponents() {
        // <--- Graph Stuff -->

        scrollPane = new ZoomableScrollPane(this);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


        stateMap = new HashMap<>(); // <id,cell>

        this.setStyle(cssLayout);
        mainStageView.setCenter(this);

    }

    private void setUpListeners() {

        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);

        this.setOnMousePressed(diagramListener);

    }

    public void addStateView(double x, double y, StateController stateController, String stateId) {

        StateView stateView = new StateView(x, y, stateController, stateId);

        this.getChildren().add(stateView);

        stateMap.put(stateId, stateView);
    }

    public void addEdge(String sourceId, String targetId) {

        //Get state from map using state ID
        StateView sourceCell = stateMap.get(sourceId);
        StateView targetCell = stateMap.get(targetId);

        //Create TransitionView
        TransitionView transitionView = new TransitionView(sourceCell, targetCell);

        this.getChildren().add(transitionView);
    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }


}