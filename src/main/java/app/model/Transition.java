package app.model;

public class Transition {

    private String topStackSymbol;
    private String nextInputSymbol;
    private State currentState;
    
    public Transition(State currentState, String nextInputSymbol, String topStackSymbol) {
        this.currentState = currentState;
        this.nextInputSymbol = nextInputSymbol;
        this.topStackSymbol = topStackSymbol;

    }
}
