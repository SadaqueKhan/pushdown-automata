package app.model;

public class Transition {


    private ControlState current;
    private String inputSymbol;
    private String symbolToPop;

    private ControlState resulting;
    private String symbolToPush;


    public Transition(ControlState current, String inputSymbol, String symbolToPop, ControlState resulting, String symbolToPush) {
        this.current = current;
        this.inputSymbol = inputSymbol;
        this.symbolToPop = symbolToPop;
        this.resulting = resulting;
        this.symbolToPush = symbolToPush;
    }

    public ControlState getCurrent() {
        return current;
    }

    public void setCurrent(ControlState current) {
        this.current = current;
    }

    public String getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(String inputSymbol) {
        this.inputSymbol = inputSymbol;
    }

    public String getSymbolToPop() {
        return symbolToPop;
    }

    public void setSymbolToPop(String symbolToPop) {
        this.symbolToPop = symbolToPop;
    }

    public ControlState getResulting() {
        return resulting;
    }

    public void setResulting(ControlState resulting) {
        this.resulting = resulting;
    }

    public String getSymbolToPush() {
        return symbolToPush;
    }

    public void setSymbolToPush(String symbolToPush) {
        this.symbolToPush = symbolToPush;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transition)) return false;

        Transition that = (Transition) o;

        if (!getCurrent().equals(that.getCurrent())) return false;
        if (!getInputSymbol().equals(that.getInputSymbol())) return false;
        if (!getSymbolToPop().equals(that.getSymbolToPop())) return false;
        if (!getResulting().equals(that.getResulting())) return false;
        return getSymbolToPush().equals(that.getSymbolToPush());
    }

    @Override
    public int hashCode() {
        int result = getCurrent().hashCode();
        result = 31 * result + getInputSymbol().hashCode();
        result = 31 * result + getSymbolToPop().hashCode();
        result = 31 * result + getResulting().hashCode();
        result = 31 * result + getSymbolToPush().hashCode();
        return result;
    }
}
