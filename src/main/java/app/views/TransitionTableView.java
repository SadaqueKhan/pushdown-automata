package app.views;

import app.models.TransitionModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TransitionTableView extends BorderPane {


    private TableView<TransitionModel> transitionTable;

    private TableColumn configurationCol;
    private TableColumn stateSymbolCol;
    private TableColumn inputSymbolCol;
    private TableColumn topStackSymbolToPopCol;

    private TableColumn actionCol;
    private TableColumn stateAfterEventCol;
    private TableColumn topStackSymbolToPushCol;


    public TransitionTableView() {

        setUpComponents();
        setUpLayout();
        setUpListeners();
    }


    private void setUpComponents() {

        //Create table
        transitionTable = new TableView<TransitionModel>();

        //Set the table to be editable
        transitionTable.setEditable(true);

        //Set the default message output for the table when it is empty
        transitionTable.setPlaceholder(new Label("No transition defined"));

        //Distribute extra space in table column header among the columns.
        transitionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


//<---Create configuration column --->
        configurationCol = new TableColumn("Configuration");


        //<---Make first column --->
        //Create first column of the table
        this.stateSymbolCol = new TableColumn("State");
        stateSymbolCol.setMinWidth(250);

        //Setting data properities to columns
        stateSymbolCol.setCellValueFactory(
                new PropertyValueFactory<TransitionModel, String>("state"));


        //<---Make second column --->
        //Create second column of the table
        this.inputSymbolCol = new TableColumn("Input symbol");
        inputSymbolCol.setMinWidth(250);

        //Setting data properities to columns
        inputSymbolCol.setCellValueFactory(
                new PropertyValueFactory<TransitionModel, String>("inputSymbol"));


        //<---Make third column --->
        //Create third column of the table
        this.topStackSymbolToPopCol = new TableColumn("Top stack symbol to pop");
        topStackSymbolToPopCol.setMinWidth(250);

        //Setting data properities to columns
        topStackSymbolToPopCol.setCellValueFactory(
                new PropertyValueFactory<TransitionModel, String>("topStackSymbolToPop"));


        configurationCol.getColumns().addAll(stateSymbolCol, inputSymbolCol, topStackSymbolToPopCol);


//<---Create action column --->
        actionCol = new TableColumn("Action");


        //<---Make first column --->
        //Create first column of the table
        this.stateAfterEventCol = new TableColumn("State");
        stateAfterEventCol.setMinWidth(250);

        //Setting data properities to columns
        stateAfterEventCol.setCellValueFactory(
                new PropertyValueFactory<TransitionModel, String>("state"));


        //<---Make second column --->
        //Create second column of the table
        this.topStackSymbolToPushCol = new TableColumn("Input symbol");
        topStackSymbolToPushCol.setMinWidth(250);

        //Setting data properities to columns
        topStackSymbolToPushCol.setCellValueFactory(
                new PropertyValueFactory<TransitionModel, String>("inputSymbol"));


        actionCol.getColumns().addAll(stateAfterEventCol, topStackSymbolToPushCol);


//Add configuration column and action column to the transition table
        transitionTable.getColumns().addAll(configurationCol, actionCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));

        vbox.getChildren().add(transitionTable);


        this.setCenter(vbox);
    }


    private void setUpLayout() {
    }

    private void setUpListeners() {
    }


}
