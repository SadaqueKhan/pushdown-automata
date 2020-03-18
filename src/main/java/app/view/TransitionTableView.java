package app.view;

import app.listener.TransitionTableListener;
import app.model.TransitionModel;
import app.presenter.TransitionTableController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
        setUpUIListeners();
    }


    private void setUpUIComponents() {

        //Create table
        transitionTable = new TableView<>();

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

        this.stackSymbolToPopCol = new TableColumn("StackModel symbol to pop");
        stackSymbolToPopCol.setMinWidth(250);
        stackSymbolToPopCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("stackSymbolToPop"));

        configurationCol.getColumns().addAll(currentStateCol, inputSymbolCol, stackSymbolToPopCol);


//<---Create action column --->
        actionCol = new TableColumn("Action");

        this.resultingStateCol = new TableColumn("Resulting state");
        resultingStateCol.setMinWidth(250);
        resultingStateCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("resultingStateModel"));

        this.stackSymbolToPushCol = new TableColumn("StackModel symbol to push");
        stackSymbolToPushCol.setMinWidth(250);

        stackSymbolToPushCol.setCellValueFactory(new PropertyValueFactory<TransitionModel, String>("stackSymbolToPush"));

        actionCol.getColumns().addAll(resultingStateCol, stackSymbolToPushCol);

        transitionTable.getColumns().addAll(configurationCol, actionCol);

        //Set how many rows the user can select
        transitionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


//Create input widgets for the user to enter a configuration
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setAlignment(Pos.TOP_CENTER);

        this.currentStateComboBox = new ComboBox<>();
        currentStateComboBox.setEditable(true);
        currentStateComboBox.setPrefWidth(110);

        gridPane.add(new Label("Current State"), 1, 1);
        gridPane.add(currentStateComboBox, 1, 2);

        this.inputSymbolComboBox = new ComboBox<>();
        inputSymbolComboBox.setEditable(true);
        inputSymbolComboBox.setPrefWidth(110);
        inputSymbolComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(inputSymbolComboBox);

        gridPane.add(new Label("Input Symbol"), 2, 1);
        gridPane.add(inputSymbolComboBox, 2, 2);

        this.stackSymbolToPopComboBox = new ComboBox<>();
        stackSymbolToPopComboBox.setEditable(true);
        stackSymbolToPopComboBox.setPrefWidth(110);
        stackSymbolToPopComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(stackSymbolToPopComboBox);

        gridPane.add(new Label("Stack Symbol to Pop"), 3, 1);
        gridPane.add(stackSymbolToPopComboBox, 3, 2);

// Create a arrow label to connect the configuration input widgets to action input widgets
        gridPane.add(new Label("->"), 4, 2);

//Create input widgets for the user to enter a configuration
        this.resultingStateComboBox = new ComboBox<>();
        resultingStateComboBox.setEditable(true);
        resultingStateComboBox.setPrefWidth(110);

        gridPane.add(new Label("Resulting State"), 5, 1);
        gridPane.add(resultingStateComboBox, 5, 2);

        this.stackSymbolToPushComboBox = new ComboBox<>();
        stackSymbolToPushComboBox.setEditable(true);
        stackSymbolToPushComboBox.setPrefWidth(110);
        stackSymbolToPushComboBox.getItems().add("\u03B5");
        setUpComboBoxesListeners(stackSymbolToPushComboBox);

        gridPane.add(new Label("Stack Symbol to Push"), 6, 1);
        gridPane.add(stackSymbolToPushComboBox, 6, 2);

        this.submitTransitionButton = new Button("Submit");

        //Create submit button for the user to submit a transition
        this.deleteTransitionButton = new Button("Delete");

        HBox hBoxButtons = new HBox();
        hBoxButtons.setPadding(new Insets(10, 10, 10, 10));
        hBoxButtons.setSpacing(10);
        hBoxButtons.getChildren().addAll(submitTransitionButton, deleteTransitionButton);
        gridPane.add(hBoxButtons, 7, 2);

        transitionTableContainer = new VBox();
        transitionTableContainer.setPadding(new Insets(10, 10, 10, 10));
        transitionTableContainer.setSpacing(10);
        transitionTableContainer.getChildren().addAll(transitionTable, gridPane);
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

            if ((newValue.matches("^\\w{1}$")) || newValue.equals("\u03B5")) {
                return;
            }
            Platform.runLater(() -> {
                comboBox.getEditor().clear();
            });
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

