package app.views;

import app.controllers.StateController;
import app.listeners.StateListener;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Diagram extends Pane {

    //Reference to other stuff external files
    private final StateController stateController;
    private final MainStageView mainStageView;

    private HashSet<StateView> addedStateViews;
    private HashSet<TransitionView> addedTransitionViews;

    private Map<String, StateView> stateMap;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    private Pane cellLayer;
    private Group canvas;
    private ZoomableScrollPane scrollPane;


    public Diagram(StateController stateController, MainStageView mainStageView) {

        // Reference to the controller of this view
        this.stateController = stateController;

        // Reference to the main application container
        this.mainStageView = mainStageView;


        setUpComponents();
//        setUpListeners();
    }


    private void setUpComponents() {
        // <--- Graph Stuff -->
        canvas = new Group();
        cellLayer = new Pane();


        scrollPane = new ZoomableScrollPane(this);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // <--- End -->

        addedStateViews = new HashSet<>();

        addedTransitionViews = new HashSet<>();

        stateMap = new HashMap<>(); // <id,cell>


//        Pane pane = new Pane();
        double radius = 30;
        this.setOnMouseClicked(e -> {
            System.out.println("helloworld");
            if (e.getButton() == MouseButton.PRIMARY) {
                double X = e.getX(); // remove pane's coordinate system here
                double Y = e.getY(); // remove pane's coordinate system here

                System.out.println("X coordinate=" + X + "Y coordinate");
                Circle circle = new Circle(X, Y, radius);
                circle.setFill(javafx.scene.paint.Color.RED);
                circle.setStroke(Color.BLACK);

                this.getChildren().add(circle);

            } else if (e.getButton() == MouseButton.SECONDARY) {
                // check if cicle was clicked and remove it if this is the case
                Node picked = e.getPickResult().getIntersectedNode();
                if (picked instanceof Circle) {
                    this.getChildren().remove(picked);
                }
            }
        });


//        this.addStateView("Cell A");
//        this.addStateView("Cell B");
//        this.addStateView("Cell C");
//
//
//        this.addEdge("Cell A", "Cell B");
//        this.addEdge("Cell B", "Cell C");
//
//
//        cellLayer.getChildren().addAll(this.getAddedTransitionViews());
//        cellLayer.getChildren().addAll(this.getAddedStateViews());
//

    }

    private void setUpListeners() {

        //Create listener for this view
        StateListener stateListener = new StateListener(stateController);

        // enable dragging of cells
        for (StateView stateView : this.getAddedStateViews()) {
            stateView.setOnMousePressed(stateListener);
            stateView.setOnMouseDragged(stateListener);
        }
    }


    private void addStateView(String id) {
        StateView stateView = new StateView(id);
        addedStateViews.add(stateView);

        stateMap.put(stateView.getStateId(), stateView);
    }

    public void addEdge(String sourceId, String targetId) {

        //Get state from map using state ID
        StateView sourceCell = stateMap.get(sourceId);
        StateView targetCell = stateMap.get(targetId);

        //Create TransitionView
        TransitionView transitionView = new TransitionView(sourceCell, targetCell);

        //Add TransitionView to List
        addedTransitionViews.add(transitionView);

    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }


    public HashSet<StateView> getAddedStateViews() {
        return addedStateViews;
    }


    public HashSet<TransitionView> getAddedTransitionViews() {
        return addedTransitionViews;
    }


}