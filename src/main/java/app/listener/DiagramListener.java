package app.listener;
import app.presenter.DiagramScenePresenter;
import app.view.DiagramScene;
import app.view.StateNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Listener attached to diagram scene nodes.
 * </p>
 */
public class DiagramListener implements EventHandler {
    private final DiagramScenePresenter diagramScenePresenter;
    /**
     * Constructor of the diagram scene listener, used to instantiate an instance of the listener.
     * @param diagramScenePresenter a reference to the presenter which responds to the events picked up by the
     * listener.
     */
    public DiagramListener(DiagramScenePresenter diagramScenePresenter) {
        this.diagramScenePresenter = diagramScenePresenter;
    }
    /**
     * Method which routes user commands (events) to the presenter to act upon that data.
     * @param event which occurred on a node that the listener is attached to.
     */
    @Override
    public void handle(Event event) {
        String eventType = event.getEventType().toString();
        MouseEvent mouseEvent = (MouseEvent) event;
        //Node selected
        Node picked = mouseEvent.getPickResult().getIntersectedNode();
        if (picked instanceof DiagramScene) {
            if (event.getSource() instanceof DiagramScene) {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    //Add the state to diagram
                    double relXPosOfEvent = mouseEvent.getX();
                    double relYPosOfEvent = mouseEvent.getY();
                    diagramScenePresenter.addStateViewOntoDiagramViewDynamicRender(relXPosOfEvent, relYPosOfEvent);
                }
            }
        }
        if (picked instanceof Circle || picked instanceof Text || picked instanceof Arc) {
            if (event.getSource() instanceof StateNode) {
                StateNode stateNode = (StateNode) event.getSource();
                double absXPosOfEvent = mouseEvent.getScreenX();
                double absYPosOfEvent = mouseEvent.getScreenY();
                if (eventType.equals("MOUSE_PRESSED")) {
                    diagramScenePresenter.stateViewOnMousePressed(stateNode, absXPosOfEvent, absYPosOfEvent);
                    // Popup dialog
                    if (mouseEvent.isPopupTrigger()) {
                        diagramScenePresenter.stateViewContextMenuPopUp(stateNode);
                    }
                }
                if (eventType.equals("MOUSE_DRAGGED")) {
                    diagramScenePresenter.stateViewOnMouseDragged(stateNode, absXPosOfEvent, absYPosOfEvent);
                }
                if (eventType.equals("MOUSE_RELEASED")) {
                    diagramScenePresenter.stateViewOnMouseReleased(stateNode);
                }
            }
        }
    }
}


