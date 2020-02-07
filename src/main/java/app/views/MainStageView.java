package app.views;


import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTableButton;
    private SegmentedButton segmentedButton;
    private VBox containerForCenterNodes;

    public MainStageView(MainStageController mainStageController) {

        this.mainStageController = mainStageController;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        //Setup top GUI elements
        inputTextField = new TextField();
        inputTextField.setPromptText("Enter input word");
        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.getChildren().addAll(new Text("Input Word"), inputTextField);

        this.setTop(containerForTopNodes);

        //Setup center GUI elements
        this.toggleDiagramButton = new ToggleButton("Diagram");
        this.toggleTableButton = new ToggleButton("Table");


        this.segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTableButton);

        HBox hBox = new HBox(segmentedButton);


        toggleDiagramButton.setSelected(true);

        this.containerForCenterNodes = new VBox();
        containerForCenterNodes.setPadding(new Insets(10, 50, 50, 50));
        containerForCenterNodes.setSpacing(10);
        containerForCenterNodes.setAlignment(Pos.TOP_CENTER);
        containerForCenterNodes.setStyle(cssLayout);

        containerForCenterNodes.getChildren().add(segmentedButton);


        this.setCenter(containerForCenterNodes);

        Accordion accordion = new Accordion();

        TitledPane pane1 = new TitledPane("Boats", new Label("Show all boats available"));
        TitledPane pane2 = new TitledPane("Cars", new Label("Show all cars available"));
        TitledPane pane3 = new TitledPane("Planes", new Label("Show all planes available"));

        TitledPane pane4 = new TitledPane("Boats", new Label("Show all boats available"));
        TitledPane pane5 = new TitledPane("Cars", new Label("Show all cars available"));
        TitledPane pane6 = new TitledPane("Planes", new Label("Show all planes available"));

        accordion.getPanes().add(pane1);
        accordion.getPanes().add(pane2);
        accordion.getPanes().add(pane3);
        accordion.getPanes().add(pane4);
        accordion.getPanes().add(pane5);
        accordion.getPanes().add(pane6);

        accordion.setMinSize(200, 200);

        this.setBottom(accordion);

    }

    private void setUpUILayout() {

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

    public VBox getContainerForCenterNodes() {
        return containerForCenterNodes;
    }
}
