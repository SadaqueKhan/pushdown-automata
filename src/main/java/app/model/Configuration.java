package app.model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private Configuration previousConfiguration;
    private StateModel currentStateModel;
    private int headPosition;
    private ArrayList<String> stackContent;
    private boolean isVisited;
    private List<Configuration> configurations;


    public Configuration(Configuration previousConfiguration, StateModel currentStateModel, int headPosition, ArrayList<String> stackContent) {
        this.previousConfiguration = previousConfiguration;
        this.currentStateModel = currentStateModel;
        this.headPosition = headPosition;
        this.stackContent = stackContent;
        this.isVisited = false;
    }

    public Configuration getPreviousConfiguration() {
        return previousConfiguration;
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

    public void markAsVisited() {
        isVisited = true;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }
}
