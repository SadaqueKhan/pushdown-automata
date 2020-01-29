package app.models;

public class TransitionModel {

    private String currentStateModel;
    private String inputSymbol;
    private String stackSymbolToPop;

    private String resultingStateModel;
    private String stackSymbolToPush;

    public TransitionModel(
            String currentStateModel,
            String inputSymbol,
            String stackSymbolToPop,

            String resultingStateModel,
            String stackSymbolToPush
    ) {
        this.currentStateModel = currentStateModel;
        this.inputSymbol = inputSymbol;
        this.stackSymbolToPop = stackSymbolToPop;

        this.resultingStateModel = resultingStateModel;
        this.stackSymbolToPush = stackSymbolToPush;

    }

    public String getCurrentStateModel() {
        return currentStateModel;
    }

    public void setCurrentStateModel(String currentStateModel) {
        this.currentStateModel = currentStateModel;
    }

    public String getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(String inputSymbol) {
        this.inputSymbol = inputSymbol;
    }

    public String getStackSymbolToPop() {
        return stackSymbolToPop;
    }

    public void setStackSymbolToPop(String stackSymbolToPop) {
        this.stackSymbolToPop = stackSymbolToPop;
    }

    
    public String getResultingStateModel() {
        return resultingStateModel;
    }

    public void setResultingStateModel(String resultingStateModel) {
        this.resultingStateModel = resultingStateModel;
    }

    public String getStackSymbolToPush() {
        return stackSymbolToPush;
    }

    public void setStackSymbolToPush(String stackSymbolToPush) {
        this.stackSymbolToPush = stackSymbolToPush;
    }
}
