package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationModel {

    private ArrayList<TransitionModel> pathList;


    static final String EMPTY = "\u03B5";


    private InputTape inputTape;
    private Stack stack;
    private Configuration currentConfig;
    private ArrayList<Configuration> configurations;

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
        currentConfig = new Configuration(null, machineModel.findStartStateModel(), 0, new ArrayList<String>());
        configurations.add(currentConfig);
        currentConfig.markAsVisited();
    }

    public int next() {
        if (isInAcceptingConfiguration()) {
            return 5;
        }

        //Retrieve applicable configurations stored in the current configuration
        List<Configuration> applicableConfigurations = currentConfig.getConfigurations();


        if (currentConfig.getConfigurations() == null) {
            //Search for applicable configurations stored in current configuration
            applicableConfigurations = configurationApplicable(currentConfig.getCurrentStateModel(), inputTape.getAtHead(), stack.peak());
            currentConfig.setConfigurations(applicableConfigurations);
        }

        Configuration toExplore;

        if (applicableConfigurations.isEmpty()) {
            //flopped
            if (currentConfig.getPreviousConfiguration() == null) {
                return -1;
            }
            return 8;
        }


        if (applicableConfigurations.size() == 1) {
            //Explore deterministic path
            toExplore = applicableConfigurations.get(0);
        } else {
            //Explore non-deterministic path

            //Check if the c
            toExplore = applicableConfigurations.stream().filter(config -> !config.isVisited()).findFirst().orElse(null);

            if (currentConfig.getPreviousConfiguration() == null) {
                return -1;
            }

            if (toExplore == null) {
                return 8;
            }
        }

        loadConfiguration(toExplore);
        return 1;
    }

    private void loadConfiguration(Configuration toExplore) {
        currentConfig = toExplore;
        configurations.add(currentConfig);
        currentConfig.markAsVisited();
        inputTape.setHead(toExplore.getHeadPosition());
        stack.setContent(toExplore.getStackContent());
    }

    public void previous() {
        Configuration previous = currentConfig.getPreviousConfiguration();
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

    private Configuration generateConfig(TransitionModel transitionModel) {

        int currentHead = inputTape.getHead();
        ArrayList<String> currentStack = new ArrayList<>(stack.getContent());

        if (!(transitionModel.getInputSymbol().equals(EMPTY))) {
            ++currentHead;
        }
        if (!(transitionModel.getStackSymbolToPop().equals(EMPTY))) {
            currentStack.remove(currentStack.size() - 1);
        }
        if (!(transitionModel.getStackSymbolToPush().equals(EMPTY))) {
            currentStack.add(transitionModel.getStackSymbolToPush());
        }

        Configuration newConfig = new Configuration(currentConfig, transitionModel.getResultingStateModel(), currentHead, currentStack);
        return newConfig;
    }


    public boolean isInAcceptingConfiguration() {
        return inputTape.isEmpty() && currentConfig.getCurrentStateModel().isFinalState();
    }


    public int run() {
        while (true) {
            int result = next();

            if (result == 5 || result == -1) {
                return result;
            }

            if (result == 8) {
                previous();
            }
        }

    }

}


