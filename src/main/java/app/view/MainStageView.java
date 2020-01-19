package app.view;


import app.controller.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.layout.BorderPane;


public class MainStageView extends BorderPane {


    private final MainStageController mainStageController;


    private Graph graph;


    public MainStageView(MainStageController mainStageController) {


        this.mainStageController = mainStageController;


        setUpComponents();

        setUpLayout();

        setUpListeners();

    }


    private void setUpComponents() {

        this.graph = new Graph();

        this.setCenter(graph.getScrollPane());

        addGraphComponents();

        Layout layout = new RandomLayout(graph);
        layout.execute();
    }


    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.STATE);
        model.addCell("Cell B", CellType.STATE);
        model.addCell("Cell C", CellType.STATE);
        model.addCell("Cell D", CellType.STATE);
        model.addCell("Cell E", CellType.STATE);
        model.addCell("Cell F", CellType.STATE);
        model.addCell("Cell G", CellType.STATE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();

    }


    private void setUpLayout() {


    }


    private void setUpListeners() {

        MainStageListener mainStageListener = new MainStageListener(mainStageController);

    }


}
