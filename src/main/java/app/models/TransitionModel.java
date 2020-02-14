package app.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;

@XmlRootElement
public class TransitionModel {

    private StateModel currentStateModel;
    private String inputSymbol;
    private String stackSymbolToPop;

    private StateModel resultingStateModel;
    private String stackSymbolToPush;

    private HashSet<TransitionModel> relatedTransitionModels = new HashSet<>();

    public TransitionModel() {
    }

    public TransitionModel(
            StateModel currentStateModel,
            String inputSymbol,
            String stackSymbolToPop,

            StateModel resultingStateModel,
            String stackSymbolToPush
    ) {
        super();
        this.currentStateModel = currentStateModel;
        this.inputSymbol = inputSymbol;
        this.stackSymbolToPop = stackSymbolToPop;

        this.resultingStateModel = resultingStateModel;
        this.stackSymbolToPush = stackSymbolToPush;

    }

    @XmlElement
    public StateModel getCurrentStateModel() {
        return currentStateModel;
    }

    public void setCurrentStateModel(StateModel currentStateModel) {
        this.currentStateModel = currentStateModel;
    }

    @XmlElement
    public String getInputSymbol() {
        return inputSymbol;
    }

    public void setInputSymbol(String inputSymbol) {
        this.inputSymbol = inputSymbol;
    }

    @XmlElement
    public String getStackSymbolToPop() {
        return stackSymbolToPop;
    }

    public void setStackSymbolToPop(String stackSymbolToPop) {
        this.stackSymbolToPop = stackSymbolToPop;
    }

    @XmlElement
    public StateModel getResultingStateModel() {
        return resultingStateModel;
    }

    public void setResultingStateModel(StateModel resultingStateModel) {
        this.resultingStateModel = resultingStateModel;
    }

    @XmlElement
    public String getStackSymbolToPush() {
        return stackSymbolToPush;
    }

    public void setStackSymbolToPush(String stackSymbolToPush) {
        this.stackSymbolToPush = stackSymbolToPush;
    }

    public HashSet<TransitionModel> getRelatedTransitionModels() {
        return relatedTransitionModels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionModel)) return false;

        TransitionModel that = (TransitionModel) o;

        if (getCurrentStateModel() != null ? !getCurrentStateModel().equals(that.getCurrentStateModel()) : that.getCurrentStateModel() != null)
            return false;
        if (getInputSymbol() != null ? !getInputSymbol().equals(that.getInputSymbol()) : that.getInputSymbol() != null)
            return false;
        if (getStackSymbolToPop() != null ? !getStackSymbolToPop().equals(that.getStackSymbolToPop()) : that.getStackSymbolToPop() != null)
            return false;
        if (getResultingStateModel() != null ? !getResultingStateModel().equals(that.getResultingStateModel()) : that.getResultingStateModel() != null)
            return false;
        return getStackSymbolToPush() != null ? getStackSymbolToPush().equals(that.getStackSymbolToPush()) : that.getStackSymbolToPush() == null;
    }

    @Override
    public int hashCode() {
        int result = getCurrentStateModel() != null ? getCurrentStateModel().hashCode() : 0;
        result = 31 * result + (getInputSymbol() != null ? getInputSymbol().hashCode() : 0);
        result = 31 * result + (getStackSymbolToPop() != null ? getStackSymbolToPop().hashCode() : 0);
        result = 31 * result + (getResultingStateModel() != null ? getResultingStateModel().hashCode() : 0);
        result = 31 * result + (getStackSymbolToPush() != null ? getStackSymbolToPush().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "[" + currentStateModel + ", " + inputSymbol + ", " + stackSymbolToPop + "] " + "->" + " [" + resultingStateModel + ", " + stackSymbolToPush + "]";
    }
}
