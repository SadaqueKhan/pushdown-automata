package app.model;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * @author Mohammed Sadaque Khan
 * <p>
 * Model of a transition, consisting of elements commonly found to define a state in push down automata theory
 * and coordinates for which the state is located on the diagram scene.
 * </p>
 */
@XmlRootElement
public class StateModel {
    private static int num = 0;
    private String stateId;
    private boolean isStartState = false;
    private boolean isFinalState = false;
    private double xCoordinateOnDiagram = 0.0;
    private double yCoordinateOnDiagram = 0.0;
    /**
     * Constructor that initialises a state model with a dynamic id.
     */
    public StateModel() {
        this.stateId = "Q" + (num++);
    }
    /**
     * Constructor that initialises a state model by specifying an id.
     * @param stateId the id for this state model.
     */
    public StateModel(String stateId) {
        this.stateId = stateId;
    }
    /**
     * Gets the id set for this state model.
     * @return the id for this state model.
     */
    @XmlAttribute
    public String getStateId() {
        return stateId;
    }
    /**
     * Sets the id set for this state model.
     * @param stateId the id used to set the id for this state model.
     */
    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
    /**
     * Gets the x coordinate for which this state representation is located on the view.
     * @return the x coordinate for which this state is located on the view.
     */
    @XmlAttribute
    public double getXCoordinateOnDiagram() {
        return xCoordinateOnDiagram;
    }
    /**
     * Sets the x coordinate for which this state representation is located on the view.
     * @param xCoordinateOnDiagram the x coordinate set for which this state is located on the view.
     */
    public void setXCoordinateOnDiagram(double xCoordinateOnDiagram) {
        this.xCoordinateOnDiagram = xCoordinateOnDiagram;
    }
    /**
     * Gets the y coordinate for which this state representation is located on the view.
     * @return the y coordinate for which this state is located on the view.
     */
    @XmlAttribute
    public double getYCoordinateOnDiagram() {
        return yCoordinateOnDiagram;
    }
    /**
     * Sets the x coordinate for which this state representation is located on the view.
     * @param yCoordinateOnDiagram the x coordinate set for which this state is located on the view.
     */
    public void setYCoordinateOnDiagram(double yCoordinateOnDiagram) {
        this.yCoordinateOnDiagram = yCoordinateOnDiagram;
    }
    /**
     * Checks whether this state is a start state.
     * @return <tt>true</tt> if the state is a start state.
     */
    public boolean isStartState() {
        return isStartState;
    }
    /**
     * Sets this state to be a start state if the specified value is <tt>true</tt>, otherwise the state is set
     * to not be a start state.
     * @param startState boolean value to specify whether this state is a start state.
     */
    public void setStartState(boolean startState) {
        isStartState = startState;
    }
    /**
     * Checks whether this state is a final state.
     * @return <tt>true</tt> if the state is a final state.
     */
    public boolean isFinalState() {
        return isFinalState;
    }
    /**
     * Sets this state to be a final state if the specified value is <tt>true</tt>, otherwise the state is set
     * to not be a final state.
     * @param finalState boolean value to specify whether this state is a final state.
     */
    public void setFinalState(boolean finalState) {
        isFinalState = finalState;
    }
    /**
     * Method checks if this object is equal to the object that is passed as an argument.
     * @param o object that is passed as an argument to check if it is equal to this object.
     * @return <tt>true</tt> if the argument is not null and is an object of the same type.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateModel)) return false;
        StateModel that = (StateModel) o;
        return getStateId().equals(that.getStateId());
    }
    /**
     * Method to return an integer hash code value of this object.
     * @return the integer hash code value of the object.
     */
    @Override
    public int hashCode() {
        return getStateId().hashCode();
    }
    /**
     * Method to return a string representation of this object.
     * @return the string representation of the object.
     */
    @Override
    public String toString() {
        return stateId;
    }
}
