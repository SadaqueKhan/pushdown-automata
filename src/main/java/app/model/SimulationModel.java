package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationModel {
    private final String EMPTY = "\u03B5";


    private MachineModel machineModel;
    private InputTape inputTape;
    private Stack stack;
    private final String inputWord;

    private Configuration currentConfig;

    private ArrayList<Configuration> configurationPath;

    public SimulationModel(MachineModel machineModel, String inputWord) {
        this.inputTape = new InputTape();
        this.stack = new Stack();
        this.inputWord = inputWord;

        loadMachine(machineModel);
        loadInput(inputWord);
    }

    public void loadMachine(MachineModel machineModel) {
        this.machineModel = machineModel;
    }

    public void loadInput(String input) {
        inputTape.loadInput(input);
        currentConfig = new Configuration(null, null, machineModel.findStartStateModel(), inputWord, 0, stack.getContent());
        currentConfig.markAsVisited();
        //Add currentConfig to the path
        configurationPath = new ArrayList<>();
        configurationPath.add(currentConfig);
    }

    public int next() {
        //checking for acceptance
        if (isInAcceptingConfiguration() && !(currentConfig.isSuccessConfig())) {
            return 100;
        }

        //Retrieve applicable configurations stored in the current configuration
        List<Configuration> applicableConfigurations = currentConfig.getChildrenConfigurations();

        if (currentConfig.getChildrenConfigurations() == null) {
            //Search for possible children configurations to move to (the transitions have already been applied to this list)
            applicableConfigurations = configurationApplicable(currentConfig.getCurrentStateModel(), inputTape.getAtHead(), stack.peak());
            currentConfig.setChildrenConfigurations(applicableConfigurations);
        }

        Configuration toExplore;

        // Parent has no children i.e. no applicable transitions
        if (applicableConfigurations.isEmpty()) {
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

    private void loadConfiguration(Configuration toExplore) {
        currentConfig = toExplore;
        currentConfig.markAsVisited(); // Mark the currently explored config as explored
        inputTape.setHead(toExplore.getHeadPosition());
        stack.setContent(toExplore.getStackContent());
        configurationPath.add(currentConfig);
    }

    public void previous() {
        Configuration previous = currentConfig.getParentConfiguration();
        if (previous != null) {
            stack.setContent(previous.getStackContent());
            inputTape.setHead(previous.getHeadPosition());
        }
        currentConfig = previous;
    }


    public List<Configuration> configurationApplicable(StateModel stateModel, String inputSymbol, String stackSymbol) {
        return machineModel.getTransitionModelSet()
                .stream()
                .filter(transitionModel -> transitionModel.getCurrentStateModel().equals(stateModel))
                .filter(transitionModel -> transitionModel.getInputSymbol().equals(inputSymbol) || transitionModel.getInputSymbol().equals(EMPTY))
                .filter(transitionModel -> transitionModel.getStackSymbolToPop().equals(stackSymbol) || transitionModel.getStackSymbolToPop().equals(EMPTY))
                .map(transitionModel -> generateConfig(transitionModel))
                .collect(Collectors.toList());
    }

    //Apply action given a transition and return the resulting configuration
    private Configuration generateConfig(TransitionModel transitionModelToNextConfiguration) {

        InputTape currentInputTape = new InputTape();
        currentInputTape.loadInput(inputWord);
        currentInputTape.setHead(inputTape.getHead());

        Stack currentStack = new Stack();
        currentStack.setContent(stack.getContent());

        if (!(transitionModelToNextConfiguration.getInputSymbol().equals(EMPTY))) {
            currentInputTape.readSymbol();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPop().equals(EMPTY))) {
            currentStack.pop();
        }
        if (!(transitionModelToNextConfiguration.getStackSymbolToPush().equals(EMPTY))) {
            currentStack.push(transitionModelToNextConfiguration.getStackSymbolToPush());
        }

        StringBuilder currentTapeString = new StringBuilder();
        if (currentInputTape.getHead() == currentInputTape.tapeSize()) {
            currentTapeString.append("\u03B5");
        } else {
            for (int i = currentInputTape.getHead(); i < currentInputTape.tapeSize(); i++) {
                currentTapeString.append(currentInputTape.getInputTape().get(i));
            }
        }

        Configuration newConfig = new Configuration(currentConfig, transitionModelToNextConfiguration, transitionModelToNextConfiguration.getResultingStateModel(), currentTapeString.toString(), currentInputTape.getHead(), currentStack.getContent());
        return newConfig;
    }


    public boolean isInAcceptingConfiguration() {
        if (inputTape.isEmpty()) {
            if (machineModel.isAcceptanceByFinalState() && currentConfig.getCurrentStateModel().isFinalState()) {
                return true;
            } else if (machineModel.isAcceptanceByEmptyStack() && currentConfig.getStackContent().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int run() {
        while (currentConfig.getStep() < 40) {

            int result = next(); // if one is returned more children exist

            if (result == 100) {
                currentConfig.setSuccessConfig(true);
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
        }

        // mean infinite loop
        return 300;
    }

    public ArrayList<Configuration> getConfigurationPath() {
        return configurationPath;
    }
}


