package app.views;

import app.controllers.TransitionTableController;
import app.listeners.TransitionTableListener;
import app.models.TransitionModel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        //Set how many rows the user can select
        transitionTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


//Create input widgets for the user to enter a configuration
        this.currentStateComboBox = new ComboBox<>();
        currentStateComboBox.setEditable(true);
        currentStateComboBox.setPrefWidth(55);

        currentStateComboBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) {
                    return;
                }
                if (!newValue.matches("^(?!\\s*$).+") || newValue.length() > 1) {
                    currentStateComboBox.getEditor().setText(
                            newValue.substring(0, 0));
                }
            }
        });


        this.inputSymbolComboBox = new ComboBox<>();
        inputSymbolComboBox.setEditable(true);
        inputSymbolComboBox.setPrefWidth(55);
        inputSymbolComboBox.getItems().add("\u03B5");

        this.stackSymbolToPopComboBox = new ComboBox<>();
        stackSymbolToPopComboBox.setEditable(true);
        stackSymbolToPopComboBox.setPrefWidth(55);
        stackSymbolToPopComboBox.getItems().add("\u03B5");

// Create a arrow label to connect the configuration input widgets to action input widgets
        final Label arrowLabel = new Label("->");

//Create input widgets for the user to enter a configuration
        this.resultingStateComboBox = new ComboBox<>();
        resultingStateComboBox.setEditable(true);
        resultingStateComboBox.setPrefWidth(55);

        this.stackSymbolToPushComboBox = new ComboBox<>();
        stackSymbolToPushComboBox.setEditable(true);
        stackSymbolToPushComboBox.setPrefWidth(55);
        stackSymbolToPushComboBox.getItems().add("\u03B5");

        this.submitTransitionButton = new Button("Submit");
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(currentStateComboBox.getEditor().textProperty(),
                        inputSymbolComboBox.getEditor().textProperty(),
                        stackSymbolToPopComboBox.getEditor().textProperty(),
                        resultingStateComboBox.getEditor().textProperty(),
                        stackSymbolToPushComboBox.getEditor().textProperty());
            }

            @Override
            protected boolean computeValue() {
                return (currentStateComboBox.getValue() == null ||
                        inputSymbolComboBox.getValue() == null ||
                        stackSymbolToPopComboBox.getValue() == null ||
                        resultingStateComboBox.getValue() == null ||
                        stackSymbolToPushComboBox.getValue() == null
                );
            }
        };
        submitTransitionButton.disableProperty().bind(bb);

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

