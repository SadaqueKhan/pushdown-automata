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
    public StateModel() {
        this.stateId = "Q" + (num++);
    }
    public StateModel(String stateID) {
        this.stateId = stateID;
    }
    @XmlAttribute
    public String getStateId() {
        return stateId;
    }
    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
    @XmlAttribute
    public double getxCoordinateOnDiagram() {
        return xCoordinateOnDiagram;
    }
    public void setxCoordinateOnDiagram(double xCoordinateOnDiagram) {
        this.xCoordinateOnDiagram = xCoordinateOnDiagram;
    }
    @XmlAttribute
    public double getyCoordinateOnDiagram() {
        return yCoordinateOnDiagram;
    }
    public void setyCoordinateOnDiagram(double yCoordinateOnDiagram) {
        this.yCoordinateOnDiagram = yCoordinateOnDiagram;
    }
    public boolean isStartState() {
        return isStartState;
    }
    public void setStartState(boolean startState) {
        isStartState = startState;
    }
    public boolean isFinalState() {
        return isFinalState;
    }
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
