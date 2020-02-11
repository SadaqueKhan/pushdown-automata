package app.models;

public class TransitionModel {

    private StateModel currentStateModel;
    private String inputSymbol;
    private String stackSymbolToPop;

    private StateModel resultingStateModel;
    private String stackSymbolToPush;

    public TransitionModel(
            StateModel currentStateModel,
            String inputSymbol,
            String stackSymbolToPop,

            StateModel resultingStateModel,
            String stackSymbolToPush
    ) {
        this.currentStateModel = currentStateModel;
        this.inputSymbol = inputSymbol;
        this.stackSymbolToPop = stackSymbolToPop;

        this.resultingStateModel = resultingStateModel;
        this.stackSymbolToPush = stackSymbolToPush;

    }


    public StateModel getCurrentStateModel() {
        return currentStateModel;
    }

    public void setCurrentStateModel(StateModel currentStateModel) {
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

    public StateModel getResultingStateModel() {
        return resultingStateModel;
    }

    public void setResultingStateModel(StateModel resultingStateModel) {
        this.resultingStateModel = resultingStateModel;
    }

    public String getStackSymbolToPush() {
        return stackSymbolToPush;
    }

    public void setStackSymbolToPush(String stackSymbolToPush) {
        this.stackSymbolToPush = stackSymbolToPush;
    }

    @Override
    public String toString() {
        return "(" + currentStateModel + ", " + inputSymbol + ", " + stackSymbolToPop + ") " + "->" + " (" + resultingStateModel + ", " + stackSymbolToPush + ")";
    }
}
