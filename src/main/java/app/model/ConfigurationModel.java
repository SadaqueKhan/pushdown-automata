package app.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a configuration, which specify a node within a tree
 * </p>
 */
public class ConfigurationModel {
    private final ConfigurationModel parentConfiguration;
    private final TransitionModel transitionModelTakenToReachCurrentConfiguration;
    private final StateModel currentStateModel;
    private final TapeModel currentTapeModel;
    private final StackModel currentStackModel;

    private boolean isVisited;
    private int branch;
    private int step;

    private boolean isSuccessConfig = false;
    private boolean isFailConfig = false;
    private boolean isStuckConfig = false;
    private boolean isInfiniteConfig = false;

    private List<ConfigurationModel> childrenConfigurations;

    /**
     * Initialises the configuration
     */
    public ConfigurationModel(ConfigurationModel parentConfiguration, TransitionModel transitionModelTakenToReachCurrentConfiguration, StateModel currentStateModel, TapeModel currentTapeModel, StackModel currentStackModel) {
        this.parentConfiguration = parentConfiguration;
        this.transitionModelTakenToReachCurrentConfiguration = transitionModelTakenToReachCurrentConfiguration;
        this.currentStateModel = currentStateModel;
        this.currentTapeModel = currentTapeModel;
        this.currentStackModel = currentStackModel;

        this.isVisited = false;
        this.branch = 1;
        this.step = parentConfiguration == null ? 0 : parentConfiguration.getStep() + 1;
    }
    /**
     * Get the step value to reach this position from the root
     *
     * @return the step integer value to reach this position from the root
     */
    public int getStep() {
        return step;
    }
    /**
     * Get the head position of the tape for of this configuration.
     *
     * @return the {@code ConfigurationModel} of this configuration
     */
    public int getHeadPosition() {
        return currentTapeModel.getHead();
    }
    /**
     * Get the parent configuration of this configuration.
     *
     * @return the parent {@code ConfigurationModel} of this configuration
     */
    public ConfigurationModel getParentConfiguration() {
        return parentConfiguration;
    }

    /**
     * Get the transition model taken to reach this configuration.
     * @return the {@code TransitionModel} took to reach this configuration
     */
    public TransitionModel getTransitionModelTakenToReachCurrentConfiguration() {
        return transitionModelTakenToReachCurrentConfiguration;
    }

    /**
     * Get the state model for this configuration.
     * @return the {@code StateModel} linked to this configuration
     */
    public StateModel getCurrentStateModel() {
        return currentStateModel;
    }

    /**
     * Get the stack content for this configuration.
     * @return the {@code ArrayList<String>} containing stack content for this configuration
     */
    public ArrayList<String> getStackContent() {
        return currentStackModel.getContent();
    }

    /**
     * Checks if the configuration has been visited with respect to traversal of the tree algorithm.
     * @return  <tt>true</tt> if the configuration has been visited
     */
    public boolean isVisited() {
        return isVisited;
    }

    /**
     *  Sets this configuration to visited with respect to traversal of the tree algorithm.
     */
    public void markAsVisited() {
        isVisited = true;
    }

    /**
     * Sets the branch position of this configuration.
     *
     * @param branch position of this configuration
     */
    public void setBranch(int branch) {
        this.branch = branch;
    }

    /**
     * Get this configurations children configurations.
     *
     * @return the {@code List<ConfigurationModel>} children configuration associated with this configuration
     */
    public List<ConfigurationModel> getChildrenConfigurations() {
        return childrenConfigurations;
    }

    /**
     * Sets the children configuration for this configuration.
     * @param childrenConfigurations for this configuration
     */
    public void setChildrenConfigurations(List<ConfigurationModel> childrenConfigurations) {
        this.childrenConfigurations = childrenConfigurations;
        // Compute the branch id of this configuration
        if (this.childrenConfigurations.size() > 1) {
            int branchId = 1;
            for (ConfigurationModel configuration : childrenConfigurations) {
                configuration.setBranch(branchId++);
            }
        }
    }

    /**
     * Get this configurations path from the root.
     * @return a {@code ArrayList<ConfigurationModel>} which is a list of configurations take from the root to reach this configuration
     */
    public ArrayList<ConfigurationModel> getPath() {
        ArrayList<ConfigurationModel> pathList = new ArrayList<>();
        if (parentConfiguration != null) {
            ArrayList<ConfigurationModel> configurationToRoot = new ArrayList<>();
            configurationToRoot.add(this);
            configurationToRoot.add(parentConfiguration);
            ConfigurationModel tempConfigurationModel = parentConfiguration.getParentConfiguration();
            while (tempConfigurationModel != null) {
                configurationToRoot.add(tempConfigurationModel);
                tempConfigurationModel = tempConfigurationModel.getParentConfiguration();
            }
            Collections.reverse(configurationToRoot);
            pathList.addAll(configurationToRoot);
            return pathList;
        }
        pathList.add(this);
        return pathList;
    }

    /**
     * Get this configurations toString.
     *
     * @return the string identifying the configuration node in the tree
     */
    @Override
    public String toString() {
        // Check if the start state
        if (parentConfiguration == null) {
            // create the string for the root node configuration in the tree
            return "At the start state: " + currentStateModel.getStateId();
        }

        // Otherwise create string for other node configuration in the tree

        // Create string of the current tape state at this configuration
        StringBuilder currentTapeString = new StringBuilder();
        if (currentTapeModel.getHead() == currentTapeModel.tapeSize()) {
            currentTapeString.append("\u03B5");
        } else {
            for (int i = currentTapeModel.getHead(); i < currentTapeModel.tapeSize(); i++) {
                currentTapeString.append(currentTapeModel.getInputTape().get(i));
            }
        }

        // Create string of the current stack state at this configuration
        StringBuilder stackState = new StringBuilder();
        stackState.append(currentStackModel.isEmpty() ? "\u03B5" : "");
        for (String stackSymbol : currentStackModel.getContent()) {
            stackState.append(stackSymbol);
        }

        // Create string of the position of the configuration in the tree search
        String positionInTreeString = "depth " + step + ":branch " + branch + ": ";

        // Create a string depicting the current configuration state
        String configurationString = "( " + currentStateModel.getStateId() + ", " + currentTapeString + ", " + stackState + " )";
        return positionInTreeString + this.getTransitionModelTakenToReachCurrentConfiguration() + " -> " + configurationString;
    }

    /**
     * Check if the configuration is a success configuration.
     *
     * @return <tt>true</tt> if the configuration is a success configuration
     */
    public boolean isSuccessConfig() {
        return isSuccessConfig;
    }

    /**
     * Sets this configuration to a success configuration.
     */
    public void setSuccessConfig(boolean successConfig) {
        isSuccessConfig = successConfig;
    }

    /**
     * Check if the configuration is a fail configuration.
     * @return <tt>true</tt> if the configuration is a fail configuration
     */
    public boolean isFailConfig() {
        return isFailConfig;
    }

    /**
     * Sets this configuration to a fail configuration.
     */
    public void setFailConfig(boolean failConfig) {
        isFailConfig = failConfig;
    }

    /**
     * Check if the configuration is a stuck configuration.
     * @return <tt>true</tt> if the configuration is a stuck configuration
     */
    public boolean isStuckConfig() {
        return isStuckConfig;
    }

    /**
     * Sets this configuration to a stuck configuration.
     */
    public void setStuckConfig(boolean stuckConfig) {
        isStuckConfig = stuckConfig;
    }

    /**
     * Check if the configuration is the last configuration in a possible infinite configuration.
     *
     * @return <tt>true</tt> if the configuration is the last configuration apart of a possible infinite configuration
     */
    public boolean isInfiniteConfig() {
        return isInfiniteConfig;
    }

    /**
     * Sets this configuration to the last configuration apart of an infinite configuration.
     */
    public void setInfiniteConfig(boolean infiniteConfig) {
        isInfiniteConfig = infiniteConfig;
    }
}


