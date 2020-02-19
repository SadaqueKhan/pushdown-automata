package app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationModel {
    private final String EMPTY = "\u03B5";

    private InputTape inputTape;
    private Stack stack;
    private Configuration currentConfig;
    private ArrayList<Configuration> configurations;

    HashMap<Integer, Configuration> successConfigurations = new HashMap<Integer, Configuration>();

    private int numOfSuccessPath = 0;

    private MachineModel machineModel;

    public SimulationModel(MachineModel machineModel, String inputWord) {
        inputTape = new InputTape();
        stack = new Stack();

        loadMachine(machineModel);
        loadInput(inputWord);
    }

    public void loadMachine(MachineModel machineModel) {
        this.machineModel = machineModel;
    }

    public void loadInput(String input) {
        configurations = new ArrayList<>();
        inputTape.loadInput(input);
        Stack stack = new Stack();
        currentConfig = new Configuration(null, machineModel.findStartStateModel(), 0, stack.getContent());
        configurations.add(currentConfig);
        currentConfig.markAsVisited();
    }

    public int next() {
        //checking for acceptance
        if (isInAcceptingConfiguration()) {
            return 100;
        }

        // If at a child and all input tape is read but no acceptance
        if (inputTape.isEmpty()) {
            return 8;
        }
        // If at a child and stack is empty but no acceptance found and not before reading an input symbol
        if (stack.isEmpty() && inputTape.getHead() != 0) {
            return 8;
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
            //no more paths to search for this child i.e. failed
            return 8; // Go back to parent
        }


        //Parent has a single child, then a check if path is deterministic
        if (applicableConfigurations.size() == 1) {

            //Explore deterministic path
            toExplore = applicableConfigurations.get(0);

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
        configurations.add(currentConfig);
        currentConfig.markAsVisited(); // Mark the currently explored config as explored
        inputTape.setHead(toExplore.getHeadPosition());
        stack.setContent(toExplore.getStackContent());
    }

    public void previous() {
        Configuration previous = currentConfig.getParentConfiguration();
        stack.setContent(previous.getStackContent());
        inputTape.setHead(previous.getHeadPosition());
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
    private Configuration generateConfig(TransitionModel transitionModel) {
        int currentHead = inputTape.getHead();

        Stack currentStack = new Stack();
        currentStack.setContent(stack.getContent());

        if (!(transitionModel.getInputSymbol().equals(EMPTY))) {
            ++currentHead;
        }
        if (!(transitionModel.getStackSymbolToPop().equals(EMPTY))) {
            currentStack.pop();
        }
        if (!(transitionModel.getStackSymbolToPush().equals(EMPTY))) {
            currentStack.push(transitionModel.getStackSymbolToPush());
        }

        Configuration newConfig = new Configuration(currentConfig, transitionModel.getResultingStateModel(), currentHead, currentStack.getContent());
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
        while (true) {
            int result = next(); // if one is returned more children exist

            if (result == 100) {
                ++numOfSuccessPath;
                successConfigurations.put(numOfSuccessPath, currentConfig);
                previous();
                //Check if children have all be explored of root
                Configuration toExplore = currentConfig.getChildrenConfigurations().stream().filter(config -> !config.isVisited()).findFirst().orElse(null);

                if (currentConfig.getParentConfiguration() == null && toExplore == null) {
                    return 200;
                }

            }

            if (result == 8) {
                previous();
                //Check if children have all be explored of root
                Configuration toExplore = currentConfig.getChildrenConfigurations().stream().filter(config -> !config.isVisited()).findFirst().orElse(null);

                if (currentConfig.getParentConfiguration() == null && toExplore == null) {
                    return 200;
                }
            }
        }

    }

    public HashMap<Integer, Configuration> getSuccessConfigurations() {
        return successConfigurations;
    }
}


