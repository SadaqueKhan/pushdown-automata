package app.models;

public class DiagramModel {

//    private app.views.DiagramListener graphParent;
//
//    private List<app.views.DiagramListener> allStates;
//    private List<app.views.DiagramListener> addedStates;
//    private List<app.views.DiagramListener> removedStates;
//
//    private List<TransitionView> allArrows;
//    private List<TransitionView> addedArrows;
//    private List<TransitionView> removedArrows;
//
//    private Map<String, app.views.DiagramListener> stateMap; // <id,cell>
//
//    public DiagramModel() {
//
//        graphParent = new app.views.DiagramListener("_ROOT_");
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
//        app.views.DiagramListener state = new app.views.DiagramListener(id);
//        addCell(state);
//    }
//
//    private void addCell(app.views.DiagramListener state) {
//
//        addedStates.add(state);
//
//        stateMap.put(state.getStateId(), state);
//
//    }
//
//    public void addEdge(String sourceId, String targetId) {
//
//        app.views.DiagramListener sourceCell = stateMap.get(sourceId);
//        app.views.DiagramListener targetCell = stateMap.get(targetId);
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
//    public void attachOrphansToGraphParent(List<app.views.DiagramListener> stateList) {
//
//        for (app.views.DiagramListener state : stateList) {
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
//    public void disconnectFromGraphParent(List<app.views.DiagramListener> stateList) {
//
//        for (app.views.DiagramListener state : stateList) {
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
//    public List<app.views.DiagramListener> getAddedStateViews() {
//        return addedStates;
//    }
//
//    public List<app.views.DiagramListener> getRemovedStateViews() {
//        return removedStates;
//    }
//
//    public List<DiagramListener> getAllStateViews() {
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
