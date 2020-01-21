package app.views;

import app.controllers.DiagramController;
import app.listeners.DiagramListener;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Diagram {

    //Reference to other stuff external files
    private final DiagramController diagramController;
    private final MainStageView mainStageView;

    private List<StateView> addedStateViews;
    private List<TransitionView> addedTransitionViews;

    private Map<String, StateView> stateMap;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    private Pane cellLayer;
    private Group canvas;
    private ZoomableScrollPane scrollPane;


    public Diagram(DiagramController diagramController, MainStageView mainStageView) {

        // Reference to the controller of this view
        this.diagramController = diagramController;

        // Reference to the main application container
        this.mainStageView = mainStageView;


        setUpComponents();
        setUpListeners();
    }


    private void setUpComponents() {
        // <--- Graph Stuff -->
        canvas = new Group();
        cellLayer = new Pane();

        canvas.getChildren().add(cellLayer);
        
        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // <--- End -->

        addedStateViews = new ArrayList<>();

        addedTransitionViews = new ArrayList<>();

        stateMap = new HashMap<>(); // <id,cell>


        this.addCell("Cell A");
        this.addCell("Cell B");
        this.addCell("Cell C");
        this.addCell("Cell D");
        this.addCell("Cell E");
        this.addCell("Cell F");
        this.addCell("Cell G");

        this.addEdge("Cell A", "Cell B");
        this.addEdge("Cell B", "Cell A");
        this.addEdge("Cell B", "Cell C");
        this.addEdge("Cell C", "Cell D");
        this.addEdge("Cell B", "Cell E");
        this.addEdge("Cell D", "Cell F");
        this.addEdge("Cell D", "Cell G");


        cellLayer.getChildren().addAll(this.getAddedStateViews());
        cellLayer.getChildren().addAll(this.getAddedTransitionViews());

    }


    private void setUpListeners() {

        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);

        // enable dragging of cells
        for (StateView stateView : this.getAddedStateViews()) {
            stateView.setOnMousePressed(diagramListener);
            stateView.setOnMouseDragged(diagramListener);
            stateView.setOnMouseReleased(diagramListener);
        }
    }


    private void addCell(String id) {

        StateView stateView = new StateView(id);
        addedStateViews.add(stateView);

        stateMap.put(stateView.getStateId(), stateView);

    }

    public void addEdge(String sourceId, String targetId) {

        StateView sourceCell = stateMap.get(sourceId);
        StateView targetCell = stateMap.get(targetId);

        TransitionView edge = new TransitionView(sourceCell, targetCell);

        addedTransitionViews.add(edge);

    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }


    public List<StateView> getAddedStateViews() {
        return addedStateViews;
    }


    public List<TransitionView> getAddedTransitionViews() {
        return addedTransitionViews;
    }


}