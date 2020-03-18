package app.view;

import app.listener.DiagramListener;
import app.presenter.DiagramPresenter;
import javafx.scene.layout.Pane;

public class DiagramScene extends Pane {
    private final DiagramPresenter diagramPresenter;
    private final String cssLayout = "-fx-border-color: black;\n" +
            "-fx-background-color: #f8fffa,\n" +
            "linear-gradient(from 0.5px 0.0px to 10.5px  0.0px, repeat, black 5%, transparent 5%),\n" +
            "linear-gradient(from 0.0px 0.5px to  0.0px 10.5px, repeat, black 5%, transparent 5%)";

    public DiagramScene(DiagramPresenter diagramPresenter) {
        this.diagramPresenter = diagramPresenter;
        setUpUIComponents();
        setUpUIListeners();
    }

    private void setUpUIComponents() {
        //Add style listener for this view
        this.setStyle(cssLayout);
        this.setMinSize(200, 500);
    }

    private void setUpUIListeners() {
        //Create listener for this view
        DiagramListener diagramListener = new DiagramListener(diagramPresenter);
        this.setOnMousePressed(diagramListener);
    }
}



