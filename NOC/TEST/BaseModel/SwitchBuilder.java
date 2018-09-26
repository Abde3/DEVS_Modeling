package BaseModel;


import NocTopology.NOCDirections.IPoint;

import java.util.Arrays;
import java.util.Set;
import java.util.Vector;

public class SwitchBuilder {
    private Vector<String> inputDataPortsNames;
    private Vector<String> outputDataPortsNames;
    private Vector<String> inputCmdPortsNames;
    private Vector<String> outputCmdPortsNames;
    private IPoint coordinate;

    public SwitchBuilder withDataInputPorts(Set<String> inputPorts) {
        this.inputDataPortsNames = new Vector<>(inputPorts);
        return this;
    }

    public SwitchBuilder withDataOutputPorts(Set<String> outputPorts) {
        this.outputDataPortsNames = new Vector<>(outputPorts);
        return this;
    }

    public SwitchBuilder withCmdInputPorts(Set<String> inputPorts) {
        this.inputCmdPortsNames = new Vector<>(inputPorts);
        return this;
    }

    public SwitchBuilder withCmdOutputPorts(Set<String> outputPorts) {
        this.outputCmdPortsNames = new Vector<>(outputPorts);
        return this;
    }

    public SwitchBuilder withCoordinate(IPoint coordinate) {
        this.coordinate = coordinate;
        return this;
    }

    public Switch build() {
        return new Switch(coordinate, inputDataPortsNames, outputDataPortsNames, inputCmdPortsNames, outputCmdPortsNames);
    }
}