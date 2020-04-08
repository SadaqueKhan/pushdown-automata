package app.view;
import app.listener.MainStageListener;
import app.presenter.MainStagePresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;
import java.util.Set;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for the main scene.
 * </p>
 */
public class MainScene extends BorderPane {
    //Reference to views presenter.
    private final MainStagePresenter mainStagePresenter;
    //Reference to UI components for view.
    private TextField inputTextField;
    private AutoCompletionBinding<String> autoCompletionBinding;
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTransitionTableButton;
    private VBox containerForCenterNodes;
    private Set<String> inputWordSet;
    private TapeScene tapeScene;
    private MenuItem saveMenuItem;
    private MenuItem loadMenuItem;
    private CheckMenuItem acceptanceByFinalStateMenuItem;
    private CheckMenuItem acceptanceByEmptyStackMenuItem;
    private Label inputTextLabel;
    private StackScene stackScene;
    private MenuItem helpGuideItem;
    private CheckMenuItem simulationByQuickRunMenuItem;
    private CheckMenuItem simulationByStepRunMenuItem;
    private MenuBar menuBar;
    private MenuItem helpCodeItem;
    private MenuItem newMachineMenuItem;
    /**
     * Constructor of the main stage, used to instantiate an instance of this view.
     * @param mainStagePresenter a reference to the views presenter.
     */
    public MainScene(MainStagePresenter mainStagePresenter) {
        this.mainStagePresenter = mainStagePresenter;
        setUpUIComponents();
        setUpUIListeners();
    }
    /**
     * Sets up the UI components of the view.
     */
    private void setUpUIComponents() {
        //Setup top GUI elements
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        fileMenu.setId("fileMenu");
        this.newMachineMenuItem = new MenuItem("New");
        this.saveMenuItem = new MenuItem("Save");
        saveMenuItem.setId("saveMenuItem");
        this.loadMenuItem = new MenuItem("Load");
        loadMenuItem.setId("loadMenuItem");
        fileMenu.getItems().addAll(newMachineMenuItem, saveMenuItem, loadMenuItem);
        menuBar.getMenus().add(fileMenu);
        Menu acceptanceMenu = new Menu("Acceptance");
        acceptanceMenu.setId("acceptanceMenu");
        this.acceptanceByFinalStateMenuItem = new CheckMenuItem("By Final State");
        acceptanceByFinalStateMenuItem.setId("acceptanceByFinalStateMenuItem");
        this.acceptanceByEmptyStackMenuItem = new CheckMenuItem("By Empty Stack");
        acceptanceByEmptyStackMenuItem.setId("acceptanceByEmptyStackMenuItem");
        acceptanceMenu.getItems().addAll(acceptanceByFinalStateMenuItem, acceptanceByEmptyStackMenuItem);
        acceptanceByFinalStateMenuItem.setSelected(true);
        menuBar.getMenus().add(acceptanceMenu);
        Menu simulationMenu = new Menu("Simulation");
        simulationMenu.setId("simulationMenu");
        this.simulationByQuickRunMenuItem = new CheckMenuItem("By Quick Run");
        simulationByQuickRunMenuItem.setId("simulationByQuickRunMenuItem");
        this.simulationByStepRunMenuItem = new CheckMenuItem("By Step Run");
        simulationByStepRunMenuItem.setId("simulationByStepRunMenuItem");
        simulationMenu.getItems().addAll(simulationByQuickRunMenuItem, simulationByStepRunMenuItem);
        simulationByQuickRunMenuItem.setSelected(true);
        menuBar.getMenus().add(simulationMenu);
        Menu helpMenu = new Menu("Help");
        this.helpCodeItem = new MenuItem("Code");
        this.helpGuideItem = new MenuItem("Guide");
        helpMenu.getItems().addAll(helpCodeItem, helpGuideItem);
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
        String cssLayout = "-fx-border-color: black;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: solid;\n";
        containerForCenterNodes.setStyle(cssLayout);
        containerForCenterNodes.getChildren().add(segmentedButton);
        this.setCenter(containerForCenterNodes);
        this.stackScene = new StackScene();
        VBox containerForRightNodes = new VBox();
        containerForRightNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForRightNodes.setSpacing(5);
        containerForRightNodes.setStyle(cssLayout);
        containerForRightNodes.getChildren().addAll(new Text("Stack"), stackScene);
        this.setRight(containerForRightNodes);
        //Setup bottom GUI elements
        this.tapeScene = new TapeScene();
        VBox containerForBotoomNodes = new VBox();
        containerForBotoomNodes.setPadding(new Insets(10, 10, 10, 10));
        containerForBotoomNodes.setSpacing(5);
        containerForBotoomNodes.setStyle(cssLayout);
        containerForBotoomNodes.getChildren().addAll(new Text("Tape"), tapeScene);
        this.setBottom(containerForBotoomNodes);
    }
    /**
     * Sets up the listeners for UI components of the view.
     */
    private void setUpUIListeners() {
        //Create listener for UI component for this view.
        MainStageListener mainStageListener = new MainStageListener(mainStagePresenter);
        //Link listener to events on UI components for this view.
        inputTextField.setOnKeyPressed(mainStageListener);
        toggleDiagramButton.setOnAction(mainStageListener);
        toggleTransitionTableButton.setOnAction(mainStageListener);
        newMachineMenuItem.setOnAction(mainStageListener);
        saveMenuItem.setOnAction(mainStageListener);
        loadMenuItem.setOnAction(mainStageListener);
        acceptanceByFinalStateMenuItem.setOnAction(mainStageListener);
        acceptanceByEmptyStackMenuItem.setOnAction(mainStageListener);
        simulationByQuickRunMenuItem.setOnAction(mainStageListener);
        simulationByStepRunMenuItem.setOnAction(mainStageListener);
        helpCodeItem.setOnAction(mainStageListener);
        helpGuideItem.setOnAction(mainStageListener);
    }
    // Getters/Setters for UI components of the view.
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
    public TapeScene getTapeScene() {
        return tapeScene;
    }
    public StackScene getStackScene() {
        return stackScene;
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
    public MenuBar getMenuBar() {
        return menuBar;
    }
    public ToggleButton getToggleDiagramButton() {
        return toggleDiagramButton;
    }
    public ToggleButton getToggleTransitionTableButton() {
        return toggleTransitionTableButton;
    }
}
