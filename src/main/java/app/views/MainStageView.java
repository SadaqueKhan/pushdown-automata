package app.views;

import app.controllers.MainStageController;
import app.listeners.MainStageListener;
import app.models.MachineModel;
import app.models.StateModel;
import app.models.TransitionModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

public class MainStageView extends BorderPane {

    private final MainStageController mainStageController;

    //Top content elements
    private TextField inputTextField;
    AutoCompletionBinding<String> autoCompletionBinding;
    String cssLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: solid;\n";
    //Center content elements
    private ToggleButton toggleDiagramButton;
    private ToggleButton toggleTransitionTableButton;
    private SegmentedButton segmentedButton;
    private VBox containerForCenterNodes;
    private Set<String> inputWordSet;
    private ProgressBar progressBar;
    //Bottom content elements
    private TapeView tapeView;
    private VBox containerForBotoomNodes;

    public MainStageView(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        setUpUIComponents();
        setUpUILayout();
        setUpUIListeners();
    }

    private void setUpUIComponents() {

        Menu menu = new Menu("Menu 1");


        MenuItem save = new MenuItem("Save");

        save.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Machine");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));

                File fileChoosen = fileChooser.showSaveDialog(mainStageController.getPrimaryStage());

                if (fileChoosen != null) {
                    JAXBContext contextObj = JAXBContext.newInstance(MachineModel.class);
                    Marshaller marshallerObj = contextObj.createMarshaller();
                    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    marshallerObj.marshal(mainStageController.getMachineModel(), new FileOutputStream(fileChoosen));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });


        MenuItem load = new MenuItem("Load");

        load.setOnAction(e -> {

            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load Machine");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML File", "*.xml"));


                File fileChoosen = fileChooser.showOpenDialog(mainStageController.getPrimaryStage());

                if (fileChoosen != null) {

                    JAXBContext jaxbContext = JAXBContext.newInstance(MachineModel.class);

                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    MachineModel que = (MachineModel) jaxbUnmarshaller.unmarshal(fileChoosen);

                    for (TransitionModel transitionModel : que.getTransitionModelSet()) {
                        System.out.println(transitionModel);
                    }

                    for (StateModel stateModel : que.getStateModelSet()) {
                        System.out.println(stateModel.getStateId());
                    }

                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        });


        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem help = new MenuItem("Help");
        menu.getItems().addAll(save, load, separator, help);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);


        //Setup top GUI elements
        inputTextField = new TextField();
        inputTextField.setPromptText("Enter input word");
        inputWordSet = new HashSet<>();
        autoCompletionBinding = TextFields.bindAutoCompletion(inputTextField, inputWordSet);
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setVisible(false);
        VBox containerForTopNodes = new VBox();
        containerForTopNodes.setFillWidth(true);
        containerForTopNodes.getChildren().addAll(menuBar, new Text("Input Word"), inputTextField, progressBar);


        this.setTop(containerForTopNodes);

        //Setup center GUI elements
        this.toggleDiagramButton = new ToggleButton("Diagram");
        this.toggleTransitionTableButton = new ToggleButton("Table");
        this.segmentedButton = new SegmentedButton();
        segmentedButton.getButtons().addAll(toggleDiagramButton, toggleTransitionTableButton);
        toggleDiagramButton.setSelected(true);

        this.containerForCenterNodes = new VBox();
        containerForCenterNodes.setPadding(new Insets(10, 50, 50, 50));
        containerForCenterNodes.setSpacing(10);
        containerForCenterNodes.setAlignment(Pos.TOP_CENTER);
        containerForCenterNodes.setStyle(cssLayout);
        containerForCenterNodes.getChildren().add(segmentedButton);

        this.setCenter(containerForCenterNodes);

        //Setup bottom GUI elements
        this.tapeView = new TapeView();

        this.containerForBotoomNodes = new VBox();
        containerForBotoomNodes.setPadding(new Insets(10, 50, 50, 50));
        containerForBotoomNodes.setSpacing(10);
        containerForBotoomNodes.setStyle(cssLayout);
        containerForBotoomNodes.getChildren().addAll(new Text("Tape"), tapeView);

        this.setBottom(containerForBotoomNodes);
    }

    private void setUpUILayout() {
    }

    private void setUpUIListeners() {
        MainStageListener mainStageListener = new MainStageListener(mainStageController);
        inputTextField.setOnKeyPressed(mainStageListener);
        toggleDiagramButton.setOnAction(mainStageListener);
        toggleTransitionTableButton.setOnAction(mainStageListener);
    }

    public TextField getInputTextField() {
        return inputTextField;
    }

    public Set<String> getInputWordSet() {
        return inputWordSet;
    }

    public AutoCompletionBinding<String> getAutoCompletionBinding() {
        return autoCompletionBinding;
    }

    public void setAutoCompletionBinding(AutoCompletionBinding<String> autoCompletionBinding) {
        this.autoCompletionBinding = autoCompletionBinding;
    }

    public VBox getContainerForCenterNodes() {
        return containerForCenterNodes;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TapeView getTapeView() {
        return tapeView;
    }
}
