package app.views;

import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;

public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;

    //Top content elements
    private TextField inputTextField;
    AutoCompletionBinding<String> autoCompletionBinding;
    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: solid;\n";
    //Center content elements
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTransitionTableButton;
    private SegmentedButton segmentedButton;
    private VBox containerForCenterNodes;
    private Set<String> inputWordSet;
    private ProgressBar progressBar;
    //Bottom content elements
    private TapeView tapeView;
    private VBox containerForBotoomNodes;

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
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setVisible(false);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.setFillWidth(true);
        containerForTopNodes.getChildren().addAll(new Text("Input Word"), inputTextField, progressBar);
        this.setTop(containerForTopNodes);

        //Setup center GUI elements
        this.toggleDiagramButton = new ToggleButton("Diagram");
        this.toggleTransitionTableButton = new ToggleButton("Table");
        this.segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTransitionTableButton);
        toggleDiagramButton.setSelected(true);

        this.containerForCenterNodes = new VBox();
        containerForCenterNodes.setPadding(new Insets(10, 50, 50, 50));
        containerForCenterNodes.setSpacing(10);
        containerForCenterNodes.setAlignment(Pos.TOP_CENTER);
        containerForCenterNodes.setStyle(cssLayout);
        containerForCenterNodes.getChildren().add(segmentedButton);

        this.setCenter(containerForCenterNodes);

        this.tapeView = new TapeView();

        this.containerForBotoomNodes = new VBox();
        containerForBotoomNodes.setPadding(new Insets(10, 50, 50, 50));
        containerForBotoomNodes.setSpacing(10);
        containerForBotoomNodes.setStyle(cssLayout);
        containerForBotoomNodes.getChildren().addAll(new Text("Tape"), tapeView);

        this.setBottom(containerForBotoomNodes);
    }

    private void setUpUILayout() {
    }

    private void setUpUIListeners() {
        MainStageListener mainStageListener = new MainStageListener(mainStageController);
        inputTextField.setOnKeyPressed(mainStageListener);
        toggleDiagramButton.setOnAction(mainStageListener);
        toggleTransitionTableButton.setOnAction(mainStageListener);
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

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TapeView getTapeView() {
        return tapeView;
    }
}
