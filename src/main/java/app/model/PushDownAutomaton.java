package app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PushDownAutomaton {

    static final String EMPTY = "\u03B5";


    private InputTape inputTape;
    private Stack stack;
    private Configuration currentConfig;
    private ArrayList<Configuration> configurations;

    private Definition definition;

    public PushDownAutomaton() {
        inputTape = new InputTape();
        stack = new Stack();
    }

    public void loadDefinition(Definition definition) {
        this.definition = definition;
    }

    public void loadInput(String input) {
        configurations = new ArrayList<>();
        inputTape.loadInput(input);
        currentConfig = new Configuration(null, definition.getInitialControlState(), 0, new ArrayList<String>());
        configurations.add(currentConfig);
        currentConfig.markAsVisited();
    }

    public int next() {
        if (isInAcceptingConfiguration()) {
            return 5;
        }
        List<Configuration> applicableConfigurations = currentConfig.getConfigurations();
        if (currentConfig.getConfigurations() == null) {
            applicableConfigurations = configurationApplicable(currentConfig.getCurrentControlState(), inputTape.getAtHead(), stack.peak());
            currentConfig.setConfigurations(applicableConfigurations);
        }

        Configuration toExplore;

        if (applicableConfigurations.size() == 0) {
            //flopped
            if (currentConfig.getPreviousConfiguration() == null) {
                return -1;
            }

            return 8;
        }

        if (applicableConfigurations.size() == 1) {
            toExplore = applicableConfigurations.get(0);
        } else {
            //non-detministic
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


    public List<Configuration> configurationApplicable(ControlState controlState, String inputSymbol, String stackSymbol) {
        return definition.getTransitions()
                .stream()
                .filter(transition -> transition.getCurrent().equals(controlState))
                .filter(transition -> transition.getInputSymbol().equals(inputSymbol) || transition.getInputSymbol().equals(EMPTY))
                .filter(transition -> transition.getSymbolToPop().equals(stackSymbol) || transition.getSymbolToPop().equals(EMPTY))
                .map(transition -> generateConfig(transition))
                .collect(Collectors.toList());
    }

    private Configuration generateConfig(Transition transition) {

        int currentHead = inputTape.getHead();
        ArrayList<String> currentStack = new ArrayList<>(stack.getContent());

        if (!(transition.getInputSymbol().equals(EMPTY))) {
            ++currentHead;
        }
        if (!(transition.getSymbolToPop().equals(EMPTY))) {
            currentStack.remove(currentStack.size() - 1);
        }
        if (!(transition.getSymbolToPush().equals(EMPTY))) {
            currentStack.add(transition.getSymbolToPush());
        }

        Configuration newConfig = new Configuration(currentConfig, transition.getResulting(), currentHead, currentStack);
        return newConfig;
    }


    public boolean isInAcceptingConfiguration() {
        return inputTape.isEmpty() && currentConfig.getCurrentControlState().isFinal();
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
