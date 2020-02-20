package app.model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private Configuration parentConfiguration;
    private TransitionModel transitionModelTakenToReachCurrentConfiguration;
    private StateModel currentStateModel;
    private int headPosition;
    private ArrayList<String> stackContent;
    private boolean isVisited;
    private List<Configuration> childrenConfigurations;


    public Configuration(Configuration parentConfiguration, TransitionModel transitionModelTakenToReachCurrentConfiguration, StateModel currentStateModel, int headPosition, ArrayList<String> stackContent) {
        this.parentConfiguration = parentConfiguration;
        this.transitionModelTakenToReachCurrentConfiguration = transitionModelTakenToReachCurrentConfiguration;
        this.currentStateModel = currentStateModel;
        this.headPosition = headPosition;
        this.stackContent = stackContent;
        this.isVisited = false;
    }


    public Configuration getParentConfiguration() {
        return parentConfiguration;
    }

    public TransitionModel getTransitionModelTakenToReachCurrentConfiguration() {
        return transitionModelTakenToReachCurrentConfiguration;
    }

    public StateModel getCurrentStateModel() {
        return currentStateModel;
    }

    public int getHeadPosition() {
        return headPosition;
    }

    public ArrayList<String> getStackContent() {
        return stackContent;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public List<Configuration> getChildrenConfigurations() {
        return childrenConfigurations;
    }

    //Setters
    public void markAsVisited() {
        isVisited = true;
    }


    public void setChildrenConfigurations(List<Configuration> childrenConfigurations) {
        this.childrenConfigurations = childrenConfigurations;
    }
}
