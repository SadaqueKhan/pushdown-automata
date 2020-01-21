package app.models;

public class DiagramModel {

//    private app.views.StateListener graphParent;
//
//    private List<app.views.StateListener> allStates;
//    private List<app.views.StateListener> addedStates;
//    private List<app.views.StateListener> removedStates;
//
//    private List<TransitionView> allArrows;
//    private List<TransitionView> addedArrows;
//    private List<TransitionView> removedArrows;
//
//    private Map<String, app.views.StateListener> stateMap; // <id,cell>
//
//    public DiagramModel() {
//
//        graphParent = new app.views.StateListener("_ROOT_");
//
//        // clear model, create lists
//        clear();
//    }
//
//    public void clear() {
//
//        allStates = new ArrayList<>();
//        addedStates = new ArrayList<>();
//        removedStates = new ArrayList<>();
//
//        allArrows = new ArrayList<>();
//        addedArrows = new ArrayList<>();
//        removedArrows = new ArrayList<>();
//
//        stateMap = new HashMap<>(); // <id,cell>
//
//    }
//
//    public void clearAddedLists() {
//        addedStates.clear();
//        addedArrows.clear();
//    }
//
//
//    public void addCell(String id) {
//        app.views.StateListener state = new app.views.StateListener(id);
//        addCell(state);
//    }
//
//    private void addCell(app.views.StateListener state) {
//
//        addedStates.add(state);
//
//        stateMap.put(state.getStateId(), state);
//
//    }
//
//    public void addEdge(String sourceId, String targetId) {
//
//        app.views.StateListener sourceCell = stateMap.get(sourceId);
//        app.views.StateListener targetCell = stateMap.get(targetId);
//
//        TransitionView edge = new TransitionView(sourceCell, targetCell);
//
//        addedArrows.add(edge);
//
//    }
//
//
//    /**
//     * Attach all cells which don't have a parent to graphParent
//     *
//     * @param stateList
//     */
//    public void attachOrphansToGraphParent(List<app.views.StateListener> stateList) {
//
//        for (app.views.StateListener state : stateList) {
//            if (state.getStateParents().size() == 0) {
//                graphParent.addStateChild(state);
//            }
//        }
//    }
//
//    /**
//     * Remove the graphParent reference if it is set
//     *
//     * @param stateList
//     */
//    public void disconnectFromGraphParent(List<app.views.StateListener> stateList) {
//
//        for (app.views.StateListener state : stateList) {
//            graphParent.removeStateChild(state);
//        }
//    }
//
//    public void merge() {
//
//        // cells
//        allStates.addAll(addedStates);
//        allStates.removeAll(removedStates);
//
//        addedStates.clear();
//        removedStates.clear();
//
//        // edges
//        allArrows.addAll(addedArrows);
//        allArrows.removeAll(removedArrows);
//
//        addedArrows.clear();
//        removedArrows.clear();
//
//    }
//
//
//    public List<app.views.StateListener> getAddedStateViews() {
//        return addedStates;
//    }
//
//    public List<app.views.StateListener> getRemovedStateViews() {
//        return removedStates;
//    }
//
//    public List<StateListener> getAllStateViews() {
//        return allStates;
//    }
//
//    public List<TransitionView> getAddedTransitionViews() {
//        return addedArrows;
//    }
//
//    public List<TransitionView> getRemovedTransitionViews() {
//        return removedArrows;
//    }
//
//    public List<TransitionView> getAllTransitionViews() {
//        return allArrows;
//    }

}
