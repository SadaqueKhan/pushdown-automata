package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationModel {

    private final String EMPTY = "\u03B5";
    private MachineModel machineModel;
    private final String inputWord;

    private ConfigurationModel currentConfig;
    private TapeModel currentTapeModel;
    private StackModel currentStackModel;

    private ArrayList<ConfigurationModel> configurationPath;
    private ArrayList<ConfigurationModel> leafConfigurationPath;

    private boolean isNFA = false;
    private int numOfPossibleInfinitePaths = 0;
    private int numOfPossibleSuccessPaths = 0;

    public SimulationModel(MachineModel machineModel, String inputWord) {
        this.machineModel = machineModel;
        this.currentTapeModel = new TapeModel();
        this.currentStackModel = new StackModel();
        this.inputWord = inputWord;
        configurationPath = new ArrayList<>();
        leafConfigurationPath = new ArrayList<>();

        //Set the root node 
        currentTapeModel.loadInput(inputWord);
        currentConfig = new ConfigurationModel(null, null, machineModel.findStartStateModel(), currentTapeModel, currentStackModel);
        currentConfig.markAsVisited();
        //Add currentConfig to the path
        configurationPath.add(currentConfig);
    }


    public int next() {
        //checking for acceptance
        if (isInAcceptingConfiguration() && !(currentConfig.isSuccessConfig())) {
            return 100;
        }

        //Retrieve applicable configurations stored in the current configuration
        List<ConfigurationModel> applicableConfigurations = currentConfig.getChildrenConfigurations();

        if (currentConfig.getChildrenConfigurations() == null) {
            //Search for possible children configurations to move to (the transitions have already been applied to this list)
            applicableConfigurations = configurationApplicable(currentConfig.getCurrentStateModel(), currentTapeModel.getAtHead(), currentStackModel.peak());
            currentConfig.setChildrenConfigurations(applicableConfigurations);
        }

        ConfigurationModel toExplore;

        // Parent has no children i.e. no applicable transitions
        if (applicableConfigurations.isEmpty()) {
            if (!(currentConfig.isSuccessConfig())) {
                leafConfigurationPath.add(currentConfig);
            }
            //no more paths to search for this child
            return 8; // Go back to parent
        }


        //Parent has a single child, then a check if path is deterministic
        if (applicableConfigurations.size() == 1) {

            //Explore deterministic path
            toExplore = applicableConfigurations.get(0);

            //Check if this child is visited
            if (toExplore.isVisited()) {
                return 8;
            }
        } else {
            //Explore non-deterministic path
            isNFA = true;

            //Find configuration to explore that hasn't been explored yet, if all paths have been explored then set applicable configuration to null
            toExplore = applicableConfigurations.stream().filter(config -> !config.isVisited()).findFirst().orElse(null);

            // All children are visited
            if (toExplore == null) {
                return 8; // Go back to parent
            }
        }

        // Move to next configuration
        loadConfiguration(toExplore);
        return 1;
    }

    public void loadConfiguration(ConfigurationModel toExplore) {
        currentConfig = toExplore;
        currentConfig.markAsVisited(); // Mark the currently explored config as explored
        currentTapeModel.setHead(toExplore.getHeadPosition());
        currentStackModel.setContent(toExplore.getStackContent());
        configurationPath.add(currentConfig);
    }

    public void previous() {
        ConfigurationModel previous = currentConfig.getParentConfiguration();
        if (previous != null) {
            currentStackModel.setContent(previous.getStackContent());
            currentTapeModel.setHead(previous.getHeadPosition());
        }
        currentConfig = previous;
    }


    public List<ConfigurationModel> configurationApplicable(StateModel currentStateModel, String currentInputSymbolAtHeadOnTape, String currentTopElementOnStack) {
        return machineModel.getTransitionModelSet()
                .stream()
                .filter(transitionModel -> transitionModel.getCurrentStateModel().equals(currentStateModel))
                .filter(transitionModel -> transitionModel.getInputSymbol().equals(currentInputSymbolAtHeadOnTape) || transitionModel.getInputSymbol().equals(EMPTY))
                .filter(transitionModel -> transitionModel.getStackSymbolToPop().equals(currentTopElementOnStack) || transitionModel.getStackSymbolToPop().equals(EMPTY))
                .map(transitionModel -> generateConfig(transitionModel))
                .collect(Collectors.toList());
    }

    //Apply action given a transition and return the resulting configuration
    private ConfigurationModel generateConfig(TransitionModel transitionModelToNextConfiguration) {
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


    public boolean isInAcceptingConfiguration() {
        if (currentTapeModel.isEmpty()) {
            if (machineModel.isAcceptanceByFinalState() && currentConfig.getCurrentStateModel().isFinalState()) {
                return true;
            } else if (machineModel.isAcceptanceByEmptyStack() && currentConfig.getStackContent().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int createTree() {
        while (currentConfig.getStep() < 51) {
            int result = next(); // if one is returned more children exist

            if (result == 100) {
                ++numOfPossibleSuccessPaths;
                currentConfig.setSuccessConfig(true);
                leafConfigurationPath.add(currentConfig);
            }
            //Returning 8 when no more children present to search for given parent
            if (result == 8) {
                //Check if children have all be explored of root
                previous();

                if (currentConfig == null) {
                    return 200;
                }
                configurationPath.add(currentConfig);
            }

            if (currentConfig.getStep() == 50) {
                currentConfig.setInfiniteConfig(true);
                ++numOfPossibleInfinitePaths;
                leafConfigurationPath.add(currentConfig);
                previous();
                configurationPath.add(currentConfig);
            }

        }
        return 300;
    }

    public ConfigurationModel getCurrentConfig() {
        return currentConfig;
    }

    public void setCurrentConfig(ConfigurationModel currentConfig) {
        this.currentConfig = currentConfig;
    }

    public TapeModel getCurrentTapeModel() {
        return currentTapeModel;
    }

    public void setCurrentTapeModel(TapeModel currentTapeModel) {
        this.currentTapeModel = currentTapeModel;
    }

    public StackModel getCurrentStackModel() {
        return currentStackModel;
    }

    public void setCurrentStackModel(StackModel currentStackModel) {
        this.currentStackModel = currentStackModel;
    }

    public ArrayList<ConfigurationModel> getConfigurationPath() {
        return configurationPath;
    }

    public int getNumOfPossibleInfinitePaths() {
        return numOfPossibleInfinitePaths;
    }

    public int getNumOfPossibleSuccessPaths() {
        return numOfPossibleSuccessPaths;
    }

    public ArrayList<ConfigurationModel> getLeafConfigurationPath() {
        return leafConfigurationPath;
    }

    public boolean isNFA() {
        return isNFA;
    }
}


