package app.model;

import app.view.Edge;
import app.view.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model {

    State graphParent;

    List<State> allCells;
    List<State> addedCells;
    List<State> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String, State> cellMap; // <id,cell>

    public Model() {

        graphParent = new State("_ROOT_");

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<State> getAddedCells() {
        return addedCells;
    }

    public List<State> getRemovedCells() {
        return removedCells;
    }

    public List<State> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(String id) {
        State state = new State(id);
        addCell(state);
    }

    private void addCell(State state) {

        addedCells.add(state);

        cellMap.put(state.getCellId(), state);

    }

    public void addEdge(String sourceId, String targetId) {

        State sourceCell = cellMap.get(sourceId);
        State targetCell = cellMap.get(targetId);

        Edge edge = new Edge(sourceCell, targetCell);

        addedEdges.add(edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param cellList
     */
    public void attachOrphansToGraphParent(List<State> cellList) {

        for (State state : cellList) {
            if (state.getCellParents().size() == 0) {
                graphParent.addCellChild(state);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     *
     * @param cellList
     */
    public void disconnectFromGraphParent(List<State> cellList) {

        for (State state : cellList) {
            graphParent.removeCellChild(state);
        }
    }

    public void merge() {

        // cells
        allCells.addAll(addedCells);
        allCells.removeAll(removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll(addedEdges);
        allEdges.removeAll(removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}