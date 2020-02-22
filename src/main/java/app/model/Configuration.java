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

    private boolean isSuccessConfig = false;
    private boolean isFailConfig = false;
    private boolean isStuckConfig = false;


    private int step;
    private int branchId;


    public Configuration(Configuration parentConfiguration, TransitionModel transitionModelTakenToReachCurrentConfiguration, StateModel currentStateModel, int headPosition, ArrayList<String> stackContent) {
        this.parentConfiguration = parentConfiguration;
        this.transitionModelTakenToReachCurrentConfiguration = transitionModelTakenToReachCurrentConfiguration;
        this.currentStateModel = currentStateModel;
        this.headPosition = headPosition;
        this.stackContent = stackContent;
        this.isVisited = false;
        this.step = parentConfiguration == null ? 0 : parentConfiguration.getStep() + 1;
        this.branchId = -1;
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

    public boolean isFailConfig() {
        return isFailConfig;
    }

    public void setFailConfig(boolean failConfig) {
        isFailConfig = failConfig;
    }

    public boolean isStuckConfig() {
        return isStuckConfig;
    }

    public void setStuckConfig(boolean stuckConfig) {
        isStuckConfig = stuckConfig;
    }

    @Override
    public String toString() {
        String parentStateModelString = "";
        String transitionModelTakenToReachCurrentConfigurationString = "";
        String currentStateModelString = "";
        if (parentConfiguration != null) {
            parentStateModelString = parentConfiguration.getCurrentStateModel().getStateId();
        } else {
            return "Start at: " + currentStateModel.getStateId();
        }

        if (transitionModelTakenToReachCurrentConfiguration != null) {
            transitionModelTakenToReachCurrentConfigurationString = transitionModelTakenToReachCurrentConfiguration.toString();
        }
        if (currentStateModel != null) {
            currentStateModelString = currentStateModel.getStateId();
        }

        String additionalInfo = generateAdditionalInfo();

        return additionalInfo + parentStateModelString + " => " + " { " + transitionModelTakenToReachCurrentConfigurationString + " } "
                + " => " + currentStateModelString;
    }

    private String generateAdditionalInfo() {
        String instruction = "step " + step + ":";
        instruction += branchId != -1 ? "branch " + branchId + ":" : "";
        instruction += " ";
        return instruction;
    }


    public int getStep() {
        return step;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}


