package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;

public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;


    private HBox centerContent;

    AutoCompletionBinding<String> autoCompletionBinding;
    private TextField input;
    private Set<String> inputWordSet;

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

        inputWordSet = new HashSet<>();


        input = new TextField();
        autoCompletionBinding = TextFields.bindAutoCompletion(input, inputWordSet);


        input.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                learnWord(input.getText());
            }
        });

        this.setTop(input);

//        TextField textField = new TextField();
//     textField.bindAutoCompletion(input, poss)

        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTableButton);


        this.setCenter(segmentedButton);


        Button save = new Button("Save");
        Button load = new Button("Load");

        HBox bottomBar = new HBox(button4, save, load);

        this.setBottom(bottomBar);

    }

    private void learnWord(String text) {
        inputWordSet.add(input.getText());
        if (autoCompletionBinding != null) {
            autoCompletionBinding.dispose();
        }
        autoCompletionBinding = TextFields.bindAutoCompletion(input, inputWordSet);

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
