package app.models;

import app.views.Arrow;
import app.views.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diagram {

    private app.views.State graphParent;

    private List<app.views.State> allStates;
    private List<app.views.State> addedStates;
    private List<app.views.State> removedStates;

    private List<Arrow> allArrows;
    private List<Arrow> addedArrows;
    private List<Arrow> removedArrows;

    private Map<String, app.views.State> stateMap; // <id,cell>

    public Diagram() {

        graphParent = new app.views.State("_ROOT_");

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
        app.views.State state = new app.views.State(id);
        addCell(state);
    }

    private void addCell(app.views.State state) {

        addedStates.add(state);

        stateMap.put(state.getStateId(), state);

    }

    public void addEdge(String sourceId, String targetId) {

        app.views.State sourceCell = stateMap.get(sourceId);
        app.views.State targetCell = stateMap.get(targetId);

        Arrow edge = new Arrow(sourceCell, targetCell);

        addedArrows.add(edge);

    }


    /**
     * Attach all cells which don't have a parent to graphParent
     *
     * @param stateList
     */
    public void attachOrphansToGraphParent(List<app.views.State> stateList) {

        for (app.views.State state : stateList) {
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
    public void disconnectFromGraphParent(List<app.views.State> stateList) {

        for (app.views.State state : stateList) {
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


    public List<app.views.State> getAddedStates() {
        return addedStates;
    }

    public List<app.views.State> getRemovedStates() {
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
