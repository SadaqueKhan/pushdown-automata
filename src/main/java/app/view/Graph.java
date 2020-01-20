package app.view;


import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Graph {

    private MouseGestures mouseGestures;
    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    private Pane cellLayer;
    private Diagram diagram;
    private Group canvas;
    private ZoomableScrollPane scrollPane;

    public Graph() {

        this.diagram = new Diagram();

        canvas = new Group();
        cellLayer = new Pane();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);


    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Diagram getDiagram() {
        return diagram;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(diagram.getAddedArrows());
        getCellLayer().getChildren().addAll(diagram.getAddedStates());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(diagram.getRemovedStates());
        getCellLayer().getChildren().removeAll(diagram.getRemovedArrows());

        // enable dragging of cells
        for (State state : diagram.getAddedStates()) {
            mouseGestures.makeDraggable(state);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getDiagram().attachOrphansToGraphParent(diagram.getAddedStates());

        // remove reference to graphParent
        getDiagram().disconnectFromGraphParent(diagram.getRemovedStates());

        // merge added & removed cells with all cells
        getDiagram().merge();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
}