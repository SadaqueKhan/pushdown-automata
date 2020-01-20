package app.model;

import app.view.Arrow;
import app.view.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diagram {

    private app.view.State graphParent;

    private List<app.view.State> allStates;
    private List<app.view.State> addedStates;
    private List<app.view.State> removedStates;

    private List<Arrow> allArrows;
    private List<Arrow> addedArrows;
    private List<Arrow> removedArrows;

    private Map<String, app.view.State> stateMap; // <id,cell>

    public Diagram() {

        graphParent = new app.view.State("_ROOT_");

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
        app.view.State state = new app.view.State(id);
        addCell(state);
    }

    private void addCell(app.view.State state) {

        addedStates.add(state);

        stateMap.put(state.getStateId(), state);

    }

    public void addEdge(String sourceId, String targetId) {

        app.view.State sourceCell = stateMap.get(sourceId);
        app.view.State targetCell = stateMap.get(targetId);

        Arrow edge = new Arrow(sourceCell, targetCell);

        addedArrows.add(edge);

    }


    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param stateList
     */
    public void attachOrphansToGraphParent(List<app.view.State> stateList) {

        for (app.view.State state : stateList) {
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
    public void disconnectFromGraphParent(List<app.view.State> stateList) {

        for (app.view.State state : stateList) {
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


    public List<app.view.State> getAddedStates() {
        return addedStates;
    }

    public List<app.view.State> getRemovedStates() {
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
