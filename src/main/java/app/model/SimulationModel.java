package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationModel {
    private final String EMPTY = "\u03B5";
    private MachineModel machineModel;
    private TapeModel tapeModel;
    private StackModel stackModel;
    private final String inputWord;

    private ConfigurationModel currentConfig;
    private int numOfPossibleInfinitePaths = 0;
    private int numOfPossibleSuccessPaths = 0;

    private ArrayList<ConfigurationModel> configurationPath;
    private ArrayList<ConfigurationModel> leafConfigurationPath;

    private boolean isNFA = false;

    public SimulationModel(MachineModel machineModel, String inputWord) {
        this.tapeModel = new TapeModel();
        this.stackModel = new StackModel();
        this.inputWord = inputWord;
        configurationPath = new ArrayList<>();
        leafConfigurationPath = new ArrayList<>();

        loadMachine(machineModel);
        loadInput(inputWord);
    }

    public void loadMachine(MachineModel machineModel) {
        this.machineModel = machineModel;
    }

    public void loadInput(String input) {
        tapeModel.loadInput(input);
        currentConfig = new ConfigurationModel(null, null, machineModel.findStartStateModel(), inputWord, 0, stackModel.getContent());
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
            applicableConfigurations = configurationApplicable(currentConfig.getCurrentStateModel(), tapeModel.getAtHead(), stackModel.peak());
            currentConfig.setChildrenConfigurations(applicableConfigurations);
        }

        ConfigurationModel toExplore;

        // Parent has no children i.e. no applicable transitions
        if (applicableConfigurations.isEmpty()) {
            if (!(currentConfig.isFailConfig() || currentConfig.isSuccessConfig())) {
                currentConfig.setStuckConfig(true);
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

    private void loadConfiguration(ConfigurationModel toExplore) {
        currentConfig = toExplore;
        currentConfig.markAsVisited(); // Mark the currently explored config as explored
        tapeModel.setHead(toExplore.getHeadPosition());
        stackModel.setContent(toExplore.getStackContent());
        configurationPath.add(currentConfig);
    }

    public void previous() {
        ConfigurationModel previous = currentConfig.getParentConfiguration();
        if (previous != null) {
            stackModel.setContent(previous.getStackContent());
            tapeModel.setHead(previous.getHeadPosition());
        }
        currentConfig = previous;
    }


    public List<ConfigurationModel> configurationApplicable(StateModel stateModel, String inputSymbol, String stackSymbol) {
        return machineModel.getTransitionModelSet()
                .stream()
                .filter(transitionModel -> transitionModel.getCurrentStateModel().equals(stateModel))
                .filter(transitionModel -> transitionModel.getInputSymbol().equals(inputSymbol) || transitionModel.getInputSymbol().equals(EMPTY))
                .filter(transitionModel -> transitionModel.getStackSymbolToPop().equals(stackSymbol) || transitionModel.getStackSymbolToPop().equals(EMPTY))
                .map(transitionModel -> generateConfig(transitionModel))
                .collect(Collectors.toList());
    }

    //Apply action given a transition and return the resulting configuration
    private ConfigurationModel generateConfig(TransitionModel transitionModelToNextConfiguration) {

        TapeModel currentTapeModel = new TapeModel();
        currentTapeModel.loadInput(inputWord);
        currentTapeModel.setHead(tapeModel.getHead());

        StackModel currentStackModel = new StackModel();
        currentStackModel.setContent(stackModel.getContent());

        if (!(transitionModelToNextConfiguration.getInputSymbol().equals(EMPTY))) {
            currentTapeModel.readSymbol();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPop().equals(EMPTY))) {
            currentStackModel.pop();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPush().equals(EMPTY))) {
            currentStackModel.push(transitionModelToNextConfiguration.getStackSymbolToPush());
        }

        StringBuilder currentTapeString = new StringBuilder();
        if (currentTapeModel.getHead() == currentTapeModel.tapeSize()) {
            currentTapeString.append("\u03B5");
        } else {
            for (int i = currentTapeModel.getHead(); i < currentTapeModel.tapeSize(); i++) {
                currentTapeString.append(currentTapeModel.getInputTape().get(i));
            }
        }

        ConfigurationModel newConfig = new ConfigurationModel(currentConfig, transitionModelToNextConfiguration, transitionModelToNextConfiguration.getResultingStateModel(), currentTapeString.toString(), currentTapeModel.getHead(), currentStackModel.getContent());
        return newConfig;
    }


    public boolean isInAcceptingConfiguration() {
        if (tapeModel.isEmpty()) {
            if (machineModel.isAcceptanceByFinalState() && currentConfig.getCurrentStateModel().isFinalState()) {
                return true;
            } else if (machineModel.isAcceptanceByEmptyStack() && currentConfig.getStackContent().isEmpty()) {
                return true;
            }
            currentConfig.setFailConfig(true);
            leafConfigurationPath.add(currentConfig);
        }
        return false;
    }

    public int run() {
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


