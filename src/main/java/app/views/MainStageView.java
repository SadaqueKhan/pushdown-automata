package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;

public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;


    //User input textfield fields
    private TextField inputTextField;
    private Set<String> inputWordSet;
    AutoCompletionBinding<String> autoCompletionBinding;

    public MainStageView(MainStageController mainStageController) {

        this.mainStageController = mainStageController;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }


    private void setUpUIComponents() {
        inputTextField = new TextField();
        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);

        this.setTop(inputTextField);


        ToggleButton toggleDiagramButton = new ToggleButton("Diagram");
        ToggleButton toggleTableButton = new ToggleButton("Table");

        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTableButton);


        this.setCenter(segmentedButton);

        Button button4 = new Button("Simulate");
        Button save = new Button("Save");
        Button load = new Button("Load");

        HBox bottomBar = new HBox(button4, save, load);

        this.setBottom(bottomBar);

    }

    private void learnWord(String text) {
        inputWordSet.add(inputTextField.getText());
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);

    }


    private void setUpUILayout() {
    }

    private void setUpUIListeners() {

        MainStageListener mainStageListener = new MainStageListener(mainStageController);

        inputTextField.setOnKeyPressed(mainStageListener);

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
