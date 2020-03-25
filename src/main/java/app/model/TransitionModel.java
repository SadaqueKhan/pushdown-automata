package app.model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a transition, consisting of elements commonly found to define a transition in pushdown automata theory
 * and coordinates for which the transition is located on the diagram scene.
 * </p>
 */
@XmlRootElement
public class TransitionModel {
    private StateModel currentStateModel;
    private String inputSymbol;
    private String stackSymbolToPop;
    private StateModel resultingStateModel;
    private String stackSymbolToPush;
    private double xCoordinateOnDiagram = 0.0;
    private double yCoordinateOnDiagram = 0.0;
    /**
     * Empty constructor needed for saving/loading using JAXB Marshaller interface for serializing Java content trees
     * i.e. Java objects to XML data.
     */
    public TransitionModel() {
    }
    /**
     * Constructor that initialises a transition model by the specifying the elements commonly found to define a
     * transition in a pushdown automata theory.
     * @param currentStateModel the current state which this transition starts from.
     * @param inputSymbol the input symbol required to use this transition to move to another state.
     * @param stackSymbolToPop the stack symbol to pop required to use this transition to move to another state.
     * @param resultingStateModel the resulting state which this transition moves to.
     * @param stackSymbolToPush the stack symbol to pop to complete action to move to another state using this
     * transition.
     */
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
    /**
     * Get the x coordinate for which this state is located on the view.
     * @return the x coordinate for which this state is located on the view.
     */
    public double getXCoordinateOnDiagram() {
        return xCoordinateOnDiagram;
    }
    /**
     * Set the x coordinate for which this state is located on the view.
     * @param xCoordinateOnDiagram the x coordinate set for which this state is located on the view.
     */
    public void setXCoordinateOnDiagram(double xCoordinateOnDiagram) {
        this.xCoordinateOnDiagram = xCoordinateOnDiagram;
    }
    /**
     * Get the y coordinate for which this state is located on the view.
     * @return the y coordinate for which this state is located on the view.
     */
    public double getYCoordinateOnDiagram() {
        return yCoordinateOnDiagram;
    }
    /**
     * Set the y coordinate for which this state is located on the view.
     * @param yCoordinateOnDiagram the y coordinate set for which this state is located on the view.
     */
    public void setYCoordinateOnDiagram(double yCoordinateOnDiagram) {
        this.yCoordinateOnDiagram = yCoordinateOnDiagram;
    }
    /**
     * Method checks if this object is equal to the object that is passed as an argument.
     * @param o object that is passed as an argument to check if it is equal to this object.
     * @return <tt>true</tt> if the argument is not null and is an object of the same type.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransitionModel)) return false;
        TransitionModel that = (TransitionModel) o;
        return (getCurrentStateModel() != null ? getCurrentStateModel().equals(that.getCurrentStateModel()) : that.getCurrentStateModel() == null) && (getInputSymbol() != null ? getInputSymbol().equals(that.getInputSymbol()) : that.getInputSymbol() == null) && (getStackSymbolToPop() != null ? getStackSymbolToPop().equals(that.getStackSymbolToPop()) : that.getStackSymbolToPop() == null) && (getResultingStateModel() != null ? getResultingStateModel().equals(that.getResultingStateModel()) : that.getResultingStateModel() == null) && (getStackSymbolToPush() != null ? getStackSymbolToPush().equals(that.getStackSymbolToPush()) : that.getStackSymbolToPush() == null);
    }
    /**
     * Get the {@code StateModel} which this transition starts from.
     * @return the {@code StateModel} which this transition starts from.
     */
    @XmlElement
    public StateModel getCurrentStateModel() {
        return currentStateModel;
    }
    /**
     * Set the the state which this transition starts from.
     * @param currentStateModel the state set for which this transition starts from.
     */
    public void setCurrentStateModel(StateModel currentStateModel) {
        this.currentStateModel = currentStateModel;
    }
    /**
     * Get the input symbol required to use this transition to move to another state.
     * @return the input symbol used by this transition.
     */
    @XmlElement
    String getInputSymbol() {
        return inputSymbol;
    }
    /**
     * Set the input symbol required to use this transition to move to another state.
     * @param inputSymbol the input symbol set for this transition.
     */
    public void setInputSymbol(String inputSymbol) {
        this.inputSymbol = inputSymbol;
    }
    /**
     * Get the stack symbol needed to pop to use this transition to move to another state.
     * @return the stack symbol to pop used by this transition.
     */
    @XmlElement
    String getStackSymbolToPop() {
        return stackSymbolToPop;
    }
    /**
     * Set the stack symbol needed to pop to use this transition to move to another state.
     * @param stackSymbolToPop the stack symbol to pop set for this transition.
     */
    public void setStackSymbolToPop(String stackSymbolToPop) {
        this.stackSymbolToPop = stackSymbolToPop;
    }
    /**
     * Get the {@code StateModel} which this transition points to.
     * @return the {@code StateModel} which this transition points to
     */
    @XmlElement
    public StateModel getResultingStateModel() {
        return resultingStateModel;
    }
    /**
     * Set the the state which this transition points to.
     * @param resultingStateModel the state set for which this transition points to.
     */
    public void setResultingStateModel(StateModel resultingStateModel) {
        this.resultingStateModel = resultingStateModel;
    }
    /**
     * Get the stack symbol needed to push to use this transition to move to another state.
     * @return the stack symbol to push used by this transition.
     */
    @XmlElement
    String getStackSymbolToPush() {
        return stackSymbolToPush;
    }
    /**
     * Set the stack symbol needed to push to use this transition to move to another state.
     * @param stackSymbolToPush the stack symbol to push set for this transition.
     */
    public void setStackSymbolToPush(String stackSymbolToPush) {
        this.stackSymbolToPush = stackSymbolToPush;
    }
    /**
     * Method to return an integer hash code value of this object.
     * @return the integer hash code value of the object.
     */
    @Override
    public int hashCode() {
        int result = getCurrentStateModel() != null ? getCurrentStateModel().hashCode() : 0;
        result = 31 * result + (getInputSymbol() != null ? getInputSymbol().hashCode() : 0);
        result = 31 * result + (getStackSymbolToPop() != null ? getStackSymbolToPop().hashCode() : 0);
        result = 31 * result + (getResultingStateModel() != null ? getResultingStateModel().hashCode() : 0);
        result = 31 * result + (getStackSymbolToPush() != null ? getStackSymbolToPush().hashCode() : 0);
        return result;
    }
    /**
     * Method to return a string representation of this object.
     * @return the string representation of the object.
     */
    @Override
    public String toString() {
        return "{ " + "[" + currentStateModel.getStateId() + ", " + inputSymbol + ", " + stackSymbolToPop + "] " + "->" + " [" + resultingStateModel.getStateId() + ", " + stackSymbolToPush + "]" + " }";
    }
}
