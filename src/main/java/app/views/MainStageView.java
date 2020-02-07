package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;


public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;


    private HBox centerContent;


    public MainStageView(MainStageController mainStageController) {

        this.mainStageController = mainStageController;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }


    private void setUpUIComponents() {


        Button button4 = new Button("Simulate");
        
        ToggleButton toggleDiagramButton = new ToggleButton("Diagram");
        ToggleButton toggleTableButton = new ToggleButton("Table");


        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTableButton);

        VBox topBar = new VBox(button4);

        this.setCenter(segmentedButton);


        this.centerContent = new HBox(toggleDiagramButton, toggleTableButton);

        this.setTop(centerContent);



        Button save = new Button("Save");
        Button load = new Button("Load");

        HBox bottomBar = new HBox(save, load);

        this.setBottom(bottomBar);

    }

    public HBox getCenterContent() {
        return centerContent;
    }

    private void setUpUILayout() {
    }

    private void setUpUIListeners() {

        MainStageListener mainStageListener = new MainStageListener(mainStageController);

    }


}
