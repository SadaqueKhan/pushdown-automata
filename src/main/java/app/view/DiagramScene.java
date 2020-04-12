package app.view;
import app.listener.DiagramSceneListener;
import app.presenter.DiagramScenePresenter;
import javafx.scene.layout.Pane;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Blueprint for a diagram scene.
 * </p>
 */
public class DiagramScene extends Pane {
    //Reference to views presenter.
    private final DiagramScenePresenter diagramScenePresenter;
    /**
     * Constructor of the diagram scene, used to instantiate an instance of the view.
     * @param diagramScenePresenter a reference to the views presenter.
     */
    public DiagramScene(DiagramScenePresenter diagramScenePresenter) {
        this.diagramScenePresenter = diagramScenePresenter;
        setUpUIComponents();
        setUpUIListeners();
    }
    /**
     * Sets up the UI components of the view.
     */
    private void setUpUIComponents() {
        this.setStyle("-fx-border-color: black;\n" +
                "-fx-background-color: #f8fffa,\n" +
                "linear-gradient(from 0.5px 0.0px to 10.5px  0.0px, repeat, black 5%, transparent 5%),\n" +
                "linear-gradient(from 0.0px 0.5px to  0.0px 10.5px, repeat, black 5%, transparent 5%)");
        this.setMinSize(190, 515);
    }
    /**
     * Sets up the listeners for UI components of the view.
     */
    private void setUpUIListeners() {
        //Create listener for UI component for this view.
        DiagramSceneListener diagramSceneListener = new DiagramSceneListener(diagramScenePresenter);
        //Link listener to events on UI components for this view.
        this.setOnMousePressed(diagramSceneListener);
    }
}



