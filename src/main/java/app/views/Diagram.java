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

    private State graphParent;

    private List<State> allStates;
    private List<State> addedStates;
    private List<State> removedStates;

    private List<Arrow> allArrows;
    private List<Arrow> addedArrows;
    private List<Arrow> removedArrows;

    private Map<String, State> stateMap; // <id,cell>


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

        graphParent = new State("_ROOT_");

        // clear model, create lists
        clear();

        setUpComponents();


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


        // <---- ENDUPDATE CODE -->


        // add components to graph pane
        cellLayer.getChildren().addAll(this.getAddedArrows());
        cellLayer.getChildren().addAll(this.getAddedStates());

        // remove components from graph pane
        cellLayer.getChildren().removeAll(this.getRemovedStates());
        cellLayer.getChildren().removeAll(this.getRemovedArrows());

        // enable dragging of cells
        for (State state : this.getAddedStates()) {
            mouseGestures.makeDraggable(state);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        this.attachOrphansToGraphParent(this.getAddedStates());

        // remove reference to graphParent
        this.disconnectFromGraphParent(this.getRemovedStates());

        // merge added & removed cells with all cells
        this.merge();

        // <--- End -->


    }


    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }


    public void clear() {

        allStates = new ArrayList<>();
        addedStates = new ArrayList<>();
        removedStates = new ArrayList<>();

        allArrows = new ArrayList<>();
        addedArrows = new ArrayList<>();
        removedArrows = new ArrayList<>();

        stateMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedStates.clear();
        addedArrows.clear();
    }


    private void addCell(String id) {

        State state = new State(id);
        addedStates.add(state);

        stateMap.put(state.getStateId(), state);

    }

    public void addEdge(String sourceId, String targetId) {

        State sourceCell = stateMap.get(sourceId);
        State targetCell = stateMap.get(targetId);

        Arrow edge = new Arrow(sourceCell, targetCell);

        addedArrows.add(edge);

    }


    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param stateList
     */
    public void attachOrphansToGraphParent(List<State> stateList) {

        for (State state : stateList) {
            if (state.getStateParents().size() == 0) {
                graphParent.addStateChild(state);
            }
        }
    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param stateList
     */
    public void disconnectFromGraphParent(List<State> stateList) {

        for (State state : stateList) {
            graphParent.removeStateChild(state);
        }
    }

    public void merge() {

        // cells
        allStates.addAll(addedStates);
        allStates.removeAll(removedStates);

        addedStates.clear();
        removedStates.clear();

        // edges
        allArrows.addAll(addedArrows);
        allArrows.removeAll(removedArrows);

        addedArrows.clear();
        removedArrows.clear();

    }


    public List<State> getAddedStates() {
        return addedStates;
    }

    public List<State> getRemovedStates() {
        return removedStates;
    }

    public List<State> getAllStates() {
        return allStates;
    }

    public List<Arrow> getAddedArrows() {
        return addedArrows;
    }

    public List<Arrow> getRemovedArrows() {
        return removedArrows;
    }

    public List<Arrow> getAllArrows() {
        return allArrows;
    }
}