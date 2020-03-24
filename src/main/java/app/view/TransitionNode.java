package app.view;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * View blueprint for a transition node.
 * </p>
 */
public class TransitionNode extends Line {
    //Reference to the state nodes linked to view.
    private StateNode currentStateNode;
    private StateNode resultingStateNode;
    //Reference to UI components for view.
    private VBox transitionListVBox;
    public TransitionNode(StateNode currentStateNode, StateNode resultingStateNode) {
        this.currentStateNode = currentStateNode;
        this.resultingStateNode = resultingStateNode;
        setUpUIComponents();
    }
    /**
     * Sets up the UI components of the view.
     */
    private void setUpUIComponents() {
        transitionListVBox = new VBox();
        transitionListVBox.setStyle("-fx-background-color:#ffffff;-fx-border-width:2px;-fx-border-color:black;");
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
    }
    // Getters for state nodes linked to view.
    public StateNode getCurrentStateNode() {
        return currentStateNode;
    }
    public StateNode getResultingStateNode() {
        return resultingStateNode;
    }
    // Getters for UI components of the view.
    public VBox getTransitionListVBox() {
        return transitionListVBox;
    }
}