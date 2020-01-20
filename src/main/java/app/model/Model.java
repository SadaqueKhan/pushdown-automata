package app.model;

import app.view.Arrow;
import app.view.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Model {

    private State graphParent;

    private List<State> allStates;
    private List<State> addedStates;
    private List<State> removedStates;

    private List<Arrow> allArrows;
    private List<Arrow> addedArrows;
    private List<Arrow> removedArrows;

    private Map<String, State> stateMap; // <id,cell>

    public Model() {

        graphParent = new State("_ROOT_");

        // clear model, create lists
        clear();
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


    public void addCell(String id) {
        State state = new State(id);
        addCell(state);
    }

    private void addCell(State state) {

        addedStates.add(state);

        stateMap.put(state.getCellId(), state);

    }

    public void addEdge(String sourceId, String targetId) {

        State sourceCell = stateMap.get(sourceId);
        State targetCell = stateMap.get(targetId);

        Arrow edge = new Arrow(sourceCell, targetCell);

        addedArrows.add(edge);

    }


    /**
     * Attach all cells which don't have a parent to graphParent
     * @param stateList
     */
    public void attachOrphansToGraphParent(List<State> stateList) {

        for (State state : stateList) {
            if (state.getCellParents().size() == 0) {
                graphParent.addCellChild(state);
            }
        }
    }

    /**
     * Remove the graphParent reference if it is set
     * @param stateList
     */
    public void disconnectFromGraphParent(List<State> stateList) {

        for (State state : stateList) {
            graphParent.removeCellChild(state);
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