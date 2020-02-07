package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;

public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;

    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: solid;\n";
    private Set<String> inputWordSet;
    AutoCompletionBinding<String> autoCompletionBinding;
    //Top content elements
    private TextField inputTextField;
    //Center content elements
    private BorderPane borderPane;
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTableButton;


    public MainStageView(MainStageController mainStageController) {

        this.mainStageController = mainStageController;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        //Setup top GUI elements
        inputTextField = new TextField();
        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);

        this.setTop(inputTextField);
        
        //Setup center GUI elements
        this.toggleDiagramButton = new ToggleButton("Diagram");
        this.toggleTableButton = new ToggleButton("Table");

        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTableButton);

        VBox vBox = new VBox(segmentedButton);
        vBox.setAlignment(Pos.TOP_CENTER);

        toggleDiagramButton.setSelected(true);

        this.setCenter(vBox);

        Button button4 = new Button("Simulate");
        Button save = new Button("Save");
        Button load = new Button("Load");

        HBox bottomBar = new HBox(button4, save, load);

        this.setBottom(bottomBar);

    }

    private void setUpUILayout() {
        this.setStyle(cssLayout);
    }

    private void setUpUIListeners() {
        MainStageListener mainStageListener = new MainStageListener(mainStageController);

        inputTextField.setOnKeyPressed(mainStageListener);

        toggleDiagramButton.setOnAction(mainStageListener);
        toggleTableButton.setOnAction(mainStageListener);

    }

    public TextField getInputTextField() {
        return inputTextField;
    }

    public Set<String> getInputWordSet() {
        return inputWordSet;
    }

    public AutoCompletionBinding<String> getAutoCompletionBinding() {
        return autoCompletionBinding;
    }

    public void setAutoCompletionBinding(AutoCompletionBinding<String> autoCompletionBinding) {
        this.autoCompletionBinding = autoCompletionBinding;
    }
}
