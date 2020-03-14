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
    private VBox containerForCenterNodes;
    private Set<String> inputWordSet;
    private TapeView tapeView;
    private VBox containerForBotoomNodes;
    private MenuItem saveMenuItem;
    private MenuItem loadMenuItem;
    private CheckMenuItem acceptanceByFinalStateMenuItem;
    private CheckMenuItem acceptanceByEmptyStackMenuItem;
    private Label inputTextLabel;
    private StackView stackView;
    private VBox containerForRightNodes;
    private MenuItem helpGuidelItem;
    private CheckMenuItem simulationByQuickRunMenuItem;
    private CheckMenuItem simulationByStepRunMenuItem;

    public MainStageView(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        //Setup top GUI elements
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        this.saveMenuItem = new MenuItem("Save");
        this.loadMenuItem = new MenuItem("Load");
        fileMenu.getItems().addAll(saveMenuItem, loadMenuItem);

        menuBar.getMenus().add(fileMenu);

        Menu acceptanceMenu = new Menu("Acceptance");
        this.acceptanceByFinalStateMenuItem = new CheckMenuItem("By Final State");
        this.acceptanceByEmptyStackMenuItem = new CheckMenuItem("By Empty Stack");
        acceptanceMenu.getItems().addAll(acceptanceByFinalStateMenuItem, acceptanceByEmptyStackMenuItem);
        acceptanceByFinalStateMenuItem.setSelected(true);
        menuBar.getMenus().add(acceptanceMenu);

        Menu simulationMenu = new Menu("Simulation");
        this.simulationByQuickRunMenuItem = new CheckMenuItem("By Quick Run");
        this.simulationByStepRunMenuItem = new CheckMenuItem("By Step Run");
        simulationMenu.getItems().addAll(simulationByQuickRunMenuItem, simulationByStepRunMenuItem);
        simulationByQuickRunMenuItem.setSelected(true);
        menuBar.getMenus().add(simulationMenu);

        Menu helpMenu = new Menu("Help");
        this.helpGuidelItem = new MenuItem("Guide");
        helpMenu.getItems().addAll(helpGuidelItem);

        menuBar.getMenus().add(helpMenu);

        this.inputTextLabel = new Label("Input Word (acceptance by final state)");
        this.inputTextField = new TextField();
        inputTextField.setPromptText("Enter input word");
        inputTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().equals(" ")) {
                change.setText("");
            }
            if (change.getText().equals("\u03B5")) {
                change.setText("");
            }
            return change;
        }));

        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.setFillWidth(true);
        containerForTopNodes.getChildren().addAll(menuBar, inputTextLabel, inputTextField);


        this.setTop(containerForTopNodes);

        //Setup center GUI elements
        this.toggleDiagramButton = new ToggleButton("Diagram");
        this.toggleTransitionTableButton = new ToggleButton("Table");
        SegmentedButton segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTransitionTableButton);
        toggleDiagramButton.setSelected(true);

        this.containerForCenterNodes = new VBox();
        containerForCenterNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForCenterNodes.setSpacing(5);
        containerForCenterNodes.setAlignment(Pos.TOP_CENTER);
        containerForCenterNodes.setStyle(cssLayout);
        containerForCenterNodes.getChildren().add(segmentedButton);

        this.setCenter(containerForCenterNodes);

        this.stackView = new StackView();

        this.containerForRightNodes = new VBox();
        containerForRightNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForRightNodes.setSpacing(5);
        containerForRightNodes.setStyle(cssLayout);
        containerForRightNodes.getChildren().addAll(new Text("Stack"), stackView);

        this.setRight(containerForRightNodes);

        //Setup bottom GUI elements
        this.tapeView = new TapeView();

        this.containerForBotoomNodes = new VBox();
        containerForBotoomNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForBotoomNodes.setSpacing(5);
        containerForBotoomNodes.setStyle(cssLayout);
        containerForBotoomNodes.getChildren().addAll(new Text("Tape"), tapeView);

        this.setBottom(containerForBotoomNodes);

    }
    private void setUpUIListeners() {
        MainStageListener mainStageListener = new MainStageListener(mainStageController);
        inputTextField.setOnKeyPressed(mainStageListener);
        toggleDiagramButton.setOnAction(mainStageListener);
        toggleTransitionTableButton.setOnAction(mainStageListener);
        saveMenuItem.setOnAction(mainStageListener);
        loadMenuItem.setOnAction(mainStageListener);
        acceptanceByFinalStateMenuItem.setOnAction(mainStageListener);
        acceptanceByEmptyStackMenuItem.setOnAction(mainStageListener);
        simulationByQuickRunMenuItem.setOnAction(mainStageListener);
        simulationByStepRunMenuItem.setOnAction(mainStageListener);
        helpGuidelItem.setOnAction(mainStageListener);
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


    public TapeView getTapeView() {
        return tapeView;
    }

    public StackView getStackView() {
        return stackView;
    }

    public CheckMenuItem getAcceptanceByFinalStateMenuItem() {
        return acceptanceByFinalStateMenuItem;
    }

    public CheckMenuItem getAcceptanceByEmptyStackMenuItem() {
        return acceptanceByEmptyStackMenuItem;
    }

    public Label getInputTextLabel() {
        return inputTextLabel;
    }

    public CheckMenuItem getSimulationByQuickRunMenuItem() {
        return simulationByQuickRunMenuItem;
    }

    public CheckMenuItem getSimulationByStepRunMenuItem() {
        return simulationByStepRunMenuItem;
    }
}
