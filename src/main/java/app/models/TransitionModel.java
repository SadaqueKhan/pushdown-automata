package app.models;

public class TransitionModel {

    private String topStackSymbol;
    private String nextInputSymbol;
    private StateModel currentStateModel;

    public TransitionModel(StateModel currentStateModel, String nextInputSymbol, String topStackSymbol) {
        this.currentStateModel = currentStateModel;
        this.nextInputSymbol = nextInputSymbol;
        this.topStackSymbol = topStackSymbol;

    }
}
