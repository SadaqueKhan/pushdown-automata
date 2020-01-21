package app.views;


import app.controllers.DiagramController;
import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class MainStageView extends BorderPane {


    private final MainStageController mainStageController;


    private Diagram diagram;

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

        //TODO Remove controller creation from here
        DiagramController diagramController = new DiagramController(this);

        //TODO Need to de-couple mainStageController.getDiagramController()
        this.diagram = new Diagram(diagramController, this);

        this.setCenter(diagram.getScrollPane());


    }

    public Diagram getDiagram() {
        return diagram;
    }

    private void setUpLayout() {


    }


    private void setUpListeners() {

        MainStageListener mainStageListener = new MainStageListener(mainStageController);

    }


}
