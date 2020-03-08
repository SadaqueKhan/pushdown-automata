package app.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateModel)) return false;

        StateModel that = (StateModel) o;

        return getStateId().equals(that.getStateId());
    }

    @Override
    public int hashCode() {
        return getStateId().hashCode();
    }

}
