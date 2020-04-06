package app.model;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a computation tree, where nodes are configurations and transitions are branches.
 * </p>
 */
public class SimulationModel {
    private final String EMPTY = "\u03B5";
    private final MachineModel machineModel;
    private final String inputWord;
    private ConfigurationModel currentConfig;
    private final TapeModel currentTapeModel;
    private final StackModel currentStackModel;
    private final ArrayList<ConfigurationModel> computationTreeArrayList;
    private final ArrayList<ConfigurationModel> computationPathArrayList;
    private boolean isNFA = false;
    private int numOfPossibleSuccessPaths = 0;
    private int numOfPossibleFailPaths = 0;
    private int numOfPossibleStuckPaths = 0;
    private int numOfPossibleInfinitePaths = 0;
    public SimulationModel(MachineModel machineModel, String inputWord) {
        this.machineModel = machineModel;
        this.currentTapeModel = new TapeModel();
        this.currentStackModel = new StackModel();
        this.inputWord = inputWord;
        computationTreeArrayList = new ArrayList<>();
        computationPathArrayList = new ArrayList<>();
        //Set the root node
        currentTapeModel.loadInput(inputWord);
        currentConfig = new ConfigurationModel(null, null, machineModel.getStartStateModel(), currentTapeModel, currentStackModel);
        currentConfig.setChildrenConfigurations(findChildrenConfigurations(currentConfig));
        currentConfig.markAsVisited();
        //Add currentConfig to the path
        computationTreeArrayList.add(currentConfig);
    }
    /**
     * Method used to move to the next configuration in a computation given a transition to reach the next
     * configuration.
     * @param selectedTransitionModelToTake the transition used to move to the next configuration in a computation.
     */
    public void next(TransitionModel selectedTransitionModelToTake) {
        // Search for the next configuration that can be reached given the selected transition to move to
        for (ConfigurationModel currentConfigurationModelChild : currentConfig.getChildrenConfigurations()) {
            if (selectedTransitionModelToTake.equals(currentConfigurationModelChild.getTransitionModelTakenToReachCurrentConfiguration())) {
                currentConfig = currentConfigurationModelChild;
                // Set the current configuration to the next
                currentConfig.markAsVisited();
                currentTapeModel.setHead(currentConfig.getHeadPosition());
                currentStackModel.setContent(currentConfig.getStackContent());
                //Setup current configuration children configurations
                List<ConfigurationModel> childrenConfigurationList = findChildrenConfigurations(currentConfig);
                currentConfig.setChildrenConfigurations(childrenConfigurationList);
                // Set type of configuration
                setTypeOfConfiguration();
                if (childrenConfigurationList.isEmpty()) {
                    //no more paths to search for this child
                    if (!(currentConfig.isSuccessConfig() || currentConfig.isFailConfig())) {
                        currentConfig.setStuckConfig(true);
                        computationPathArrayList.add(currentConfig);
                    }
                }
            }
        }
    }
    /**
     * Method used to determine the next possible configuration in a computation from a given configuration.
     * @param configurationModel the configuration used to determine the next possible configuration in a computation.
     * @return a list of possible configurations that can be reached from the given configuration.
     */
    public List<ConfigurationModel> findChildrenConfigurations(ConfigurationModel configurationModel) {
        return machineModel.getTransitionModelSet()
                .stream()
                .filter(transitionModel -> transitionModel.getCurrentStateModel().equals(configurationModel.getCurrentStateModel()))
                .filter(transitionModel -> transitionModel.getInputSymbol().equals(currentTapeModel.getAtHead()) || transitionModel.getInputSymbol().equals(EMPTY))
                .filter(transitionModel -> transitionModel.getStackSymbolToPop().equals(currentStackModel.peak()) || transitionModel.getStackSymbolToPop().equals(EMPTY))
                .map(this::generateConfig)
                .collect(Collectors.toList());
    }
    /**
     * Determines whether the configuration is a success configuration or fail configuration.
     */
    public void setTypeOfConfiguration() {
        if (currentTapeModel.isEmpty()) {
            if (machineModel.isAcceptanceByFinalState() && currentConfig.getCurrentStateModel().isFinalState()) {
                currentConfig.setSuccessConfig(true);
                computationPathArrayList.add(currentConfig);
                ++numOfPossibleSuccessPaths;
            } else if (machineModel.isAcceptanceByEmptyStack() && currentConfig.getStackContent().isEmpty()) {
                currentConfig.setSuccessConfig(true);
                computationPathArrayList.add(currentConfig);
                ++numOfPossibleSuccessPaths;
            } else {
                currentConfig.setFailConfig(true);
                computationPathArrayList.add(currentConfig);
                ++numOfPossibleFailPaths;
            }
        }
    }
    /**
     * Method which generates the computation tree for a given simulation.
     */
    public void createTree() {
        while (currentConfig.getDepth() < 1000001L) {
            setTypeOfConfiguration();
            int result = next(); // if one is returned more children exist
            if (result == 1) {
                computationTreeArrayList.add(currentConfig);
            }
            //Returning 8 when no more children present to search for given parent
            if (result == 0) {
                //Check if children have all be explored of root
                previous();
                if (currentConfig == null) {
                    break;
                }
                computationTreeArrayList.add(currentConfig);
            }
            if (currentConfig.getDepth() == 1000000L) {
                currentConfig.setInfiniteConfig(true);
                ++numOfPossibleInfinitePaths;
                computationPathArrayList.add(currentConfig);
                previous();
                computationTreeArrayList.add(currentConfig);
            }
        }
    }
    /**
     * Method which determines the next configuration in the computation if one exists.
     * @return an integer 1 if a next configuration is reachable otherwise returns 0.
     */
    public int next() {
        //Retrieve applicable configurations stored in the current configuration
        List<ConfigurationModel> childrenConfigurations = currentConfig.getChildrenConfigurations();
        if (currentConfig.getChildrenConfigurations() == null) {
            //Search for possible children configurations to move to (the transitions have already been applied to this list)
            childrenConfigurations = findChildrenConfigurations(currentConfig);
            currentConfig.setChildrenConfigurations(childrenConfigurations);
        }
        ConfigurationModel toExplore;
        // Parent has no children i.e. no applicable transitions
        if (childrenConfigurations.isEmpty()) {
            //no more paths to search for this child
            if (!(currentConfig.isSuccessConfig() || currentConfig.isFailConfig())) {
                currentConfig.setStuckConfig(true);
                computationPathArrayList.add(currentConfig);
                ++numOfPossibleStuckPaths;
            }
            return 0; // Go back to parent
        }
        //Parent has a single child, then a check if path is deterministic
        if (childrenConfigurations.size() == 1) {
            //Explore deterministic path
            toExplore = childrenConfigurations.get(0);
            //Check if this child is visited
            if (toExplore.isVisited()) {
                return 0; // Go back to parent
            }
        } else {
            //Explore non-deterministic path
            isNFA = true;
            //Find configuration to explore that hasn't been explored yet, if all paths have been explored then set applicable configuration to null
            toExplore = childrenConfigurations.stream().filter(config -> !config.isVisited()).findFirst().orElse(null);
            // All children are visited
            if (toExplore == null) {
                return 0; // Go back to parent
            }
        }
        //Set the new config (i.e. move to next applicable configuration
        currentConfig = toExplore;
        currentConfig.markAsVisited(); // Mark the currently explored config as explored
        currentTapeModel.setHead(toExplore.getHeadPosition());
        currentStackModel.setContent(toExplore.getStackContent());
        return 1;
    }
    //Apply action given a transition and return the resulting configuration
    public ConfigurationModel generateConfig(TransitionModel transitionModelToNextConfiguration) {
        TapeModel currentTapeModel = new TapeModel();
        currentTapeModel.loadInput(inputWord);
        currentTapeModel.setHead(this.currentTapeModel.getHead());
        StackModel currentStackModel = new StackModel();
        currentStackModel.setContent(this.currentStackModel.getContent());
        if (!(transitionModelToNextConfiguration.getInputSymbol().equals(EMPTY))) {
            currentTapeModel.readSymbol();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPop().equals(EMPTY))) {
            currentStackModel.pop();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPush().equals(EMPTY))) {
            currentStackModel.push(transitionModelToNextConfiguration.getStackSymbolToPush());
        }
        return new ConfigurationModel(currentConfig, transitionModelToNextConfiguration, transitionModelToNextConfiguration.getResultingStateModel(), currentTapeModel, currentStackModel);
    }
    /**
     * Method which determines the previous configuration in a computation.
     */
    public void previous() {
        ConfigurationModel previous = currentConfig.getParentConfiguration();
        if (previous != null) {
            currentStackModel.setContent(previous.getStackContent());
            currentTapeModel.setHead(previous.getHeadPosition());
        }
        currentConfig = previous;
    }
    // Getters for data pretaining to a simulation found in the attributes of the class. ¬
    public ConfigurationModel getCurrentConfig() {
        return currentConfig;
    }
    public ArrayList<ConfigurationModel> getComputationTreeArrayList() {
        return computationTreeArrayList;
    }
    public int getNumOfPossibleSuccessPaths() {
        return numOfPossibleSuccessPaths;
    }
    public int getNumOfPossibleFailPaths() {
        return numOfPossibleFailPaths;
    }
    public int getNumOfPossibleStuckPaths() {
        return numOfPossibleStuckPaths;
    }
    public int getNumOfPossibleInfinitePaths() {
        return numOfPossibleInfinitePaths;
    }
    public ArrayList<ConfigurationModel> getComputationPathArrayList() {
        return computationPathArrayList;
    }
    public boolean isNFA() {
        return isNFA;
    }
}


