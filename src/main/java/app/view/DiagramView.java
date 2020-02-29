package app.view;

import app.controller.DiagramController;
import app.listener.DiagramListener;
import javafx.scene.layout.Pane;

public class DiagramView extends Pane {

    //Reference to other stuff external files
    private final MainStageView mainStageView;

    private final DiagramController diagramController;

    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-background-color: whitesmoke,\n" +
            "linear-gradient(from 0.5px 0.0px to 10.5px  0.0px, repeat, black 5%, transparent 5%),\n" +
            "linear-gradient(from 0.0px 0.5px to  0.0px 10.5px, repeat, black 5%, transparent 5%)";

    public DiagramView(DiagramController diagramController, MainStageView mainStageView) {
        // Reference to the main application container
        this.mainStageView = mainStageView;
        // Reference to diagram controller
        this.diagramController = diagramController;


        setUpUIComponents();
        setUpUIListeners();
    }


    private void setUpUIComponents() {
        this.setStyle(cssLayout);
        this.setMinSize(200, 500);
    }

    private void setUpUIListeners() {
        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);
        this.setOnMousePressed(diagramListener);
    }


    @Override
    public String toString() {
        return "DiagramView";
    }
}