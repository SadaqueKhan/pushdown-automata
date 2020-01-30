package app.views;

import app.controllers.DiagramController;
import app.controllers.StateController;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DiagramView extends Pane {

    //Reference to other stuff external files
    private final MainStageView mainStageView;

    private final DiagramController diagramController;
    private final StateController stateController;


    private HashSet<StateView> stateViewSet;
    private HashSet<TransitionView> transitionViewSet;

    private Map<String, StateView> stateMap;

    private ZoomableScrollPane scrollPane;


    public DiagramView(DiagramController diagramController, MainStageView mainStageView) {

        // Reference to the main application container
        this.mainStageView = mainStageView;

        this.diagramController = diagramController;
        this.stateController = new StateController(mainStageView);


        setUpComponents();
        setUpListeners();

    }


    private void setUpComponents() {
        // <--- Graph Stuff -->

        scrollPane = new ZoomableScrollPane(this);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // <--- End -->

        stateViewSet = new HashSet<>();

        transitionViewSet = new HashSet<>();

        stateMap = new HashMap<>(); // <id,cell>

        mainStageView.setCenter(this);

    }

    private void setUpListeners() {

        // Need to separate this out
        this.setOnMousePressed(e -> {


            if (e.getButton() == MouseButton.PRIMARY) {

                Node picked = e.getPickResult().getIntersectedNode();

                //Check if mouse is pressing on state node
                if (picked instanceof StateView) {
                    //No nothing
                } else {
                    double X = e.getX(); // remove pane's coordinate system here
                    double Y = e.getY(); // remove pane's coordinate system here

                    this.addStateView(X, Y);
//                this.addStateView(X, Y);
//                this.addEdge("Q0", "Q1");
                }

            }


        });

    }


    private void addStateView(double x, double y) {
        StateView stateView = new StateView(x, y, stateController);
        stateViewSet.add(stateView);

        stateMap.put(stateView.getStateId(), stateView);
        this.getChildren().add(stateView);
    }

    public void addEdge(String sourceId, String targetId) {

        //Get state from map using state ID
        StateView sourceCell = stateMap.get(sourceId);
        StateView targetCell = stateMap.get(targetId);

        //Create TransitionView
        TransitionView transitionView = new TransitionView(sourceCell, targetCell);

        //Add TransitionView to List
        transitionViewSet.add(transitionView);

    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }


}