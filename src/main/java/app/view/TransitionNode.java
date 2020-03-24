package app.view;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for a transition node.
 * </p>
 */
public class TransitionNode extends Line {
    //Reference to the state nodes linked to view.
    private final StateNode currentStateNode;
    private final StateNode resultingStateNode;
    //Reference to UI components for view.
    private VBox transitionListVBox;
    /**
     * Constructor of a transition node, used to instantiate an instance of the view.
     * @param currentStateNode
     * @param resultingStateNode
     */
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