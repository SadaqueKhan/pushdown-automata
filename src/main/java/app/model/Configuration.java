package app.model;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private Configuration parentConfiguration;
    private TransitionModel transitionModelTakenToReachCurrentConfiguration;
    private StateModel currentStateModel;
    private int headPosition;
    private String inputTapeState;
    private ArrayList<String> stackContent;
    private boolean isVisited;
    private List<Configuration> childrenConfigurations;
    private boolean isSuccessConfig = false;
    private int step;
    private int branchId;

    public Configuration(Configuration parentConfiguration, TransitionModel transitionModelTakenToReachCurrentConfiguration, StateModel currentStateModel, String inputTapeState, int headPosition, ArrayList<String> stackContent) {
        this.parentConfiguration = parentConfiguration;
        this.transitionModelTakenToReachCurrentConfiguration = transitionModelTakenToReachCurrentConfiguration;
        this.currentStateModel = currentStateModel;
        this.headPosition = headPosition;
        this.stackContent = stackContent;
        this.isVisited = false;
        this.step = parentConfiguration == null ? 0 : parentConfiguration.getStep() + 1;
        this.branchId = 1;
        this.inputTapeState = inputTapeState;
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

    public void markAsVisited() {
        isVisited = true;
    }

    public void setChildrenConfigurations(List<Configuration> childrenConfigurations) {
        this.childrenConfigurations = childrenConfigurations;

        if (this.childrenConfigurations.size() > 1) {
            int branchId = 1;
            for (Configuration configuration : childrenConfigurations) {
                configuration.setBranchId(branchId++);
            }
        }
    }

    public boolean isSuccessConfig() {
        return isSuccessConfig;
    }

    public void setSuccessConfig(boolean successConfig) {
        isSuccessConfig = successConfig;
    }


    public int getStep() {
        return step;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public String toString() {
        if (parentConfiguration == null) {
            return "At " + currentStateModel.getStateId();
        }

        StringBuilder stackState = new StringBuilder();
        stackState.append(stackContent.isEmpty() ? "\u03B5" : "");
        for (String stackSymbol : stackContent) {
            stackState.append(stackSymbol);
        }

        String additionalInfo = "depth " + step + ":branch " + branchId + ": ";
        String configuration = "( " + currentStateModel.toString() + ", " + inputTapeState + ", " + stackState + " )";
        return additionalInfo + configuration;
    }
}


