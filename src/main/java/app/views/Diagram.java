package app.views;

import app.listeners.MouseGestures;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Diagram {

    private StateView graphParent;

    private List<StateView> allStateViews;
    private List<StateView> addedStateViews;
    private List<StateView> removedStateViews;

    private List<TransitionView> allTransitionViews;
    private List<TransitionView> addedTransitionViews;
    private List<TransitionView> removedTransitionViews;

    private Map<String, StateView> stateMap; // <id,cell>


    //newStuff
    private MouseGestures mouseGestures;
    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    private Pane cellLayer;
    private Group canvas;
    private ZoomableScrollPane scrollPane;


    public Diagram() {

        graphParent = new StateView("_ROOT_");

        // clear model, create lists
        clear();

        setUpComponents();
        setUpListeners();


    }


    private void setUpComponents() {
        // <--- Graph Stuff -->
        canvas = new Group();
        cellLayer = new Pane();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // <--- End -->


        // <--- MainStage --->
        this.addCell("Cell A");
        this.addCell("Cell B");
        this.addCell("Cell C");
        this.addCell("Cell D");
        this.addCell("Cell E");
        this.addCell("Cell F");
        this.addCell("Cell G");

        this.addEdge("Cell A", "Cell B");
        this.addEdge("Cell A", "Cell C");
        this.addEdge("Cell B", "Cell C");
        this.addEdge("Cell C", "Cell D");
        this.addEdge("Cell B", "Cell E");
        this.addEdge("Cell D", "Cell F");
        this.addEdge("Cell D", "Cell G");
        // <--- End --->


        // add components to graph pane
        cellLayer.getChildren().addAll(this.getAddedStateViews());
        cellLayer.getChildren().addAll(this.getAddedTransitionViews());

        for (StateView stateView : this.getAddedStateViews()) {
            mouseGestures.makeDraggable(stateView);
        }

        for (StateView stateView : this.getAddedStateViews()) {

            if (stateView.getStateParents().size() == 0) {
                graphParent.addStateChild(stateView);
            }

        }

        for (StateView stateView : this.getRemovedStateViews()) {
            graphParent.removeStateChild(stateView);
        }


        // merge added & removed cells with all cells
        this.merge();

        // <--- End -->


    }

    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param stateViewList
     */
    public void attachOrphansToGraphParent(List<StateView> stateViewList) {

        for (StateView stateView : stateViewList) {
            if (stateView.getStateParents().size() == 0) {
                graphParent.addStateChild(stateView);
            }
        }
    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param stateViewList
     */
    public void disconnectFromGraphParent(List<StateView> stateViewList) {

        for (StateView stateView : stateViewList) {
            graphParent.removeStateChild(stateView);
        }
    }








    private void setUpListeners() {

        System.out.println("HELLOWORLD");
        // enable dragging of cells
        for (StateView stateView : this.getAddedStateViews()) {
            mouseGestures.makeDraggable(stateView);

        }


    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }


    public void clear() {

        allStateViews = new ArrayList<>();
        addedStateViews = new ArrayList<>();
        removedStateViews = new ArrayList<>();

        allTransitionViews = new ArrayList<>();
        addedTransitionViews = new ArrayList<>();
        removedTransitionViews = new ArrayList<>();

        stateMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedStateViews.clear();
        addedTransitionViews.clear();
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


    public void merge() {

        // cells
        allStateViews.addAll(addedStateViews);
        allStateViews.removeAll(removedStateViews);

        addedStateViews.clear();
        removedStateViews.clear();

        // edges
        allTransitionViews.addAll(addedTransitionViews);
        allTransitionViews.removeAll(removedTransitionViews);

        addedTransitionViews.clear();
        removedTransitionViews.clear();

    }


    public List<StateView> getAddedStateViews() {
        return addedStateViews;
    }

    public List<StateView> getRemovedStateViews() {
        return removedStateViews;
    }

    public List<StateView> getAllStateViews() {
        return allStateViews;
    }

    public List<TransitionView> getAddedTransitionViews() {
        return addedTransitionViews;
    }

    public List<TransitionView> getRemovedTransitionViews() {
        return removedTransitionViews;
    }

    public List<TransitionView> getAllTransitionViews() {
        return allTransitionViews;
    }
}