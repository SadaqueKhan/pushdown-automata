package app.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a configuration, implemented as node within a computation tree by storing references to
 * a parent configuration node, and children configuration nodes.
 * </p>
 */
public class ConfigurationModel {
    private final ConfigurationModel parentConfiguration;
    private final TransitionModel transitionModelTakenToReachCurrentConfiguration;
    private final StateModel currentStateModel;
    private final TapeModel currentTapeModel;
    private final StackModel currentStackModel;
    private boolean isVisited;
    private final long depth;
    private long branch;
    private boolean isSuccessConfig = false;
    private boolean isInfiniteConfig = false;
    private boolean isStuckConfig = false;
    private boolean isFailConfig = false;
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
        this.branch = 0;
        this.depth = parentConfiguration == null ? 0 : parentConfiguration.getDepth() + 1;
    }
    /**
     * Get the branch value to reach this position from the root
     * @return the branch long data type value to reach this position from the root
     */
    public long getBranch() {
        return branch;
    }
    /**
     * Get the depth value to reach this position from the root
     * @return the depth long data type to reach this position from the root
     */
    public long getDepth() {
        return depth;
    }
    /**
     * Get the head position of the tape for of this configuration.
     * @return the {@code ConfigurationModel} of this configuration
     */
    public int getHeadPosition() {
        return currentTapeModel.getHead();
    }
    /**
     * Get the parent configuration of this configuration.
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
     * @return <tt>true</tt> if the configuration has been visited
     */
    public boolean isVisited() {
        return isVisited;
    }
    /**
     * Sets this configuration to visited with respect to traversal of the tree algorithm.
     */
    public void markAsVisited() {
        isVisited = true;
    }
    /**
     * Sets the branch position of this configuration.
     * @param branch position of this configuration
     */
    private void setBranch(int branch) {
        this.branch = branch;
    }
    /**
     * Get this configurations children configurations.
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
        if (this.childrenConfigurations.size() > 0) {
            int branchId = 0;
            for (ConfigurationModel configuration : childrenConfigurations) {
                configuration.setBranch(++branchId);
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
     * Method to return a string representation of this object.
     * @return the string representation of the object.
     */
    @Override
    public String toString() {
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
        // Concatenate the three elements that make up a configuration into one string
        return "( " + currentStateModel.getStateId() + ", " + currentTapeString + ", " + stackState + " )";
    }
    /**
     * Check if the configuration is a success configuration.
     * @return <tt>true</tt> if the configuration is a success configuration
     */
    public boolean isSuccessConfig() {
        return isSuccessConfig;
    }
    /**
     * Sets this configuration to a success configuration.
     */
    void setSuccessConfig(boolean successConfig) {
        isSuccessConfig = successConfig;
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
    void setStuckConfig(boolean stuckConfig) {
        isStuckConfig = stuckConfig;
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
    void setFailConfig(boolean failConfig) {
        isFailConfig = failConfig;
    }
    /**
     * Check if the configuration is the last configuration computable for this application indicating a large
     * computation path.
     * @return <tt>true</tt> if the configuration is the last configuration computable for this application
     * indicating a large computation.
     */
    public boolean isInfiniteConfig() {
        return isInfiniteConfig;
    }
    /**
     * Sets this configuration to the last configuration computable for this application indicating a large computation.
     */
    void setInfiniteConfig(boolean infiniteConfig) {
        isInfiniteConfig = infiniteConfig;
    }
}


