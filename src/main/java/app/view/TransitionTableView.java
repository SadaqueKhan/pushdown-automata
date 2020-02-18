package app.view;

import app.controller.TransitionTableController;
import app.listener.TransitionTableListener;
import app.model.TransitionModel;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private ComboBox<String> currentStateComboBox;
    private ComboBox<String> inputSymbolComboBox;
    private ComboBox<String> stackSymbolToPopComboBox;

    //Action Textfields GUI
    private ComboBox<String> resultingStateComboBox;
    private ComboBox<String> stackSymbolToPushComboBox;

    //Submit transition button GUI
    private Button submitTransitionButton;
    private Button deleteTransitionButton;
    private VBox transitionTableContainer;

    public TransitionTableView(MainStageView mainStageView, TransitionTableController transitionTableController) {

        this.transitionTableController = transitionTableController;
        this.mainStageView = mainStageView;

        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
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
        //Set how many rows the user can select
        transitionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


//Create input widgets for the user to enter a configuration
        this.currentStateComboBox = new ComboBox<>();
        currentStateComboBox.setEditable(true);
        currentStateComboBox.setPrefWidth(110);

        this.inputSymbolComboBox = new ComboBox<>();
        inputSymbolComboBox.setEditable(true);
        inputSymbolComboBox.setPrefWidth(55);
        inputSymbolComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(inputSymbolComboBox);

        this.stackSymbolToPopComboBox = new ComboBox<>();
        stackSymbolToPopComboBox.setEditable(true);
        stackSymbolToPopComboBox.setPrefWidth(55);
        stackSymbolToPopComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(stackSymbolToPopComboBox);

// Create a arrow label to connect the configuration input widgets to action input widgets
        final Label arrowLabel = new Label("->");

//Create input widgets for the user to enter a configuration
        this.resultingStateComboBox = new ComboBox<>();
        resultingStateComboBox.setEditable(true);
        resultingStateComboBox.setPrefWidth(110);

        this.stackSymbolToPushComboBox = new ComboBox<>();
        stackSymbolToPushComboBox.setEditable(true);
        stackSymbolToPushComboBox.setPrefWidth(55);
        stackSymbolToPushComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(stackSymbolToPushComboBox);

        this.submitTransitionButton = new Button("Submit");


        //Create submit button for the user to submit a transition
        this.deleteTransitionButton = new Button("Delete");

        final HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.TOP_CENTER);
        hBox.getChildren().addAll(currentStateComboBox, inputSymbolComboBox, stackSymbolToPopComboBox, arrowLabel, resultingStateComboBox, stackSymbolToPushComboBox, submitTransitionButton, deleteTransitionButton);

        transitionTableContainer = new VBox();
        transitionTableContainer.setPadding(new Insets(10, 10, 10, 10));
        transitionTableContainer.setSpacing(10);
        transitionTableContainer.getChildren().addAll(transitionTable, hBox);
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

    private void setUpComboBoxesListeners(ComboBox comboBox) {
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (!newValue.matches("^\\w{1}$")) {
                // Delay the modification of the combobox as you can't edit the combobox whilst listening to events
                Platform.runLater(() -> {
                    comboBox.getEditor().clear();
                });
            }
        });
    }

    public TableView<TransitionModel> getTransitionTable() {
        return transitionTable;
    }

    public VBox getTransitionTableContainer() {
        return transitionTableContainer;
    }

    public ComboBox<String> getCurrentStateComboBox() {
        return currentStateComboBox;
    }

    public ComboBox<String> getInputSymbolComboBox() {
        return inputSymbolComboBox;
    }

    public ComboBox<String> getStackSymbolToPopComboBox() {
        return stackSymbolToPopComboBox;
    }

    public ComboBox<String> getResultingStateComboBox() {
        return resultingStateComboBox;
    }

    public ComboBox<String> getStackSymbolToPushComboBox() {
        return stackSymbolToPushComboBox;
    }
}

