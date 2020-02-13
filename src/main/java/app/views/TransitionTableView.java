package app.views;

import app.controllers.TransitionTableController;
import app.listeners.TransitionTableListener;
import app.models.TransitionModel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.HashSet;

public class TransitionTableView extends BorderPane {

    private final MainStageView mainStageView;

    private final TransitionTableController transitionTableController;

    // Transition Table GUI
    private TableView<TransitionModel> transitionTable;

    //Configuration Column GUI
    private TableColumn configurationCol;
    private TableColumn currentStateCol;
    private TableColumn inputSymbolCol;
    private TableColumn stackSymbolToPopCol;

    //Action Column GUI
    private TableColumn actionCol;
    private TableColumn resultingStateCol;
    private TableColumn stackSymbolToPushCol;


    //Configuration Textfields GUI
    private TextField currentStateTextField;
    private TextField inputSymbolTextField;
    private TextField stackSymbolToPopTextField;

    //Action Textfields GUI
    private TextField resultingStateTextField;
    private TextField stackSymbolToPushTextField;

    //Submit transition button GUI
    private Button submitTransitionButton;
    private Button deleteTransitionButton;
    private VBox transitionTableContainer;

    private ComboBox<String> currentStateComboBox;
    private HashSet<String> currentAvailableStatesSet;
    private AutoCompletionBinding<String> autoCompletionBinding;


    public TransitionTableView(MainStageView mainStageView, TransitionTableController transitionTableController) {

        this.transitionTableController = transitionTableController;
        this.mainStageView = mainStageView;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    public void loadToMainStage() {
        mainStageView.getContainerForCenterNodes().getChildren().add(transitionTableContainer);
    }


    private void setUpUIComponents() {

        //Create table
        transitionTable = new TableView<TransitionModel>();

        //Set the table to be editable
        transitionTable.setEditable(true);

        //Set the default message output for the table when it is empty
        transitionTable.setPlaceholder(new Label("No transition defined"));

        //Disable default constrained resize policy
        transitionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


//<---Create configuration column --->
        configurationCol = new TableColumn("Configuration");

        this.currentStateCol = new TableColumn("Current state");
        currentStateCol.setMinWidth(250);
        currentStateCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("currentStateModel"));

        this.inputSymbolCol = new TableColumn("Input symbol");
        inputSymbolCol.setMinWidth(250);
        inputSymbolCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("inputSymbol"));

        this.stackSymbolToPopCol = new TableColumn("Stack symbol to pop");
        stackSymbolToPopCol.setMinWidth(250);
        stackSymbolToPopCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("stackSymbolToPop"));

        configurationCol.getColumns().addAll(currentStateCol, inputSymbolCol, stackSymbolToPopCol);


//<---Create action column --->
        actionCol = new TableColumn("Action");

        this.resultingStateCol = new TableColumn("Resulting state");
        resultingStateCol.setMinWidth(250);
        resultingStateCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("resultingStateModel"));

        this.stackSymbolToPushCol = new TableColumn("Stack symbol to push");
        stackSymbolToPushCol.setMinWidth(250);

        stackSymbolToPushCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("stackSymbolToPush"));

        actionCol.getColumns().addAll(resultingStateCol, stackSymbolToPushCol);

        transitionTable.getColumns().addAll(configurationCol, actionCol);

        // Designate sorting procedure
        // TODO: Refine this current implementation is decent as it takes the letter and sorts it alphabetically
        transitionTable.getSortOrder().add(currentStateCol);

        transitionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

//Create input widgets for the user to enter a configuration
        this.currentStateTextField = new TextField();
        currentStateTextField.setPrefWidth(50);


        currentStateComboBox = new ComboBox<>();
        currentStateComboBox.setEditable(true);
        currentAvailableStatesSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(currentStateComboBox.getEditor(), currentAvailableStatesSet);
        TextFields.bindAutoCompletion(currentStateComboBox.getEditor(), currentStateComboBox.getItems());

        this.inputSymbolTextField = new TextField();
        inputSymbolTextField.setPrefWidth(50);

        this.stackSymbolToPopTextField = new TextField();
        stackSymbolToPopTextField.setPrefWidth(50);

// Create a arrow label to connect the configuration input widgets to action input widgets
        final Label arrowLabel = new Label("->");

//Create input widgets for the user to enter a configuration
        this.resultingStateTextField = new TextField();
        resultingStateTextField.setPrefWidth(50);

        this.stackSymbolToPushTextField = new TextField();
        stackSymbolToPushTextField.setPrefWidth(50);

//Create submit button for the user to submit a transition
        this.submitTransitionButton = new Button("Submit");

        //Create submit button for the user to submit a transition
        this.deleteTransitionButton = new Button("Delete");

        final HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(currentStateComboBox, inputSymbolTextField, stackSymbolToPopTextField, arrowLabel, resultingStateTextField, stackSymbolToPushTextField, submitTransitionButton, deleteTransitionButton);

        transitionTableContainer = new VBox();
        transitionTableContainer.setPadding(new Insets(10, 10, 10, 10));
        transitionTableContainer.setSpacing(10);
        transitionTableContainer.getChildren().addAll(transitionTable, hBox);

        loadToMainStage();
    }


    private void setUpUILayout() {
    }

    private void setUpUIListeners() {
        //Create listener for this view
        TransitionTableListener transitionTableListener = new TransitionTableListener(transitionTableController);

        //Set a listener that is triggered when the submit button is clicked
        submitTransitionButton.setOnAction(transitionTableListener);
        deleteTransitionButton.setOnAction(transitionTableListener);
    }

    public TableView<TransitionModel> getTransitionTable() {
        return transitionTable;
    }

    public TextField getCurrentStateTextField() {
        return currentStateTextField;
    }

    public TextField getInputSymbolTextField() {
        return inputSymbolTextField;
    }

    public TextField getStackSymbolToPopTextField() {
        return stackSymbolToPopTextField;
    }

    public TextField getResultingStateTextField() {
        return resultingStateTextField;
    }

    public TextField getStackSymbolToPushTextField() {
        return stackSymbolToPushTextField;
    }

    public ComboBox<String> getCurrentStateComboBox() {
        return currentStateComboBox;
    }

    public AutoCompletionBinding<String> getAutoCompletionBinding() {
        return autoCompletionBinding;
    }

    public void setAutoCompletionBinding(AutoCompletionBinding<String> autoCompletionBinding) {
        this.autoCompletionBinding = autoCompletionBinding;
    }

    public HashSet<String> getCurrentAvailableStatesSet() {
        return currentAvailableStatesSet;
    }

}
