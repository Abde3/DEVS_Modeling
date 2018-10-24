package Verification;

public class State {

    String name;
    float time;
    String state;

    public State(String name, float time, Enum state) {
        this.name = name;
        this.time = time;
        this.state = state.toString();
    }

    public State(String name, float time, String state) {
        this.name = name;
        this.time = time;
        this.state = state;
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", state='" + state + '\'' +
                '}';
    }
}
