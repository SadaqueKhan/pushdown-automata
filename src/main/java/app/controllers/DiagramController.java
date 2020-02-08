package app.controllers;

import app.models.MachineModel;
import app.models.StateModel;
import app.views.DiagramView;
import app.views.MainStageView;
import app.views.StateView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DiagramController {

    private final MainStageView mainStageView;
    private final MainStageController mainStageController;
    private final MachineModel machineModel;

    private final TransitionTableController transitionTableController;

    private final DiagramView diagramView;

    // Defines the x and y coordinate of the translation that is added to this {@code Node}'transform for the purpose of layout.
    private double layoutX;
    private double layoutY;

    private double sceneX;
    private double sceneY;

    private double dragContextX = 0.0;
    private double dragContextY = 0.0;


    public DiagramController(MainStageView mainStageView, MainStageController mainStageController, MachineModel machineModel) {

        this.mainStageController = mainStageController;
        this.mainStageView = mainStageView;
        this.machineModel = machineModel;

        this.transitionTableController = mainStageController.getTransitionTableController();
        this.diagramView = new DiagramView(this, mainStageView);
    }

    public void loadDiagramView() {
        diagramView.loadToMainStage();
    }

    //DiagramPaneGUIEventResponses
    public void addStateToViewMouseEventResponse(double x, double y) {
        StateModel newStateModel = new StateModel();
        machineModel.addStateModel(newStateModel);
        diagramView.addStateView(x, y, this, newStateModel.getStateId());
    }


    //StateGUIEventResponses

    public void stateViewOnMousePressed(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {
        double scale = diagramView.getScale();

        dragContextX = stateView.getBoundsInParent().getMinX() * scale - xPositionOfMouse;
        dragContextY = stateView.getBoundsInParent().getMinY() * scale - yPositionOfMouse;

    }


    public void stateViewOnMouseDragged(StateView stateView, double xPositionOfMouse, double yPositionOfMouse) {

        double scale = diagramView.getScale();
        double offsetX = xPositionOfMouse + dragContextX;
        double offsetY = yPositionOfMouse + dragContextY;

        offsetX /= scale;
        offsetY /= scale;

        if (!(stateView.getStateParents().isEmpty())) {
            //Target

            for (StateView stateView1 : stateView.getStateParents()) {

                if (stateView1.getCurrentStateXPosition() < offsetX) {
//                    System.out.println("----");
//                    System.out.println("TargetXONLEFT:" + offsetX);
//                    System.out.println("SourceYONRIGHT:" + stateView1.getCurrentStateXPosition());
//                    System.out.println("----");
                }

            }
        }

        stateView.setCurrentStateXPosition(offsetX);
        stateView.setCurrentStateYPosition(offsetY);

        stateView.relocate(offsetX, offsetY);


    }

    public void stateViewOnMouseReleased(StateView stateView) {
        // Updating the new layout positions
        stateView.setLayoutX(layoutX + stateView.getTranslateX());
        stateView.setLayoutY(layoutY + stateView.getTranslateY());

        // Resetting the translate positions
        stateView.setTranslateX(0);
        stateView.setTranslateY(0);
    }


    public void stateViewContextMenuPopUp(StateView stateView) {

        StateModel stateModel = machineModel.getStateModel(stateView.getStateId());

        //TODO need to MVC this
        ContextMenu contextMenu = new ContextMenu();
        MenuItem toggleStandardStateItem = new MenuItem("Toggle standard state");
        MenuItem toggleStartStateItem = new MenuItem("Toggle start state");
        MenuItem toggleFinalStateItem = new MenuItem("Toggle final state");
        MenuItem createTransitionItem = new MenuItem("Create transition");
        MenuItem deleteStateItem = new MenuItem("Delete");

        toggleStandardStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (stateModel.isStandardState()) {
                    stateModel.setStandardState(false);
                    stateView.toggleStandardStateUIComponent(stateModel.isStandardState());
                } else {
                    stateModel.setStandardState(true);
                    stateView.toggleStandardStateUIComponent(stateModel.isStandardState());

                }
            }

        });

        toggleStartStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (stateModel.isStartState()) {
                    stateModel.setStartState(false);
                    stateView.toggleStartStateUIComponent(stateModel.isStartState());
                } else {
                    stateModel.setStartState(true);
                    stateView.toggleStartStateUIComponent(stateModel.isStartState());

                }
            }
        });

        //TODO need to remove this listeners logic
        toggleFinalStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (stateModel.isFinalState()) {
                    stateModel.setFinalState(false);
                    stateView.toggleFinalStateUIComponent(stateModel.isFinalState());
                } else {
                    stateModel.setFinalState(true);
                    stateView.toggleFinalStateUIComponent(stateModel.isFinalState());
                }
            }
        });


        //TODO need to remove this listeners logic
        createTransitionItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {


//Create input widgets for the user to enter a configuration
                TextField currentStateTextField = new TextField();
                currentStateTextField.setPrefWidth(50);

                TextField inputSymbolTextField = new TextField();
                inputSymbolTextField.setPrefWidth(50);

                TextField stackSymbolToPopTextField = new TextField();
                stackSymbolToPopTextField.setPrefWidth(50);

// Create a arrow label to connect the configuration input widgets to action input widgets
                final Label arrowLabel = new Label("->");

//Create input widgets for the user to enter a configuration
                TextField resultingStateTextField = new TextField();
                resultingStateTextField.setPrefWidth(50);

                TextField stackSymbolToPushTextField = new TextField();
                stackSymbolToPushTextField.setPrefWidth(50);

//Create submit button for the user to submit a transition
                Button submitTransitionButton = new Button("Submit");


                final HBox hBox = new HBox();
                hBox.setPadding(new Insets(10, 10, 10, 10));
                hBox.setSpacing(10);
                hBox.getChildren().addAll(currentStateTextField, inputSymbolTextField, stackSymbolToPopTextField, arrowLabel, resultingStateTextField, stackSymbolToPushTextField);

                final VBox vBox = new VBox(hBox, submitTransitionButton);
                vBox.setAlignment(Pos.CENTER);
                
                Scene scene = new Scene(vBox, 340, 100);
                Stage stage = new Stage();
                stage.setResizable(false);
                stage.setTitle("Create Transition");
                stage.setScene(scene);
                stage.show();
            }
        });

        deleteStateItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                machineModel.removeStateModel(stateView.getStateId());
                diagramView.getChildren().remove(stateView);
            }
        });

        contextMenu.getItems().add(toggleStandardStateItem);
        contextMenu.getItems().add(toggleStartStateItem);
        contextMenu.getItems().add(toggleFinalStateItem);
        contextMenu.getItems().add(createTransitionItem);
        contextMenu.getItems().add(deleteStateItem);

        contextMenu.show(stateView, Side.RIGHT, 5, 5);

    }


    //TransitionTableGUIEventResponses
    public void addStateToViewTransitionTableInputEventResponse(double x, double y, String userEntryCurrentStateId) {
        diagramView.addStateView(x, y, this, userEntryCurrentStateId);
    }


    public void addTransitionToViewTransitionTableEventResponse(String sourceStateID, String targetStateID, String transitionsID) {
        diagramView.addTransitionView(sourceStateID, targetStateID, transitionsID);
    }


}
