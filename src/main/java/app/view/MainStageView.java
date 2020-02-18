package app.view;

import app.controller.MainStageController;
import app.listener.MainStageListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

    private TextField inputTextField;
    AutoCompletionBinding<String> autoCompletionBinding;
    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: solid;\n";
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTransitionTableButton;
    private SegmentedButton segmentedButton;
    private VBox containerForCenterNodes;
    private Set<String> inputWordSet;
    private ProgressBar progressBar;
    private TapeView tapeView;
    private VBox containerForBotoomNodes;
    private MenuItem saveMenuItem;
    private MenuItem loadMenuItem;


    public MainStageView(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        //Setup top GUI elements
        Menu fileMenu = new Menu("File");
        this.saveMenuItem = new MenuItem("Save");
        this.loadMenuItem = new MenuItem("Load");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem help = new MenuItem("Help");
        fileMenu.getItems().addAll(saveMenuItem, loadMenuItem, separator, help);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        inputTextField = new TextField();
        inputTextField.setPromptText("Enter input word");
        inputTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            return change;
        }));
        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setVisible(false);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.setFillWidth(true);
        containerForTopNodes.getChildren().addAll(menuBar, new Text("Input Word"), inputTextField, progressBar);


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

        //Setup bottom GUI elements
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
        saveMenuItem.setOnAction(mainStageListener);
        loadMenuItem.setOnAction(mainStageListener);

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
