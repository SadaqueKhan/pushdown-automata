package app.view;

import app.controller.DiagramController;
import app.listener.DiagramListener;
import app.model.TransitionModel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import org.controlsfx.control.PopOver;

import java.util.HashSet;
import java.util.Iterator;

public class StateView extends StackPane {

    //Reference to diagram controller
    private final DiagramController diagramController;

    //Data fields for view
    private String stateId;
    private double currentStateXPosition;
    private double currentStateYPosition;

    // ControlState GUI Components
    private Circle stateCircle;
    private Label stateIdText;
    private Line startStatePointLine1;
    private Line startStatePointLine2;
    private Arc finalStateArc;

    //Reflexive arc transition GUI components
    private Arc reflexiveArrowShaftArc;
    private Polygon reflexiveArrowTipPolygon;
    private VBox listOfTransitionsVBox;

    public StateView(double currentStateXPosition, double currentStateYPosition, DiagramController diagramController, String stateId) {
        this.currentStateXPosition = currentStateXPosition;
        this.currentStateYPosition = currentStateYPosition;

        this.stateId = stateId;

        // Reference to the controller of this view
        this.diagramController = diagramController;

        //Set up the components to represent the stateCircle in the view
        setUpUIComponents();
        setUpUIListeners();
    }


    public void setUpUIComponents() {
        double radius = 25;
        double paneSize = 2 * radius;

//<<< CREATE STANDARD STATE UI COMPONENT >>>
        //ControlState GUI
        this.stateCircle = new Circle();


        stateCircle.setRadius(radius);
        stateCircle.setStyle("-fx-fill:orange;-fx-stroke-width:2px;-fx-stroke:black;");

        stateIdText = new Label(stateId);
        stateIdText.setStyle("-fx-font-size:18px;-fx-font-weight:bold;");
        this.setPrefSize(paneSize, paneSize);
        this.setMaxSize(paneSize, paneSize);
        this.setMinSize(paneSize, paneSize);

//<<< CREATE START STATE UI COMPONENT >>>
        //Create arrow shaft using line object
        this.startStatePointLine1 = new Line();
        startStatePointLine1.setStrokeWidth(2);

        // instantiating the Rotate class.
        Rotate rotate1 = new Rotate();
        //setting properties for the rotate object.
        rotate1.setAngle(90);

        startStatePointLine1.setStartX(0);
        startStatePointLine1.setStartY(0);
        startStatePointLine1.setEndX(10);
        startStatePointLine1.setEndY(10);
        startStatePointLine1.getTransforms().add(rotate1);
        // startStatePointLine1.relocate(currentStateXPosition - 40, currentStateYPosition);

        startStatePointLine1.setTranslateX(-21);
        startStatePointLine1.setTranslateY(5);

        //Create arrow shaft using line object
        this.startStatePointLine2 = new Line();
        startStatePointLine2.setStrokeWidth(2);

        // instantiating the Rotate class.
        Rotate rotate2 = new Rotate();
        //setting properties for the rotate object.
        rotate2.setAngle(180);

        startStatePointLine2.setStartX(0);
        startStatePointLine2.setStartY(0);
        startStatePointLine2.setEndX(10);
        startStatePointLine2.setEndY(10);
        startStatePointLine2.getTransforms().add(rotate2);

        startStatePointLine2.setTranslateX(-21);
        startStatePointLine2.setTranslateY(5);
        //  startStatePointLine2.relocate(currentStateXPosition - 40, currentStateYPosition);

        startStatePointLine1.setVisible(false);
        startStatePointLine2.setVisible(false);


//<<< CREATE FINAL STATE UI COMPONENT >>>
        this.finalStateArc = new Arc(0, 0, radius / 1.25, radius / 1.25, 0, 360);

        finalStateArc.setType(ArcType.OPEN);
        finalStateArc.setStrokeWidth(2);
        finalStateArc.setStroke(Color.BLACK);
        finalStateArc.setStrokeType(StrokeType.INSIDE);
        finalStateArc.setFill(null);

        finalStateArc.setVisible(false);

//<<< CREATE FINAL STATE UI COMPONENT >>>

        // Reflexive arrow shaft
        reflexiveArrowShaftArc = new Arc();
        reflexiveArrowShaftArc.setCenterX(0);
        reflexiveArrowShaftArc.setCenterY(-50);
        reflexiveArrowShaftArc.setRadiusX(25);
        reflexiveArrowShaftArc.setRadiusY(25);
        reflexiveArrowShaftArc.setStartAngle(340);
        reflexiveArrowShaftArc.setLength(220);
        reflexiveArrowShaftArc.setType(ArcType.OPEN);
        reflexiveArrowShaftArc.setStrokeWidth(2);
        reflexiveArrowShaftArc.setStroke(Color.BLACK);
        reflexiveArrowShaftArc.setStrokeType(StrokeType.INSIDE);
        reflexiveArrowShaftArc.setFill(null);
        reflexiveArrowShaftArc.setTranslateX(0);
        reflexiveArrowShaftArc.setTranslateY(-28);

        // Reflexive arrow tip
        reflexiveArrowTipPolygon = new Polygon(4, 0, 8, 8, 0, 8);
        reflexiveArrowTipPolygon.setFill(Color.BLACK);
        reflexiveArrowTipPolygon.setStroke(Color.BLACK);
        reflexiveArrowTipPolygon.setStrokeWidth(2);
        reflexiveArrowTipPolygon.setRotate(65);
        reflexiveArrowTipPolygon.setTranslateX(25);
        reflexiveArrowTipPolygon.setTranslateY(-20);

        reflexiveArrowShaftArc.setVisible(false);
        reflexiveArrowTipPolygon.setVisible(false);

        this.relocate(currentStateXPosition, currentStateYPosition);

        this.getChildren().addAll(stateCircle, stateIdText, finalStateArc, startStatePointLine1, startStatePointLine2, reflexiveArrowShaftArc, reflexiveArrowTipPolygon);
    }

    public void toggleStandardStateUIComponent() {
        startStatePointLine1.setVisible(false);
        startStatePointLine2.setVisible(false);
        finalStateArc.setVisible(false);
    }


    public void toggleStartStateUIComponent(boolean isStartStateVisible) {
        startStatePointLine1.setVisible(isStartStateVisible);
        startStatePointLine2.setVisible(isStartStateVisible);
    }


    public void toggleFinalStateUIComponent(boolean isFinalStateVisible) {
        finalStateArc.setVisible(isFinalStateVisible);
    }

    public void toggleReflexiveArrowUIComponent(boolean isReflexiveArrowVisible, HashSet<TransitionModel> transitionsLinkingToResultingStateSet) {

        //Create popover to list applicable transitions for given transition
        listOfTransitionsVBox = new VBox();
        for (TransitionModel transitionModel : transitionsLinkingToResultingStateSet) {
            Label newLabel = new Label(transitionModel.toString());
            listOfTransitionsVBox.getChildren().add(newLabel);
        }

        PopOver listOfTransitionsPopOver = new PopOver(listOfTransitionsVBox);

        reflexiveArrowShaftArc.setOnMouseEntered(mouseEvent -> {
            listOfTransitionsPopOver.show(reflexiveArrowShaftArc);
        });

        reflexiveArrowShaftArc.setOnMouseExited(mouseEvent -> {
            //Hide PopOver when mouse exits label
            listOfTransitionsPopOver.hide();
        });

        reflexiveArrowShaftArc.setVisible(isReflexiveArrowVisible);
        reflexiveArrowTipPolygon.setVisible(isReflexiveArrowVisible);
    }

    public void removeReflexiveTransition(TransitionModel transitionModelToRemove) {
        Iterator<Node> iter = listOfTransitionsVBox.getChildren().iterator();

        while (iter.hasNext()) {
            Label labelToRemove = (Label) iter.next();
            if (labelToRemove.getText().equals(transitionModelToRemove.toString())) {
                iter.remove();
            }
        }
        if (listOfTransitionsVBox.getChildren().isEmpty()) {
            reflexiveArrowShaftArc.setVisible(false);
            reflexiveArrowTipPolygon.setVisible(false);
        }
    }


    private void setUpUIListeners() {
        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramController);
        //Link listener to events
        this.setOnMousePressed(diagramListener);
        this.setOnMouseDragged(diagramListener);
        this.setOnMouseReleased(diagramListener);
    }

    public String getStateID() {
        return stateId;
    }

    @Override
    public String toString() {
        return "StateView: " + stateId;
    }


    public Arc getReflexiveArrowShaftArc() {
        return reflexiveArrowShaftArc;
    }

    public Polygon getReflexiveArrowTipPolygon() {
        return reflexiveArrowTipPolygon;
    }
}