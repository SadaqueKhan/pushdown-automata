package app.view;


import app.controller.MainStageController;
import app.listeners.MainStageListener;
import app.model.Model;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


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

        Button button1 = new Button("Button Number 1");
        Button button2 = new Button("Button Number 2");

        HBox hbox = new HBox(button1, button2);

        this.setTop(hbox);

        this.graph = new Graph();

        this.setCenter(graph.getScrollPane());

        addGraphComponents();

    }


    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A");
        model.addCell("Cell B");
        model.addCell("Cell C");
        model.addCell("Cell D");
        model.addCell("Cell E");
        model.addCell("Cell F");
        model.addCell("Cell G");

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
