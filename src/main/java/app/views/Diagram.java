package app.views;

import app.controllers.StateController;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Diagram extends Pane {

    //Reference to other stuff external files
    private final StateController stateController;
    private final MainStageView mainStageView;


    private HashSet<StateView> stateViewSet;
    private HashSet<TransitionView> transitionViewSet;

    private Map<String, StateView> stateMap;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */

    private ZoomableScrollPane scrollPane;


    public Diagram(MainStageView mainStageView) {

        // Reference to the controller of this view
        this.stateController = new StateController(mainStageView);

        // Reference to the main application container
        this.mainStageView = mainStageView;


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

    }

    private void setUpListeners() {

        // Need to separate this out
        this.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                double X = e.getX(); // remove pane's coordinate system here
                double Y = e.getY(); // remove pane's coordinate system here

                this.addStateView(X, Y);
//                this.addStateView(X, Y);
//                this.addEdge("Q0", "Q1");
//
            }

//            } else if (e.getButton() == MouseButton.SECONDARY) {
//                // check if cicle was clicked and remove it if this is the case
//                Node picked = e.getPickResult().getIntersectedNode();
//
//
//
//
//
//
//                System.out.println(picked instanceof Circle);
//                if (picked instanceof Circle) {
//                    this.getChildren().remove(picked);
//                }
//            }
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