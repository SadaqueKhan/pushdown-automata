package app.models;

public class ControlState {

    private boolean isFinal;
    private String name;


    public ControlState(boolean isInitial, boolean isFinal, String name) {
        this.isFinal = isFinal;
        this.name = name;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ControlState)) return false;

        ControlState that = (ControlState) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
