package app.view;


import app.model.Model;
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
    private Model model;
    private Group canvas;
    private ZoomableScrollPane scrollPane;

    public Graph() {

        this.model = new Model();

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

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedArrows());
        getCellLayer().getChildren().addAll(model.getAddedStates());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedStates());
        getCellLayer().getChildren().removeAll(model.getRemovedArrows());

        // enable dragging of cells
        for (State state : model.getAddedStates()) {
            mouseGestures.makeDraggable(state);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedStates());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedStates());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }
}