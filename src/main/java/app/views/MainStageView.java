package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;

    private DiagramView diagramView;

    public MainStageView(MainStageController mainStageController) {

        this.mainStageController = mainStageController;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }


    private void setUpUIComponents() {

        Button button3 = new Button("Transition Table");
        Button button4 = new Button("Simulate");

        HBox topBar = new HBox(button3, button4);

        this.setTop(topBar);


        Button save = new Button("Save");
        Button load = new Button("Load");

        HBox bottomBar = new HBox(save, load);

        this.setBottom(bottomBar);

    }

    public DiagramView getDiagramView() {
        return diagramView;
    }

    private void setUpUILayout() {
    }

    private void setUpUIListeners() {

        MainStageListener mainStageListener = new MainStageListener(mainStageController);

    }


}
